package com.yuuko.core.metrics;

import java.util.concurrent.atomic.AtomicLong;

public class DiscordMetrics {
    public int GUILD_COUNT = 0;
    public int CHANNEL_COUNT = 0;
    public int USER_COUNT = 0;
    public int EMOTE_COUNT = 0;
    public int ROLE_COUNT = 0;
    public final AtomicLong PING = new AtomicLong();
}
