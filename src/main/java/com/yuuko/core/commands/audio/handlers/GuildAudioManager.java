package com.yuuko.core.commands.audio.handlers;

import com.yuuko.core.Configuration;
import lavalink.client.io.jda.JdaLink;
import lavalink.client.player.LavalinkPlayer;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class GuildAudioManager {
    private final Guild guild;
    private final JdaLink link;
    private final LavalinkPlayer player;
    private final TrackScheduler scheduler;

    /**
     * Creates a player and a track scheduler.
     * Without this class, trying to get persistent players, schedules, ect,
     * over an instanced module based bot would be pretty difficult.
     * @param guild the guild to create a player for.
     */
    public GuildAudioManager(Guild guild) {
        this.guild = guild;
        this.link = Configuration.LAVALINK.getLavalink().getLink(guild);
        this.player = link.getPlayer();
        this.scheduler = new TrackScheduler(guild, player);
        this.player.addListener(scheduler);
    }

    public JdaLink getLink() {
        return link;
    }

    public LavalinkPlayer getPlayer() {
        return player;
    }

    public void resetPlayer(Guild guild) {
        link.resetPlayer();
    }

    public void openConnection(VoiceChannel channel) {
        link.connect(channel);
    }

    public void closeConnection(Guild guild) {
        link.disconnect();
    }

    public void destroy() {
        link.destroy();
        AudioManagerController.removeGuildAudioManager(guild);
    }

    public TrackScheduler getScheduler() {
        return scheduler;
    }
}
