package basketbandit.core.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

public class GuildMusicManager {

    public final AudioPlayer player;
    public final TrackScheduler scheduler;
    public final AudioPlayerSendHandler sendHandler;

    /**
     * Creates a player and a track scheduler.
     * Without this class, trying to get persistent players, schedules, ect,
     * over an instanced modules based bot would be pretty difficult.
     * @param manager Audio player manager to use for creating the player.
     */
    public GuildMusicManager(AudioPlayerManager manager) {
        player = manager.createPlayer();
        scheduler = new TrackScheduler(player);
        sendHandler = new AudioPlayerSendHandler(player);
        player.addListener(scheduler);
    }

}
