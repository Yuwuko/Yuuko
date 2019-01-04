package com.yuuko.core.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public abstract class Job extends TimerTask {
    private static final Logger log = LoggerFactory.getLogger(Job.class);
    private final long delay;
    private final long period;
    private final TimeUnit unit;

    public Job(long delay, long period, TimeUnit unit) {
        this.delay = delay;
        this.period = period;
        this.unit = unit;
    }

    public long getDelay() {
        return delay;
    }

    public long getPeriod() {
        return period;
    }

    TimeUnit getUnit() {
        return unit;
    }

    protected void handleTask(Task... tasks) {
        for(Task task: tasks) {
            try {
                log.trace("Invoking {}#handle()", task.getClass().getName());
                task.handle();
            } catch(Exception ex) {
                log.error("An error occurred while running the {} class, message: {}", task.getClass().getSimpleName(), ex.getMessage(), ex);
            }
        }
    }

}
