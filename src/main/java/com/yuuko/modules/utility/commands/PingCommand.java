package com.yuuko.modules.utility.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.Yuuko;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.metrics.MetricsManager;
import com.yuuko.modules.Command;
import net.dv8tion.jda.api.EmbedBuilder;

import java.time.Instant;
import java.util.Arrays;

public class PingCommand extends Command {

    public PingCommand() {
        super("ping", 0, -1L, Arrays.asList("-ping"), false, null);
    }

    @Override
    public void onCommand(MessageEvent context) throws Exception {
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Pong!")
                .setDescription("GATEWAY PING is currently " + MetricsManager.getDiscordMetrics(context.getShardId()).GATEWAY_PING + "ms. \nREST PING is currently "+ MetricsManager.getDiscordMetrics(context.getShardId()).REST_PING + "ms.")
                .setTimestamp(Instant.now())
                .setFooter(Yuuko.STANDARD_STRINGS.get(1) + context.getAuthor().getAsTag(), context.getAuthor().getEffectiveAvatarUrl());
        MessageDispatcher.reply(context, embed.build());
    }
}
