package com.yuuko.core.metrics;

import java.util.concurrent.atomic.AtomicInteger;

public class EventMetrics {
    public final AtomicInteger MESSAGES_PROCESSED = new AtomicInteger();
    public final AtomicInteger REACTS_PROCESSED = new AtomicInteger();
    public final AtomicInteger COMMANDS_EXECUTED = new AtomicInteger();
    public final AtomicInteger COMMANDS_FAILED = new AtomicInteger();
}


