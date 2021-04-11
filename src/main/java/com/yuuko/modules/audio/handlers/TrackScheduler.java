package com.yuuko.modules.audio.handlers;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.yuuko.database.function.GuildFunctions;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.modules.audio.commands.CurrentCommand;
import com.yuuko.scheduler.ScheduleHandler;
import com.yuuko.scheduler.jobs.VoiceTimeoutJob;
import com.yuuko.utilities.TextUtilities;
import lavalink.client.player.IPlayer;
import lavalink.client.player.LavalinkPlayer;
import lavalink.client.player.event.PlayerEventListenerAdapter;
import net.dv8tion.jda.api.entities.Guild;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ScheduledFuture;

public class TrackScheduler extends PlayerEventListenerAdapter {
    private static final Logger log = LoggerFactory.getLogger(TrackScheduler.class);
    private final Guild guild;
    private AudioTrack background = null;
    private AudioTrack lastTrack = null;
    private boolean looping = false;
    private final LavalinkPlayer player;
    public final Queue<AudioTrack> queue;
    private ScheduledFuture<?> timeout; // If nothing is playing for 5 minutes, remove bot from voice channel to save resources.

    TrackScheduler(Guild guild, LavalinkPlayer player) {
        this.guild = guild;
        this.player = player;
        this.queue = new LinkedList<>();
    }

    /**
     * Quick check to see if there is a next track or not.
     * @return boolean
     */
    public boolean hasNextTrack() {
        return (queue.peek() != null);
    }

    /**
     * Add the next track to queue or play right away if nothing is in the queue.
     * If the queue is empty play the background handlers if it isn't null.
     * @param track {@link AudioTrack}
     */
    public void queue(AudioTrack track) {
        if(player.getPlayingTrack() == null || (background != null && player.getPlayingTrack() == background)) {
            queue.add(track);
            nextTrack();
            return;
        }

        queue.offer(track);
    }

    /**
     * Start the next track when the previous has finished.
     */
    public void nextTrack() {
        try {
            AudioTrack track = queue.poll();

            if(track == null) {
                if(background != null) {
                    background = background.makeClone();
                    player.playTrack(background);
                    return;
                }
                timeout = ScheduleHandler.registerUniqueJob(new VoiceTimeoutJob(guild));

                // If skip is used and nothing else is in the queue, we want to stop the track anyway.
                if(player.getPlayingTrack() != null) {
                    player.stopTrack();
                }
                return;
            }

            player.playTrack(track);
        } catch(Exception e) {
            log.warn("TrackScheduler issue detected: {}", e.getMessage(), e);
            // This exception occurs 99% of the time on repeating tracks.
        }
    }

    @Override
    public void onTrackStart(IPlayer player, AudioTrack track) {
        try {
            // Cancel timeout onStart if it is currently set
            if(timeout != null) {
                timeout.cancel(true);
                timeout = null;
            }

            MessageEvent context = (MessageEvent) track.getUserData();
            if(context != null && TextUtilities.toBoolean(GuildFunctions.getGuildSetting("playnotifications", context.getGuild().getId()))) {
                new CurrentCommand().onCommand(context.setParameters("no-reply"));
            }
        }  catch(Exception exception) {
            log.debug(exception.getMessage());
        }
    }

    /**
     * What to do when the current track ends.
     * @param player IPlayer.
     * @param track AudioTrack.
     * @param endReason AudioTrackEndReason
     */
    @Override
    public void onTrackEnd(IPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        this.lastTrack = track;

        // Only start the next track if the end reason is suitable for it (FINISHED or LOAD_FAILED)
        if(endReason.mayStartNext) {
            if(looping) {
                queue.add(track);
            }
            nextTrack();
        }
    }

    @Override
    public void onPlayerPause(IPlayer player) {
        super.onPlayerPause(player);
    }

    @Override
    public void onPlayerResume(IPlayer player) {
        super.onPlayerResume(player);
    }

    @Override
    public void onTrackException(IPlayer player, AudioTrack track, Exception exception) {
        super.onTrackException(player, track, exception);
    }

    @Override
    public void onTrackStuck(IPlayer player, AudioTrack track, long thresholdMs) {
        Queue<AudioTrack> temp = new LinkedList<>();
        temp.add(track.makeClone());
        temp.addAll(queue);
        AudioManager.resetStuckGuildAudioManager(guild, temp);
    }

    /**
     * Checks if looping.
     * @return boolean looping.
     */
    public boolean isLooping() {
        return looping;
    }

    /**
     * Sets looping.
     * @param looping boolean.
     */
    public void setLooping(boolean looping) {
        this.looping = looping;
    }

    /**
     * Returns the last track.
     * @return lasttrack.
     */
    public AudioTrack getLastTrack() {
        return lastTrack;
    }

    /**
     * Shuffles the playlist.
     */
    public void shuffle() {
        Collections.shuffle((List<?>) queue);
    }

    /**
     * Sets the background track.
     * @param track audio track
     */
    public void setBackground(AudioTrack track) {
        background = track;
    }
}
