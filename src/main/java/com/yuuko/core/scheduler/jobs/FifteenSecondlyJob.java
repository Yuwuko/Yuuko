package com.yuuko.core.scheduler.jobs;

import com.yuuko.core.scheduler.Job;
import com.yuuko.core.scheduler.tasks.UpdateMetricsTask;

import java.util.concurrent.TimeUnit;

public class FifteenSecondlyJob extends Job {
    private final UpdateMetricsTask updateMetricsTask = new UpdateMetricsTask();

    public FifteenSecondlyJob() {
        super(0, 15, TimeUnit.SECONDS);
    }

    @Override
    public void run() {
        handleTask(updateMetricsTask);
    }
}
