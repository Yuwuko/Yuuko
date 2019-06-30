package com.yuuko.core.metrics.pathway;

import java.util.concurrent.atomic.AtomicInteger;

public class EventMetrics {
    public final AtomicInteger GUILD_JOIN_EVENT = new AtomicInteger();
    public final AtomicInteger GUILD_LEAVE_EVENT = new AtomicInteger();
    public final AtomicInteger GUILD_MEMBER_JOIN_EVENT = new AtomicInteger();
    public final AtomicInteger GUILD_MEMBER_LEAVE_EVENT = new AtomicInteger();
    public final AtomicInteger GUILD_UPDATE_NAME_EVENT = new AtomicInteger();
    public final AtomicInteger GUILD_UPDATE_REGION_EVENT = new AtomicInteger();
    public final AtomicInteger GUILD_UPDATE_ICON_EVENT = new AtomicInteger();
    public final AtomicInteger GUILD_UPDATE_SPLASH_EVENT = new AtomicInteger();
    public final AtomicInteger GUILD_MESSAGE_RECEIVED_EVENT = new AtomicInteger();
    public final AtomicInteger GUILD_MESSAGE_DELETE_EVENT = new AtomicInteger();
    public final AtomicInteger GUILD_MESSAGE_REACTION_ADD_EVENT = new AtomicInteger();
    public final AtomicInteger GUILD_MESSAGE_REACTION_REMOVE_EVENT = new AtomicInteger();
}


