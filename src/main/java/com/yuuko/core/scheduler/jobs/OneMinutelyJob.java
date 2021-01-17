package com.yuuko.core.scheduler.jobs;

import com.yuuko.core.scheduler.Job;
import com.yuuko.core.scheduler.tasks.EventNotificationTask;

import java.util.concurrent.TimeUnit;

public class OneMinutelyJob extends Job {
    private final EventNotificationTask eventNotificationTask = new EventNotificationTask();

    public OneMinutelyJob() {
        super(0, 1, TimeUnit.MINUTES);
    }

    @Override
    public void run() {
        handleTask(eventNotificationTask);
    }
}
