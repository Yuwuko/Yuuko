package com.yuuko.core.metrics;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class Metrics {
    public static long UPTIME = 0;
    public static long MEMORY_TOTAL = 0;
    public static long MEMORY_USED = 0;
    public static int GUILD_COUNT = 0;
    public static final AtomicLong PING = new AtomicLong();
    public static final AtomicInteger MESSAGES_PROCESSED = new AtomicInteger();
    public static final AtomicInteger REACTS_PROCESSED = new AtomicInteger();
    public static final AtomicInteger COMMANDS_SUCCESSFUL = new AtomicInteger();
    public static final AtomicInteger COMMANDS_UNSUCCESSFUL = new AtomicInteger();
    public static final AtomicInteger MEMBERS_JOINED = new AtomicInteger();
}
