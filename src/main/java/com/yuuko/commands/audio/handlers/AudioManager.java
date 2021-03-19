package com.yuuko.commands.audio.handlers;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.bandcamp.BandcampAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.http.HttpAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.soundcloud.SoundCloudAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.twitch.TwitchStreamAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.vimeo.VimeoAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.yuuko.commands.audio.handlers.lavalink.LavalinkManager;
import lavalink.client.io.Link;
import net.dv8tion.jda.api.entities.Guild;

import java.util.HashMap;
import java.util.Queue;

public final class AudioManager {
    public final static LavalinkManager LAVALINK = new LavalinkManager();
    private final static HashMap<Guild, GuildAudioManager> guildManagers = new HashMap<>();
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
     * @param guild {@link Guild}
     * @return {@link GuildAudioManager}
     */
    public static GuildAudioManager getGuildAudioManager(Guild guild) {
        GuildAudioManager manager = guildManagers.get(guild);

        if(manager == null) {
            synchronized(AudioManager.getGuildAudioManagers()) {
                manager = new GuildAudioManager(guild);
                addGuildAudioManager(guild, manager);
            }
        }

        return manager;
    }

    /**
     * Returns full MusicManager HashMap.
     * @return {@link HashMap<>} key {@link HashMap<>} value {@link GuildAudioManager}
     */
    public static HashMap<Guild, GuildAudioManager> getGuildAudioManagers() {
        return guildManagers;
    }

    /**
     * Adds to the GuildAudioManager HashMap.
     * @param guild {@link Guild}
     * @param manager {@link GuildAudioManager}
     */
    private static void addGuildAudioManager(Guild guild, GuildAudioManager manager) {
        guildManagers.put(guild, manager);
    }

    /**
     * Resets stuck GuildAudioManager by destroying and reinitialising it.
     * @param guild {@link Guild}
     * @param queue {@link Queue<AudioTrack>}
     */
    public static void resetStuckGuildAudioManager(Guild guild, Queue<AudioTrack> queue) {
        destroyGuildAudioManager(guild);
        getGuildAudioManager(guild).getScheduler().queue.addAll(queue);
    }

    /**
     * Removes/Destroys a guild audio manager, stopping players from just laying dormant and using resources.
     * @param guild {@link Guild}
     */
    public static void destroyGuildAudioManager(Guild guild) {
        if(guildManagers.containsKey(guild)) {
            guildManagers.get(guild).destroyConnection();
            guildManagers.remove(guild);
        }
    }

    /**
     * Returns a guild's playerManager or null if there isn't one.
     * @return playerManager or NULL.
     */
    public static AudioPlayerManager getPlayerManager() {
        return playerManager;
    }

    /**
     * Checks to see if Lavalink contains an existing link.
     * @param guild {@link Guild}
     * @return boolean
     */
    public static boolean hasLink(Guild guild) {
        return LAVALINK.getLavalink().getExistingLink(guild) != null;
    }

    /**
     * Returns a {@link Link} if it exists or null if it doesn't.
     * This method was chosen over {@link lavalink.client.io.Lavalink#getLink(String)} so we don't create links when
     * they aren't needed, which is the same motivation behind placing this method in {@link AudioManager}
     * instead of placing it in {@link GuildAudioManager} with the similar methods.
     *
     * @param guild {@link Guild}
     * @return {@link Link}
     */
    public static Link getExistingLink(Guild guild) {
        return LAVALINK.getLavalink().getExistingLink(guild);
    }

    /**
     * Checks if {@link Link} is connected or not.
     * @param guild {@link Guild}
     * @return boolean
     */
    public static boolean isLinkConnected(Guild guild) {
        return LAVALINK.getLavalink().getExistingLink(guild).getState() == Link.State.CONNECTED;
    }
}
