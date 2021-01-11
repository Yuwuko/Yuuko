package com.yuuko.core.scheduler.tasks;

import com.yuuko.core.Config;
import com.yuuko.core.metrics.MetricsManager;
import com.yuuko.core.scheduler.Task;

public class UpdateMetricsTask implements Task {
    private static long lastUpdated = 0;

    @Override
    public void run() {
        if(Config.LOG_METRICS) {
            if(System.currentTimeMillis() - lastUpdated > 29999) {
                lastUpdated = System.currentTimeMillis();
                Config.SHARD_MANAGER.getShards().stream().filter(shard -> shard.getStatus().name().equals("CONNECTED")).forEach(shard -> {
                    MetricsManager.getDiscordMetrics(shard.getShardInfo().getShardId()).updatePing();
                });
            }
            MetricsManager.getSystemMetrics().update();
            MetricsManager.getAudioMetrics().update();
            MetricsManager.DatabaseInterface.updateMetrics();
        }
    }
}
