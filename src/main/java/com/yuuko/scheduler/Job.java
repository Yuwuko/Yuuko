package com.yuuko.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public abstract class Job extends TimerTask {
    private static final Logger log = LoggerFactory.getLogger(Job.class);
    private final long delay;
    private final long period;
    private final TimeUnit unit;

    protected Job(long delay, long period, TimeUnit unit) {
        this.delay = delay;
        this.period = period;
        this.unit = unit;
    }

    long getDelay() {
        return delay;
    }

    long getPeriod() {
        return period;
    }

    TimeUnit getUnit() {
        return unit;
    }

    protected void handleTask(Task... tasks) {
        for(Task task: tasks) {
            try {
                log.trace("Invoking {}#handle()", task.getClass().getName());
                task.run();
            } catch(Exception ex) {
                log.error("An error occurred while running the {} class, message: {}", task.getClass().getSimpleName(), ex.getMessage(), ex);
            }
        }
    }

}
