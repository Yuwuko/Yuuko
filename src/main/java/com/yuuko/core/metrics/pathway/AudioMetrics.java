package com.yuuko.core.metrics.pathway;

import com.yuuko.core.commands.audio.handlers.AudioManagerController;
import com.yuuko.core.commands.audio.handlers.GuildAudioManager;
import lavalink.client.io.LavalinkSocket;
import lavalink.client.io.RemoteStats;

import java.util.concurrent.atomic.AtomicInteger;

import static com.yuuko.core.Configuration.LAVALINK;

public class AudioMetrics {
    public final AtomicInteger PLAYERS_TOTAL = new AtomicInteger();
    public final AtomicInteger PLAYERS_ACTIVE = new AtomicInteger();
    public final AtomicInteger QUEUE_SIZE = new AtomicInteger();

    /**
     * Updates all of the system metrics
     */
    public void update() {
        PLAYERS_TOTAL.set(0);
        PLAYERS_ACTIVE.set(0);
        QUEUE_SIZE.set(0);

        for(LavalinkSocket node: LAVALINK.getLavalink().getNodes()) {
            if(node.isOpen()) {
                RemoteStats stats = node.getStats();
                PLAYERS_TOTAL.getAndAdd(stats.getPlayers());
                PLAYERS_ACTIVE.getAndAdd(stats.getPlayingPlayers());
            }
        }

        for(GuildAudioManager manager: AudioManagerController.getGuildAudioManagers().values()) {
            QUEUE_SIZE.getAndAdd(manager.getScheduler().queue.size());
        }
    }
}
