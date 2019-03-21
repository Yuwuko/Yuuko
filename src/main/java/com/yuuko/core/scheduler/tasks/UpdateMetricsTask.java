package com.yuuko.core.scheduler.tasks;

import com.yuuko.core.metrics.handlers.MetricsManager;
import com.yuuko.core.scheduler.Task;

public class UpdateMetricsTask implements Task {

    @Override
    public void handle() {
        MetricsManager.getSystemMetrics().update();
    }
}
