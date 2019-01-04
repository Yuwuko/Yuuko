package com.yuuko.core.scheduler.jobs;

import com.yuuko.core.scheduler.Job;
import com.yuuko.core.scheduler.tasks.UpdateMetricsPingTask;

import java.util.concurrent.TimeUnit;

public class ThirtySecondlyJob extends Job {
    private final UpdateMetricsPingTask updateMetricsPingTask = new UpdateMetricsPingTask();

    public ThirtySecondlyJob() {
        super(0, 30, TimeUnit.SECONDS);
    }

    @Override
    public void run() {
        handleTask(updateMetricsPingTask);
    }
}
