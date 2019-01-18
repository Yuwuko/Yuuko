package com.yuuko.core.scheduler.tasks;

import com.yuuko.core.database.DatabaseFunctions;
import com.yuuko.core.metrics.handlers.MetricsManager;
import com.yuuko.core.scheduler.Task;

public class UploadMetricsTask implements Task {

    @Override
    public void handle() {
        DatabaseFunctions.updateMetricsDatabase();
        MetricsManager.reset();
        //System.out.println("Main: " + SettingsDatabaseConnection.getLockedConnections() + ", Metrics: " + MetricsDatabaseConnection.getLockedConnections());
    }

}
