package com.yuuko.core.metrics;

import java.util.concurrent.atomic.AtomicInteger;

public class EventMetrics {
    public final AtomicInteger MESSAGES_PROCESSED = new AtomicInteger();
    public final AtomicInteger REACTS_PROCESSED = new AtomicInteger();
    public final AtomicInteger COMMANDS_EXECUTED = new AtomicInteger();
    public final AtomicInteger COMMANDS_FAILED = new AtomicInteger();

    /**
     * Resets all event metrics to 0.
     */
    public void reset() {
        MESSAGES_PROCESSED.set(0);
        REACTS_PROCESSED.set(0);
        COMMANDS_EXECUTED.set(0);
        COMMANDS_FAILED.set(0);
    }
}


