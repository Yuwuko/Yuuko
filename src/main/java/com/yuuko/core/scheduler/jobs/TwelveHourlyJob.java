package com.yuuko.core.scheduler.jobs;

import com.yuuko.core.scheduler.Job;
import com.yuuko.core.scheduler.tasks.ClearCacheTask;

import java.util.concurrent.TimeUnit;

public class TwelveHourlyJob extends Job {
    private final ClearCacheTask clearCacheTask = new ClearCacheTask();

    public TwelveHourlyJob() {
        super(0, 12, TimeUnit.HOURS);
    }

    @Override
    public void run() {
        handleTask(clearCacheTask);
    }
}