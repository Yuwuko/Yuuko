package com.yuuko.core.scheduler.jobs;

import com.yuuko.core.scheduler.Job;
import com.yuuko.core.scheduler.tasks.IncrementRuntimeTask;
import com.yuuko.core.scheduler.tasks.UpdateServerStatusTask;

import java.util.concurrent.TimeUnit;

public class SecondlyJob extends Job {
    private final IncrementRuntimeTask incrementRuntimeTask = new IncrementRuntimeTask();
    private final UpdateServerStatusTask updateServerStatusTask = new UpdateServerStatusTask();

    public SecondlyJob() {
        super(0, 1, TimeUnit.SECONDS);
    }

    @Override
    public void run() {
        handleTask(incrementRuntimeTask, updateServerStatusTask);
    }
}
