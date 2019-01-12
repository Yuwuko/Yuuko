package com.yuuko.core.scheduler.tasks;

import com.yuuko.core.metrics.handlers.MetricsManager;
import com.yuuko.core.scheduler.Task;

import java.lang.management.ManagementFactory;

public class UpdateMetricsTask implements Task {

    @Override
    public void handle() {
        MetricsManager.getSystemMetrics().UPTIME = ManagementFactory.getRuntimeMXBean().getUptime();
        MetricsManager.getSystemMetrics().MEMORY_TOTAL = Runtime.getRuntime().totalMemory();
        MetricsManager.getSystemMetrics().MEMORY_USED = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
    }
}
