package com.yuuko.core.commands.audio.handlers;

import com.yuuko.core.Cache;
import lavalink.client.player.IPlayer;

public class GuildAudioManager {

    public final IPlayer player;
    public final TrackScheduler scheduler;

    /**
     * Creates a player and a track scheduler.
     * Without this class, trying to get persistent players, schedules, ect,
     * over an instanced module based bot would be pretty difficult.
     * @param guildId Id of the guild to create a player for.
     */
    public GuildAudioManager(String guildId) {
        player = Cache.LAVALINK.createPlayer(guildId);
        scheduler = new TrackScheduler(player);
        player.addListener(scheduler);
    }

}
