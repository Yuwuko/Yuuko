package com.yuuko.modules.audio.handlers;

import lavalink.client.io.jda.JdaLink;
import lavalink.client.player.LavalinkPlayer;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class GuildAudioManager {
    private final JdaLink link;
    private final LavalinkPlayer player;
    private final TrackScheduler scheduler;

    /**
     * Creates a {@link LavalinkPlayer} and a {@link TrackScheduler}.
     * Without this class, trying to get persistent players, schedules
     * over an instanced module based bot would be pretty difficult.
     * @param guild {@link Guild}
     */
    public GuildAudioManager(Guild guild) {
        this.link = AudioManager.LAVALINK.getLavalink().getLink(guild);
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

    protected void destroyConnection() {
        scheduler.queue.clear();
        player.stopTrack();
        player.removeListener(scheduler);
        link.resetPlayer();
        link.destroy();
    }

    public TrackScheduler getScheduler() {
        return scheduler;
    }
}
