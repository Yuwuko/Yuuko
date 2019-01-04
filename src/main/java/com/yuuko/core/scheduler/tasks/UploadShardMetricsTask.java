package com.yuuko.core.scheduler.tasks;

import com.yuuko.core.database.DatabaseFunctions;
import com.yuuko.core.scheduler.Task;

public class UploadShardMetricsTask implements Task {

    @Override
    public void handle() {
        new DatabaseFunctions().updateShardMetrics();
    }

}
