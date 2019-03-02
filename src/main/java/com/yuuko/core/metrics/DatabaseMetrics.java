package com.yuuko.core.metrics;

import java.util.concurrent.atomic.AtomicInteger;

public class DatabaseMetrics {
    public final AtomicInteger SELECT = new AtomicInteger();
    public final AtomicInteger INSERT = new AtomicInteger();
    public final AtomicInteger UPDATE = new AtomicInteger();
    public final AtomicInteger DELETE = new AtomicInteger();

    /**
     * Resets all database metrics to 0.
     */
    public void reset() {
        INSERT.set(0);
        SELECT.set(0);
        DELETE.set(0);
        UPDATE.set(0);
    }
}
