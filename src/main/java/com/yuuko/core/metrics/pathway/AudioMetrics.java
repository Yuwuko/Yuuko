package com.yuuko.core.metrics.pathway;

import com.yuuko.core.commands.audio.handlers.AudioManager;
import com.yuuko.core.commands.audio.handlers.GuildAudioManager;
import com.yuuko.core.commands.audio.handlers.YouTubeSearchHandler;
import com.yuuko.core.metrics.Metric;
import lavalink.client.io.LavalinkSocket;
import lavalink.client.io.RemoteStats;

import java.util.concurrent.atomic.AtomicInteger;

public class AudioMetrics implements Metric {
    public final AtomicInteger PLAYERS_TOTAL = new AtomicInteger();
    public final AtomicInteger PLAYERS_ACTIVE = new AtomicInteger();
    public final AtomicInteger QUEUE_SIZE = new AtomicInteger();
    public final AtomicInteger TRACK_ID_CACHE_HITS = new AtomicInteger();
    public final AtomicInteger TRACK_ID_CACHE_SIZE = new AtomicInteger();

    /**
     * Updates all of the system metrics
     */
    public void update() {
        PLAYERS_TOTAL.set(0);
        PLAYERS_ACTIVE.set(0);
        QUEUE_SIZE.set(0);
        TRACK_ID_CACHE_SIZE.set(0);

        for(LavalinkSocket node: AudioManager.LAVALINK.getLavalink().getNodes()) {
            if(node.isOpen()) {
                RemoteStats stats = node.getStats();
                if(stats != null) {
                    PLAYERS_TOTAL.getAndAdd(stats.getPlayers());
                    PLAYERS_ACTIVE.getAndAdd(stats.getPlayingPlayers());
                }
            }
        }

        for(GuildAudioManager manager: AudioManager.getGuildAudioManagers().values()) {
            QUEUE_SIZE.getAndAdd(manager.getScheduler().queue.size());
        }

        TRACK_ID_CACHE_SIZE.set(YouTubeSearchHandler.getSearchCache().size());
    }
}
