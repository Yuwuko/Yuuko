package com.yuuko.core.metrics.pathway;

import com.yuuko.core.Configuration;
import net.dv8tion.jda.api.entities.Guild;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DiscordMetrics {
    public final AtomicInteger GUILD_COUNT = new AtomicInteger();
    public final AtomicInteger USER_COUNT = new AtomicInteger();
    public final AtomicInteger GATEWAY_PING = new AtomicInteger();
    public final AtomicInteger REST_PING = new AtomicInteger();

    /**
     * Updates the Discord Metrics to current values.
     */
    public void update() {
        List<Guild> guilds = Configuration.BOT.getJDA().getGuildCache().asList();

        int userCount = 0;
        for(Guild guild : guilds) {
            userCount += guild.getMemberCache().size();
        }

        USER_COUNT.set(userCount);
        GUILD_COUNT.set(guilds.size());

    }

    /**
     * Updates the bot's currently returned ping.
     */
    public void updatePing() {
        GATEWAY_PING.set((int)Configuration.BOT.getJDA().getGatewayPing());
        Configuration.BOT.getJDA().getRestPing().queue(s-> REST_PING.set(s.intValue()), f -> REST_PING.set(0));
    }
}
