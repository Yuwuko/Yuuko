package com.yuuko.core.scheduler.tasks;

import com.yuuko.core.metrics.MetricsManager;
import com.yuuko.core.scheduler.Task;

public class PruneMetricsTask implements Task {

    @Override
    public void run() {
        MetricsManager.pruneMetrics();
    }
}
