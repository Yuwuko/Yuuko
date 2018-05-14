package basketbandit.core.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class TrackScheduler extends AudioEventAdapter {

    private boolean repeating = false;
    private final AudioPlayer player;
    public final Queue<AudioTrack> queue;
    private AudioTrack lastTrack;

    /**
     * @param player The audio player this scheduler uses
     */
    public TrackScheduler(AudioPlayer player) {
        this.player = player;
        this.queue = new LinkedList<>();
    }

    /**
     * Add the next track to queue or play right away if nothing is in the queue.
     * @param track The track to play or add to queue.
     */
    public void queue(AudioTrack track) {
        if(!player.startTrack(track, true)) {
            queue.offer(track);
        }
    }

    /**
     * Start the next track, stopping the current one if it is playing.
     */
    public void nextTrack() {
        player.startTrack(queue.poll(), false);
    }

    /**
     * What to do when the current track ends.
     * @param player; AudioPlayer.
     * @param track; Track.
     * @param endReason; AudioTrackReason
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
     * What to do if the track gets stuck (sometimes happens at the end)
     * @param player; AudioPlayer
     * @param track; Track
     * @param thresholdMs; Long.
     */
    @Override
    public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
        if(thresholdMs > 1000) {
            player.startTrack(queue.poll(), false);
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
     * Shuffles the playlist.
     */
    public void shuffle() {
        Collections.shuffle((List<?>) queue);
    }
}
