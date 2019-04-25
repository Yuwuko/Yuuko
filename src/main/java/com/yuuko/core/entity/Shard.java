package com.yuuko.core.entity;

public class Shard {
    private final int id;
    private final String status;
    private final int guildCount;
    private final int userCount;
    private final int channelCount;
    private final int ping;

    public Shard(int id, String status, int guilds, int users, int channels, int ping) {
        this.id = id;
        this.status = status;
        this.guildCount = guilds;
        this.userCount = users;
        this.channelCount = channels;
        this.ping = ping;
    }

    public int getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public int getGuildCount() {
        return guildCount;
    }

    public int getUserCount() {
        return userCount;
    }

    public int getChannelCount() {
        return channelCount;
    }

    public int getPing() {
        return ping;
    }
}
