package com.yuuko.core.commands.audio.handlers;

import com.yuuko.core.Configuration;
import lavalink.client.player.IPlayer;
import net.dv8tion.jda.api.entities.Guild;

public class GuildAudioManager {

    private final IPlayer player;
    private final TrackScheduler scheduler;

    /**
     * Creates a player and a track scheduler.
     * Without this class, trying to get persistent players, schedules, ect,
     * over an instanced module based bot would be pretty difficult.
     * @param guild the guild to create a player for.
     */
    public GuildAudioManager(Guild guild) {
        this.player = Configuration.LAVALINK.createPlayer(guild);
        this.scheduler = new TrackScheduler(guild, player);
        this.player.addListener(scheduler);
    }

    public IPlayer getPlayer() {
        return player;
    }

    public TrackScheduler getScheduler() {
        return scheduler;
    }
}
