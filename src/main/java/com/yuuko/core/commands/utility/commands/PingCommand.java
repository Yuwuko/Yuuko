package com.yuuko.core.commands.utility.commands;

import com.yuuko.core.MessageDispatcher;
import com.yuuko.core.Yuuko;
import com.yuuko.core.commands.Command;
import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.metrics.MetricsManager;
import net.dv8tion.jda.api.EmbedBuilder;

import java.time.Instant;
import java.util.Arrays;

public class PingCommand extends Command {

    public PingCommand() {
        super("ping", Yuuko.MODULES.get("utility"), 0, -1L, Arrays.asList("-ping"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) throws Exception {
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Pong!")
                .setDescription("GATEWAY PING is currently " + MetricsManager.getDiscordMetrics(e.getShardId()).GATEWAY_PING + "ms. \nREST PING is currently "+ MetricsManager.getDiscordMetrics(e.getShardId()).REST_PING + "ms.")
                .setTimestamp(Instant.now())
                .setFooter(Yuuko.STANDARD_STRINGS.get(1) + e.getMember().getEffectiveName(), e.getAuthor().getEffectiveAvatarUrl());
        MessageDispatcher.reply(e, embed.build());
    }
}
