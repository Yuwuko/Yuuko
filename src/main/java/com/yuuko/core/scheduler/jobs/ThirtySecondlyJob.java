package com.yuuko.core.scheduler.jobs;

import com.yuuko.core.scheduler.Job;
import com.yuuko.core.scheduler.tasks.QueryDatabaseConnectionsTask;
import com.yuuko.core.scheduler.tasks.UpdateMetricsTask;

import java.util.concurrent.TimeUnit;

public class ThirtySecondlyJob extends Job {
    private final UpdateMetricsTask updateMetricsTask = new UpdateMetricsTask();
    private final QueryDatabaseConnectionsTask queryDatabaseConnectionsTask = new QueryDatabaseConnectionsTask();

    public ThirtySecondlyJob() {
        super(0, 30, TimeUnit.SECONDS);
    }

    @Override
    public void run() {
        handleTask(updateMetricsTask, queryDatabaseConnectionsTask);
    }
}
