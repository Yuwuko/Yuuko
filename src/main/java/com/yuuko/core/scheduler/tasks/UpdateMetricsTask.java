package com.yuuko.core.scheduler.tasks;

import com.yuuko.core.Configuration;
import com.yuuko.core.database.function.DatabaseFunctions;
import com.yuuko.core.metrics.handlers.MetricsManager;
import com.yuuko.core.scheduler.Task;

public class UpdateMetricsTask implements Task {
    private static long lastUpdated = 0;

    @Override
    public void run() {
        if(System.currentTimeMillis() - lastUpdated > 29999) {
            MetricsManager.getDiscordMetrics().PING.set(Configuration.BOT.getJDA().getPing());
            lastUpdated = System.currentTimeMillis();
        }
        MetricsManager.getSystemMetrics().update();
        DatabaseFunctions.updateMetricsDatabase();
        MetricsManager.reset();
    }
}
