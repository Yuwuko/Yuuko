package com.yuuko.scheduler.jobs;

import com.yuuko.scheduler.Job;
import com.yuuko.scheduler.tasks.PruneAbandonedEventsTask;
import com.yuuko.scheduler.tasks.PruneCooldownsTask;
import com.yuuko.scheduler.tasks.PruneMetricsTask;

import java.util.concurrent.TimeUnit;

public class OneHourlyJob extends Job {
    private final PruneMetricsTask pruneMetricsTask = new PruneMetricsTask();
    private final PruneCooldownsTask pruneCooldownsTask = new PruneCooldownsTask();
    private final PruneAbandonedEventsTask pruneAbandonedEvents = new PruneAbandonedEventsTask();

    public OneHourlyJob() {
        super(0, 1, TimeUnit.HOURS);
    }

    @Override
    public void run() {
        handleTask(pruneMetricsTask, pruneCooldownsTask, pruneAbandonedEvents);
    }
}
