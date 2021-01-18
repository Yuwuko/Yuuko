package com.yuuko.core.scheduler.jobs;

import com.yuuko.core.scheduler.Job;
import com.yuuko.core.scheduler.tasks.PruneAbandonedEventsTask;
import com.yuuko.core.scheduler.tasks.PruneCooldownsTask;
import com.yuuko.core.scheduler.tasks.PruneMetricsTask;

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
