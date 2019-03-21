package com.yuuko.core.metrics.handlers;

import com.yuuko.core.database.DatabaseFunctions;
import com.yuuko.core.metrics.DatabaseMetrics;
import com.yuuko.core.metrics.DiscordMetrics;
import com.yuuko.core.metrics.EventMetrics;
import com.yuuko.core.metrics.SystemMetrics;

public class MetricsManager {

    private static final SystemMetrics systemMetrics = new SystemMetrics();
    private static final EventMetrics eventMetrics = new EventMetrics();
    private static final DatabaseMetrics databaseMetrics = new DatabaseMetrics();
    private static final DiscordMetrics discordMetrics = new DiscordMetrics();

    public static void reset() {
        eventMetrics.reset();
        databaseMetrics.reset();
    }

    public static void truncate() {
        DatabaseFunctions.truncateDatabase();
    }

    public static DatabaseMetrics getDatabaseMetrics() {
        return databaseMetrics;
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
