package com.yuuko.core.scheduler.tasks;

import com.yuuko.core.Metrics;
import com.yuuko.core.scheduler.Task;

public class IncrementRuntimeTask implements Task {

    @Override
    public void handle() {
        Metrics.RUNTIME.getAndIncrement();
    }
}
