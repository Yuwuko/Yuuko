package com.yuuko.core.metrics.pathway;

import java.util.concurrent.atomic.AtomicInteger;

public class EventMetrics {
    public final AtomicInteger BOT_MESSAGES_PROCESSED = new AtomicInteger();
    public final AtomicInteger HUMAN_MESSAGES_PROCESSED = new AtomicInteger();
    public final AtomicInteger BOT_REACTS_PROCESSED = new AtomicInteger();
    public final AtomicInteger HUMAN_REACTS_PROCESSED = new AtomicInteger();
    public final AtomicInteger OUTPUTS_PROCESSED = new AtomicInteger();
    public final AtomicInteger TOTAL_BOT_MESSAGES_PROCESSED = new AtomicInteger();
    public final AtomicInteger TOTAL_HUMAN_MESSAGES_PROCESSED = new AtomicInteger();
    public final AtomicInteger TOTAL_BOT_REACTS_PROCESSED = new AtomicInteger();
    public final AtomicInteger TOTAL_HUMAN_REACTS_PROCESSED = new AtomicInteger();
    public final AtomicInteger TOTAL_OUTPUTS_PROCESSED = new AtomicInteger();

    /**
     * Resets all event metrics to 0.
     */
    public void reset() {
        TOTAL_BOT_MESSAGES_PROCESSED.getAndAdd(BOT_MESSAGES_PROCESSED.get());
        TOTAL_HUMAN_MESSAGES_PROCESSED.getAndAdd(HUMAN_MESSAGES_PROCESSED.get());
        TOTAL_BOT_REACTS_PROCESSED.getAndAdd(BOT_REACTS_PROCESSED.get());
        TOTAL_HUMAN_REACTS_PROCESSED.getAndAdd(HUMAN_REACTS_PROCESSED.get());
        TOTAL_OUTPUTS_PROCESSED.getAndAdd(OUTPUTS_PROCESSED.get());
        BOT_MESSAGES_PROCESSED.set(0);
        HUMAN_MESSAGES_PROCESSED.set(0);
        BOT_REACTS_PROCESSED.set(0);
        HUMAN_REACTS_PROCESSED.set(0);
        OUTPUTS_PROCESSED.set(0);
    }
}


