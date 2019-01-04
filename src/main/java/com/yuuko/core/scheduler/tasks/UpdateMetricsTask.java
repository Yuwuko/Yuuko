package com.yuuko.core.scheduler.tasks;

import com.yuuko.core.metrics.Metrics;
import com.yuuko.core.scheduler.Task;

import java.lang.management.ManagementFactory;

public class UpdateMetricsTask implements Task {

    @Override
    public void handle() {
        Metrics.UPTIME = ManagementFactory.getRuntimeMXBean().getUptime();
        Metrics.MEMORY_TOTAL = Runtime.getRuntime().totalMemory();
        Metrics.MEMORY_USED = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
    }
}
