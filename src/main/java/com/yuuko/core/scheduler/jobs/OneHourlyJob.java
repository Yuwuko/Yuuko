package com.yuuko.core.scheduler.jobs;

import com.yuuko.core.scheduler.Job;
import com.yuuko.core.scheduler.tasks.PruneMetricsTask;

import java.util.concurrent.TimeUnit;

public class OneHourlyJob extends Job {
    private final PruneMetricsTask pruneMetricsTask = new PruneMetricsTask();

    public OneHourlyJob() {
        super(0, 1, TimeUnit.HOURS);
    }

    @Override
    public void run() {
        handleTask(pruneMetricsTask);
    }
}
