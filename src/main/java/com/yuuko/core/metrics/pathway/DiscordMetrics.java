package com.yuuko.core.metrics.pathway;

import com.yuuko.core.Yuuko;
import com.yuuko.core.metrics.Metric;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DiscordMetrics implements Metric {
    public final int shardId;
    public final AtomicInteger GUILD_COUNT = new AtomicInteger();
    public final AtomicInteger GATEWAY_PING = new AtomicInteger();
    public final AtomicInteger REST_PING = new AtomicInteger();
    public final AtomicInteger MESSAGE_EVENTS = new AtomicInteger();

    public DiscordMetrics(int shardId) {
        this.shardId = shardId;
    }

    /**
     * Updates the Discord Metrics to current values.
     */
    public void update() {
        JDA jda = Yuuko.SHARD_MANAGER.getShardById(shardId);
        if(jda != null) {
            List<Guild> guilds = jda.getGuildCache().asList();
            GUILD_COUNT.set(guilds.size());
        }
    }

    /**
     * Update gateway and rest ping.
     */
    public void updatePing() {
        JDA jda = Yuuko.SHARD_MANAGER.getShardById(shardId);
        if(jda != null) {
            GATEWAY_PING.set((int) jda.getGatewayPing());
            jda.getRestPing().queue(s -> REST_PING.set(s.intValue()), f -> REST_PING.set(-1));
        }
    }
}
