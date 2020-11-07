package com.yuuko.core.commands.audio.handlers;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.bandcamp.BandcampAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.http.HttpAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.soundcloud.SoundCloudAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.twitch.TwitchStreamAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.vimeo.VimeoAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.yuuko.core.Configuration;
import lavalink.client.io.Link;
import net.dv8tion.jda.api.entities.Guild;

import java.util.HashMap;

public final class AudioManagerController {
    private final static HashMap<Guild, GuildAudioManager> managers = new HashMap<>();
    private final static AudioPlayerManager playerManager = new DefaultAudioPlayerManager();

    /**
     * Register source managers
     */
    public static void registerSourceManagers() {
        playerManager.registerSourceManager(new YoutubeAudioSourceManager());
        playerManager.registerSourceManager(SoundCloudAudioSourceManager.createDefault());
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
    public static GuildAudioManager getGuildAudioManager(Guild guild) {
        GuildAudioManager manager = managers.get(guild);

        if(manager == null) {
            synchronized(AudioManagerController.getGuildAudioManagers()) {
                manager = new GuildAudioManager(guild);
                addGuildAudioManager(guild, manager);
            }
        }

        return manager;
    }

    /**
     * Returns full MusicManager HashMap.
     * @return managers.
     */
    public static HashMap<Guild, GuildAudioManager> getGuildAudioManagers() {
        return managers;
    }

    /**
     * Adds to the GuildAudioManager HashMap.
     */
    private static void addGuildAudioManager(Guild guild, GuildAudioManager manager) {
        managers.put(guild, manager);
    }

    /**
     * Removes a guild audio manager, stopping players from just laying dormant.
     * @param guild which manager to remove.
     */
    public static void removeGuildAudioManager(Guild guild) {
        managers.remove(guild);
    }

    /**
     * Returns a guild's playerManager or null if there isn't one.
     * @return playerManager or NULL.
     */
    public static AudioPlayerManager getPlayerManager() {
        return playerManager;
    }

    /**
     * Returns a {@link Link} if it exists or null if it doesn't.
     * This method was chosen over {@link lavalink.client.io.Lavalink#getLink(String)} so we don't create links when
     * they aren't needed, which is the same motivation behind placing this method in {@link AudioManagerController}
     * instead of placing it in {@link GuildAudioManager} with the similar methods.
     *
     * @param guild {@link Guild}
     * @return {@link Link}
     */
    public static Link getExistingLink(Guild guild) {
        return Configuration.LAVALINK.getLavalink().getExistingLink(guild.getId());
    }
}
