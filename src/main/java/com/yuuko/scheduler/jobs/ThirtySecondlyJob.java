package com.yuuko.scheduler.jobs;

import com.yuuko.scheduler.Job;
import com.yuuko.scheduler.tasks.UpdateShardTask;

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
