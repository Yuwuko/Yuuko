package com.yuuko.core.metrics.pathway;

import com.yuuko.core.Configuration;
import net.dv8tion.jda.api.entities.Guild;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class DiscordMetrics {
    public int GUILD_COUNT = 0;
    public int USER_COUNT = 0;
    public final AtomicLong GATEWAY_PING = new AtomicLong();
    public final AtomicLong REST_PING = new AtomicLong();

    /**
     * Updates the Discord Metrics to current values.
     */
    public void update() {
        List<Guild> guilds = Configuration.BOT.getJDA().getGuildCache().asList();

        int userCount = 0;
        for(Guild guild : guilds) {
            userCount += guild.getMemberCache().size();
        }

        USER_COUNT = userCount;
        GUILD_COUNT = guilds.size();
    }

    /**
     * Updates the bot's currently returned ping.
     */
    public void updatePing() {
        GATEWAY_PING.set(Configuration.BOT.getJDA().getGatewayPing());
        REST_PING.set(Configuration.BOT.getJDA().getRestPing().complete());
    }
}
