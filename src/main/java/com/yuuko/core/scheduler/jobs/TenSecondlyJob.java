package com.yuuko.core.scheduler.jobs;

import com.yuuko.core.scheduler.Job;
import com.yuuko.core.scheduler.tasks.RestartTask;
import com.yuuko.core.scheduler.tasks.ShutdownTask;
import com.yuuko.core.scheduler.tasks.UpdateMetricsTask;

import java.util.concurrent.TimeUnit;

public class TenSecondlyJob extends Job {
    private final UpdateMetricsTask updateMetricsTask = new UpdateMetricsTask();
    private final ShutdownTask shutdownTask = new ShutdownTask();
    private final RestartTask restartTask = new RestartTask();

    public TenSecondlyJob() {
        super(0, 10, TimeUnit.SECONDS);
    }

    @Override
    public void run() {
        handleTask(updateMetricsTask, shutdownTask, restartTask);
    }
}
