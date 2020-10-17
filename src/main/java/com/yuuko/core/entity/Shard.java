package com.yuuko.core.entity;

public class Shard {
    private final int id;
    private final String status;
    private final int guildCount;
    private final int ping;

    public Shard(int id, String status, int guilds, int ping) {
        this.id = id;
        this.status = status;
        this.guildCount = guilds;
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

    public int getPing() {
        return ping;
    }
}
