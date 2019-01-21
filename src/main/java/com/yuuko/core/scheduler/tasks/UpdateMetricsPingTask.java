package com.yuuko.core.scheduler.tasks;

import com.yuuko.core.Cache;
import com.yuuko.core.metrics.handlers.MetricsManager;
import com.yuuko.core.scheduler.Task;

public class UpdateMetricsPingTask implements Task {

    @Override
    public void handle() {
        MetricsManager.getDiscordMetrics().PING.set(Cache.BOT.getJDA().getPing());
    }
}
