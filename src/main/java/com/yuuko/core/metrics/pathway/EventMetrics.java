package com.yuuko.core.metrics.pathway;

import java.util.concurrent.atomic.AtomicInteger;

public class EventMetrics {
    public final AtomicInteger BOT_MESSAGES_PROCESSED = new AtomicInteger();
    public final AtomicInteger HUMAN_MESSAGES_PROCESSED = new AtomicInteger();
    public final AtomicInteger BOT_REACTS_PROCESSED = new AtomicInteger();
    public final AtomicInteger HUMAN_REACTS_PROCESSED = new AtomicInteger();
    public final AtomicInteger OUTPUTS_PROCESSED = new AtomicInteger();
    public final AtomicInteger COMMANDS_EXECUTED = new AtomicInteger();
    public final AtomicInteger COMMANDS_FAILED = new AtomicInteger();

    /**
     * Resets all event metrics to 0.
     */
    public void reset() {
        BOT_MESSAGES_PROCESSED.set(0);
        HUMAN_MESSAGES_PROCESSED.set(0);
        BOT_REACTS_PROCESSED.set(0);
        HUMAN_REACTS_PROCESSED.set(0);
        OUTPUTS_PROCESSED.set(0);
        COMMANDS_EXECUTED.set(0);
        COMMANDS_FAILED.set(0);
    }
}


