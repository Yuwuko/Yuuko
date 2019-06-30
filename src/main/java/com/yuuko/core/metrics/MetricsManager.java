package com.yuuko.core.metrics;

import com.yuuko.core.database.function.DatabaseFunctions;
import com.yuuko.core.metrics.pathway.DiscordMetrics;
import com.yuuko.core.metrics.pathway.EventMetrics;
import com.yuuko.core.metrics.pathway.SystemMetrics;

public class MetricsManager {
    private static final SystemMetrics systemMetrics = new SystemMetrics();
    private static final EventMetrics eventMetrics = new EventMetrics();
    private static final DiscordMetrics discordMetrics = new DiscordMetrics();

    public static void pruneMetrics() {
        DatabaseFunctions.pruneMetrics();
    }

    public static void truncateMetrics(int shard) {
        DatabaseFunctions.truncateMetrics(shard);
    }

    public static DiscordMetrics getDiscordMetrics() {
        return discordMetrics;
    }

    public static EventMetrics getEventMetrics() {
        return eventMetrics;
    }

    public static SystemMetrics getSystemMetrics() {
        return systemMetrics;
    }

}
