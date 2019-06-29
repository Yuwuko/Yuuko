package com.yuuko.core.scheduler.jobs;

import com.yuuko.core.Configuration;
import com.yuuko.core.scheduler.Job;
import com.yuuko.core.scheduler.tasks.UpdateMetricsTask;

import java.util.concurrent.TimeUnit;

public class TenSecondlyJob extends Job {
    private final UpdateMetricsTask updateMetricsTask = new UpdateMetricsTask();

    public TenSecondlyJob() {
        super(0, 10, TimeUnit.SECONDS);
    }

    @Override
    public void run() {
        if(Configuration.LOG_METRICS) {
            handleTask(updateMetricsTask);
        }
    }
}
