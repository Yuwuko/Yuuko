package com.yuuko.core.metrics.pathway;

import java.util.concurrent.atomic.AtomicInteger;

public class EventMetrics {
    public final AtomicInteger BOT_MESSAGES = new AtomicInteger();
    public final AtomicInteger HUMAN_MESSAGES = new AtomicInteger();
    public final AtomicInteger BOT_REACTS = new AtomicInteger();
    public final AtomicInteger HUMAN_REACTS = new AtomicInteger();
    public final AtomicInteger OUTPUTS = new AtomicInteger();
    public final AtomicInteger TOTAL_BOT_MESSAGES = new AtomicInteger();
    public final AtomicInteger TOTAL_HUMAN_MESSAGES = new AtomicInteger();
    public final AtomicInteger TOTAL_BOT_REACTS = new AtomicInteger();
    public final AtomicInteger TOTAL_HUMAN_REACTS = new AtomicInteger();
    public final AtomicInteger TOTAL_OUTPUTS = new AtomicInteger();

    /**
     * Resets all event metrics to 0.
     */
    public void reset() {
        TOTAL_BOT_MESSAGES.getAndAdd(BOT_MESSAGES.get());
        TOTAL_HUMAN_MESSAGES.getAndAdd(HUMAN_MESSAGES.get());
        TOTAL_BOT_REACTS.getAndAdd(BOT_REACTS.get());
        TOTAL_HUMAN_REACTS.getAndAdd(HUMAN_REACTS.get());
        TOTAL_OUTPUTS.getAndAdd(OUTPUTS.get());
        BOT_MESSAGES.set(0);
        HUMAN_MESSAGES.set(0);
        BOT_REACTS.set(0);
        HUMAN_REACTS.set(0);
        OUTPUTS.set(0);
    }
}


