package com.yuuko.core;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class Statistics {
    public static final AtomicInteger RUNTIME = new AtomicInteger();
    public static final AtomicLong PING = new AtomicLong();
    public static int DB_POOL_ACTIVE;
    public static int DB_POOL_IDLE;
    public static int GUILD_COUNT;
    public static final AtomicInteger MESSAGES_PROCESSED = new AtomicInteger();
    public static final AtomicInteger REACTS_PROCESSED = new AtomicInteger();
    public static final AtomicInteger COMMANDS_PROCESSED = new AtomicInteger();
    public static final AtomicInteger MEMBERS_JOINED = new AtomicInteger();
}
