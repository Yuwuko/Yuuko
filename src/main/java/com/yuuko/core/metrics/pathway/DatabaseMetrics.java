package com.yuuko.core.metrics.pathway;

import java.util.concurrent.atomic.AtomicInteger;

public class DatabaseMetrics {
    public final AtomicInteger SELECT = new AtomicInteger();
    public final AtomicInteger INSERT = new AtomicInteger();
    public final AtomicInteger UPDATE = new AtomicInteger();
    public final AtomicInteger DELETE = new AtomicInteger();
    public final AtomicInteger TOTAL_SELECTS = new AtomicInteger();
    public final AtomicInteger TOTAL_INSERTS = new AtomicInteger();
    public final AtomicInteger TOTAL_UPDATES = new AtomicInteger();
    public final AtomicInteger TOTAL_DELETES = new AtomicInteger();

    /**
     * Resets all database metrics to 0.
     */
    public void reset() {
        TOTAL_SELECTS.getAndAdd(SELECT.get());
        TOTAL_INSERTS.getAndAdd(INSERT.get());
        TOTAL_UPDATES.getAndAdd(UPDATE.get());
        TOTAL_DELETES.getAndAdd(DELETE.get());
        SELECT.set(0);
        INSERT.set(0);
        UPDATE.set(0);
        DELETE.set(0);
    }
}
