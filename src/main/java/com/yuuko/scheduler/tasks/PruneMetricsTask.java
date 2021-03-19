package com.yuuko.scheduler.tasks;

import com.yuuko.metrics.MetricsManager;
import com.yuuko.scheduler.Task;

public class PruneMetricsTask implements Task {

    @Override
    public void run() {
        MetricsManager.DatabaseInterface.pruneMetrics();
    }
}
