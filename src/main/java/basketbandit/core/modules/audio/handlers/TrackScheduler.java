package basketbandit.core.modules.audio.handlers;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class TrackScheduler extends AudioEventAdapter {

    private AudioTrack background = null;
    private boolean repeating = false;
    private final AudioPlayer player;
    public final Queue<AudioTrack> queue;
    private AudioTrack lastTrack;

    /**
     * @param player The handlers player this scheduler uses
     */
    TrackScheduler(AudioPlayer player) {
        this.player = player;
        this.queue = new LinkedList<>();
    }

    /**
     * Add the next track to queue or play right away if nothing is in the queue.
     * If the queue is empty play the background handlers if it isn't null.
     * @param track The track to play or add to queue.
     * @return if the queue was successful.
     */
    public boolean queue(AudioTrack track) {
        if(player.getPlayingTrack() == background) {
            player.startTrack(track, false);
            return true;
        } else if(!player.startTrack(track, true)) {
            queue.offer(track);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Start the next track, stopping the current one if it is playing.
     * @return if the next track was able to play.
     */
    public boolean nextTrack() {
        if(!player.startTrack(queue.poll(), false)) {
            if(background != null) {
                player.startTrack(background.makeClone(), false);
            }
        }
        return true;
    }

    /**
     * What to do when the current track ends.
     * @param player; AudioPlayer.
     * @param track; Track.
     * @param endReason; AudioTrackEndReason
     */
    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        this.lastTrack = track;

        // Only start the next track if the end reason is suitable for it (FINISHED or LOAD_FAILED)
        if(endReason.mayStartNext) {
            if(repeating) {
                player.startTrack(lastTrack.makeClone(), false);
            } else {
                nextTrack();
            }
        }

    }

    /**
     * What to do if the track gets stuck.
     * @param player; AudioPlayer
     * @param track; Track.
     * @param thresholdMs; Long
     */
    @Override
    public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
        if(!player.startTrack(queue.poll(), false)) {
            if(background != null) player.startTrack(background.makeClone(), false);
        }
    }

    /**
     * Checks if repeating.
     * @return boolean repeating.
     */
    public boolean isRepeating() {
        return repeating;
    }

    /**
     * Sets repeating.
     * @param repeating; boolean.
     */
    public void setRepeating(boolean repeating) {
        this.repeating = repeating;
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
     * @param track;
     */
    public void setBackground(AudioTrack track) {
        background = track;
    }
}
