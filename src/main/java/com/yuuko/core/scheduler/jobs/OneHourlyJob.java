package com.yuuko.core.scheduler.jobs;

import com.yuuko.core.scheduler.Job;
import com.yuuko.core.scheduler.tasks.TruncateDatabaseTask;

import java.util.concurrent.TimeUnit;

public class OneHourlyJob extends Job {
    private final TruncateDatabaseTask truncateDatabaseTask = new TruncateDatabaseTask();

    public OneHourlyJob() {
        super(0, 1, TimeUnit.HOURS);
    }

    @Override
    public void run() {
        handleTask(truncateDatabaseTask);
    }
}
