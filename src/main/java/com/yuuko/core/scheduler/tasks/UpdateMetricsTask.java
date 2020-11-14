package com.yuuko.core.scheduler.tasks;

import com.yuuko.core.Config;
import com.yuuko.core.database.function.DatabaseFunctions;
import com.yuuko.core.metrics.MetricsManager;
import com.yuuko.core.scheduler.Task;

public class UpdateMetricsTask implements Task {
    private static long lastUpdated = 0;

    @Override
    public void run() {
        if(Config.LOG_METRICS) {
            if(System.currentTimeMillis() - lastUpdated > 29999) {
                lastUpdated = System.currentTimeMillis();
                MetricsManager.getDiscordMetrics().updatePing();
            }
            MetricsManager.getSystemMetrics().update();
            MetricsManager.getAudioMetrics().update();
            MetricsManager.getCacheMetrics().update();
            DatabaseFunctions.updateMetricsDatabase();
        }
    }
}
