package com.yuuko.core.metrics;

import com.yuuko.core.database.function.MetricsFunctions;
import com.yuuko.core.metrics.pathway.AudioMetrics;
import com.yuuko.core.metrics.pathway.CacheMetrics;
import com.yuuko.core.metrics.pathway.DiscordMetrics;
import com.yuuko.core.metrics.pathway.SystemMetrics;

import java.util.ArrayList;
import java.util.List;

public class MetricsManager {
    private static final SystemMetrics systemMetrics = new SystemMetrics();
    private static final List<DiscordMetrics> shardedDiscordMetrics = new ArrayList<>();
    private static final AudioMetrics audioMetrics = new AudioMetrics();
    private static final CacheMetrics cacheMetrics = new CacheMetrics();

    public MetricsManager(int shards) {
        for(int i = 0; i < shards; i++) {
            shardedDiscordMetrics.add(new DiscordMetrics(i));
        }
    }

    public static void pruneMetrics() {
        MetricsFunctions.pruneMetrics();
    }

    public static AudioMetrics getAudioMetrics() { return audioMetrics; }

    public static DiscordMetrics getDiscordMetrics(int shardId) {
        return shardedDiscordMetrics.get(shardId);
    }

    public static List<DiscordMetrics> getShardedDiscordMetrics() {
        return shardedDiscordMetrics;
    }

    public static SystemMetrics getSystemMetrics() {
        return systemMetrics;
    }

    public static CacheMetrics getCacheMetrics() {
        return cacheMetrics;
    }
}
