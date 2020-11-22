package com.yuuko.core.commands.utility.commands;

import com.yuuko.core.Config;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.metrics.MetricsManager;
import net.dv8tion.jda.api.EmbedBuilder;

import java.time.Instant;
import java.util.Arrays;

public class PingCommand extends Command {

    public PingCommand() {
        super("ping", Config.MODULES.get("utility"), 0, -1L, Arrays.asList("-ping"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Pong!")
                .setDescription("GATEWAY PING is currently " + MetricsManager.getDiscordMetrics(e.getShardId()).GATEWAY_PING + "ms. \nREST PING is currently "+ MetricsManager.getDiscordMetrics(e.getShardId()).REST_PING + "ms.")
                .setTimestamp(Instant.now())
                .setFooter(Config.STANDARD_STRINGS.get(1) + e.getMember().getEffectiveName(), e.getAuthor().getEffectiveAvatarUrl());
        MessageHandler.sendMessage(e, embed.build());
    }
}
