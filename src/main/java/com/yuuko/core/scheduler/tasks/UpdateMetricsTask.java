package com.yuuko.core.scheduler.tasks;

import com.yuuko.core.Cache;
import com.yuuko.core.Metrics;
import com.yuuko.core.scheduler.Task;

public class UpdateMetricsTask implements Task {

    @Override
    public void handle() {
        Metrics.PING.set(Cache.JDA.getPing());
    }
}
