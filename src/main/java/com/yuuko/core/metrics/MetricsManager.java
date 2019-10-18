package com.yuuko.core.metrics;

import com.yuuko.core.database.function.DatabaseFunctions;
import com.yuuko.core.metrics.pathway.*;

public class MetricsManager {
    private static final SystemMetrics systemMetrics = new SystemMetrics();
    private static final EventMetrics eventMetrics = new EventMetrics();
    private static final DiscordMetrics discordMetrics = new DiscordMetrics();
    private static final AudioMetrics audioMetrics = new AudioMetrics();
    private static final CacheMetrics cacheMetrics = new CacheMetrics();

    public static void pruneMetrics() {
        DatabaseFunctions.pruneMetrics();
    }

    public static void truncateMetrics(int shard) {
        DatabaseFunctions.truncateMetrics(shard);
    }

    public static AudioMetrics getAudioMetrics() { return audioMetrics; }

    public static DiscordMetrics getDiscordMetrics() {
        return discordMetrics;
    }

    public static EventMetrics getEventMetrics() {
        return eventMetrics;
    }

    public static SystemMetrics getSystemMetrics() {
        return systemMetrics;
    }

    public static CacheMetrics getCacheMetrics() {
        return cacheMetrics;
    }

}
