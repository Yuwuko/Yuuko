package com.yuuko.core.entity;

public class Shard {
    private final int id;
    private final String status;
    private final int guildCount;
    private final int gatewayPing;
    private final int restPing;

    public Shard(int id, String status, int guilds, int gatewayPing, int restPing) {
        this.id = id;
        this.status = status;
        this.guildCount = guilds;
        this.gatewayPing = gatewayPing;
        this.restPing = restPing;
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

    public int getGatewayPing() {
        return gatewayPing;
    }

    public int getRestPing() {
        return restPing;
    }
}
