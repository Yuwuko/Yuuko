package com.yuuko.scheduler.jobs;

import com.yuuko.scheduler.Job;
import com.yuuko.scheduler.tasks.ClearCacheTask;

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