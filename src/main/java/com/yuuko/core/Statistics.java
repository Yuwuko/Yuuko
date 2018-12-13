package com.yuuko.core;

import java.util.concurrent.atomic.AtomicInteger;

public class Statistics {
    public static int DB_POOL_ACTIVE;
    public static int DB_POOL_IDLE;
    public static int GUILD_COUNT;
    public static AtomicInteger MESSAGES_PROCESSED = new AtomicInteger();
    public static AtomicInteger REACTS_PROCESSED = new AtomicInteger();
    public static AtomicInteger COMMANDS_PROCESSED = new AtomicInteger();
    public static AtomicInteger MEMBERS_JOINED = new AtomicInteger();
}
