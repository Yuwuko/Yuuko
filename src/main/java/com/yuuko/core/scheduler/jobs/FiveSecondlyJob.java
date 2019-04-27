package com.yuuko.core.scheduler.jobs;

import com.yuuko.core.scheduler.Job;
import com.yuuko.core.scheduler.tasks.UpdateMetricsTask;

import java.util.concurrent.TimeUnit;

public class FiveSecondlyJob extends Job {
    private final UpdateMetricsTask updateMetricsTask = new UpdateMetricsTask();

    public FiveSecondlyJob() {
        super(0, 5, TimeUnit.SECONDS);
    }

    @Override
    public void run() {
        handleTask(updateMetricsTask);
    }
}
