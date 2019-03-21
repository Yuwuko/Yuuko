package com.yuuko.core.metrics;

import com.yuuko.core.Configuration;
import net.dv8tion.jda.core.entities.Guild;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class DiscordMetrics {
    public int GUILD_COUNT = 0;
    public int CHANNEL_COUNT = 0;
    public int USER_COUNT = 0;
    public int EMOTE_COUNT = 0;
    public int ROLE_COUNT = 0;
    public final AtomicLong PING = new AtomicLong();

    /**
     * Updates the Discord Metrics to current values.
     */
    public void update() {
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

        GUILD_COUNT = guilds.size();
        USER_COUNT = userCount;
        CHANNEL_COUNT = channelCount;
        EMOTE_COUNT = emoteCount;
        ROLE_COUNT = roleCount;
    }

    /**
     * Updates the bot's currently returned ping.
     */
    public void updatePing() {
        PING.set(Configuration.BOT.getJDA().getPing());
    }
}
