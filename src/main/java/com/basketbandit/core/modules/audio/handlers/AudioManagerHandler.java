package com.basketbandit.core.modules.audio.handlers;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.bandcamp.BandcampAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.http.HttpAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.soundcloud.SoundCloudAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.twitch.TwitchStreamAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.vimeo.VimeoAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;

import java.util.HashMap;

public class AudioManagerHandler {

    private static HashMap<String, GuildAudioManager> managers;
    private static AudioPlayerManager playerManager;

    /**
     * Handles full application runtime handlers
     * managers instead of leaving it to the main class.
     */
    public AudioManagerHandler() {
        managers = new HashMap<>();
        playerManager = new DefaultAudioPlayerManager();

        playerManager.registerSourceManager(new YoutubeAudioSourceManager());
        playerManager.registerSourceManager(new SoundCloudAudioSourceManager());
        playerManager.registerSourceManager(new BandcampAudioSourceManager());
        playerManager.registerSourceManager(new VimeoAudioSourceManager());
        playerManager.registerSourceManager(new TwitchStreamAudioSourceManager());
        playerManager.registerSourceManager(new HttpAudioSourceManager());
        playerManager.setFrameBufferDuration(400);
        playerManager.setTrackStuckThreshold(10000);

    }

    /**
     * Finds and sets the guild's handlers manager.
     * @return GuildAudioManager.
     */
    public static GuildAudioManager getGuildAudioManager(String id) {
        GuildAudioManager manager;

        if(managers.get(id) == null) {
            synchronized(AudioManagerHandler.getGuildAudioManagers()) {
                manager = new GuildAudioManager(playerManager);
                managers.put(id, manager);
            }
        } else {
            manager = managers.get(id);
        }
        return manager;
    }

    /**
     * Returns full MusicManager HashMap.
     * @return managers.
     */
    private static HashMap<String, GuildAudioManager> getGuildAudioManagers() {
        return managers;
    }

    /**
     * Adds to the GuildAudioManager HashMap.
     */
    public static void addGuildMusicManager(String guild, GuildAudioManager manager) {
        managers.put(guild, manager);
    }

    /**
     * Returns a guild's playerManager or null if there isn't one.
     * @return playerManager or NULL.
     */
    public static AudioPlayerManager getPlayerManager() {
        return playerManager;
    }
}
