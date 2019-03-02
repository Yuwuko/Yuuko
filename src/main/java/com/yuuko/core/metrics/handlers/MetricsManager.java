package com.yuuko.core.metrics.handlers;

import com.yuuko.core.Configuration;
import com.yuuko.core.metrics.DatabaseMetrics;
import com.yuuko.core.metrics.DiscordMetrics;
import com.yuuko.core.metrics.EventMetrics;
import com.yuuko.core.metrics.SystemMetrics;
import net.dv8tion.jda.core.entities.Guild;

import java.util.List;

public class MetricsManager {

    private static final SystemMetrics systemMetrics = new SystemMetrics();
    private static final EventMetrics eventMetrics = new EventMetrics();
    private static final DatabaseMetrics databaseMetrics = new DatabaseMetrics();
    private static final DiscordMetrics discordMetrics = new DiscordMetrics();

    public static void reset() {
        eventMetrics.reset();
        databaseMetrics.reset();
    }

    public static DatabaseMetrics getDatabaseMetrics() {
        return databaseMetrics;
    }

    public static DiscordMetrics getDiscordMetrics() {
        return discordMetrics;
    }

    public static EventMetrics getEventMetrics() {
        return eventMetrics;
    }

    public static SystemMetrics getSystemMetrics() {
        return systemMetrics;
    }

    public static void updateDiscordMetrics() {
        List<Guild> guilds = Configuration.BOT.getJDA().getGuilds();

        int userCount = 0;
        int channelCount = 0;
        int emoteCount = 0;
        int roleCount = 0;

        for(Guild guild : guilds) {
            userCount += guild.getMemberCache().size();
            channelCount += guild.getChannels().size();
            emoteCount += guild.getEmoteCache().size();
            roleCount += guild.getRoleCache().size();
        }

        discordMetrics.GUILD_COUNT = guilds.size();
        discordMetrics.USER_COUNT = userCount;
        discordMetrics.CHANNEL_COUNT = channelCount;
        discordMetrics.EMOTE_COUNT = emoteCount;
        discordMetrics.ROLE_COUNT = roleCount;
    }
}
