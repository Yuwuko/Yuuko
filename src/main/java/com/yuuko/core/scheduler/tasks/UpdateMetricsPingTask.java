package com.yuuko.core.scheduler.tasks;

import com.yuuko.core.Configuration;
import com.yuuko.core.metrics.handlers.MetricsManager;
import com.yuuko.core.scheduler.Task;

public class UpdateMetricsPingTask implements Task {

    @Override
    public void handle() {
        MetricsManager.getDiscordMetrics().PING.set(Configuration.BOT.getJDA().getPing());
    }
}
