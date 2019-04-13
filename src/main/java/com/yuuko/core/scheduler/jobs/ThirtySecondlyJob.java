package com.yuuko.core.scheduler.jobs;

import com.yuuko.core.scheduler.Job;
import com.yuuko.core.scheduler.tasks.RenewShardTask;
import com.yuuko.core.scheduler.tasks.UpdateMetricsPingTask;
import com.yuuko.core.scheduler.tasks.UpdateShardStatisticsTask;

import java.util.concurrent.TimeUnit;

public class ThirtySecondlyJob extends Job {
    private final UpdateMetricsPingTask updateMetricsPingTask = new UpdateMetricsPingTask();
    private final RenewShardTask renewShardTask = new RenewShardTask();
    private final UpdateShardStatisticsTask updateShardStatisticsTask = new UpdateShardStatisticsTask();

    public ThirtySecondlyJob() {
        super(0, 30, TimeUnit.SECONDS);
    }

    @Override
    public void run() {
        handleTask(updateMetricsPingTask, renewShardTask, updateShardStatisticsTask);
    }
}
