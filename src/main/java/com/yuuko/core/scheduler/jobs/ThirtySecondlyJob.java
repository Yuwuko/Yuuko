package com.yuuko.core.scheduler.jobs;

import com.yuuko.core.scheduler.Job;
import com.yuuko.core.scheduler.tasks.UpdateShardTask;

import java.util.concurrent.TimeUnit;

public class ThirtySecondlyJob extends Job {
    private final UpdateShardTask updateShardTask = new UpdateShardTask();

    public ThirtySecondlyJob() {
        super(0, 30, TimeUnit.SECONDS);
    }

    @Override
    public void run() {
        handleTask(updateShardTask);
    }
}
