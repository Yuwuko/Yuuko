package com.yuuko.core.commands.audio.handlers;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.bandcamp.BandcampAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.http.HttpAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.soundcloud.SoundCloudAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.twitch.TwitchStreamAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.vimeo.VimeoAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;

import java.util.HashMap;

public class AudioManagerManager {

    private static HashMap<String, GuildAudioManager> managers;
    private static AudioPlayerManager playerManager;

    /**
     * Handles full application runtime handlers
     * managers instead of leaving it to the main class.
     */
    public AudioManagerManager() {
        managers = new HashMap<>();
        playerManager = new DefaultAudioPlayerManager();

        playerManager.registerSourceManager(new YoutubeAudioSourceManager());
        playerManager.registerSourceManager(new SoundCloudAudioSourceManager());
        playerManager.registerSourceManager(new BandcampAudioSourceManager());
        playerManager.registerSourceManager(new VimeoAudioSourceManager());
        playerManager.registerSourceManager(new TwitchStreamAudioSourceManager());
        playerManager.registerSourceManager(new HttpAudioSourceManager());
        playerManager.setTrackStuckThreshold(5000);
    }

    /**
     * Finds and sets the guild's handlers manager.
     * @return GuildAudioManager.
     */
    public static GuildAudioManager getGuildAudioManager(String guildId) {
        GuildAudioManager manager = managers.get(guildId);

        if(manager == null) {
            synchronized(AudioManagerManager.getGuildAudioManagers()) {
                manager = new GuildAudioManager(guildId);
                addGuildMusicManager(guildId, manager);
            }
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
    private static void addGuildMusicManager(String guild, GuildAudioManager manager) {
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
