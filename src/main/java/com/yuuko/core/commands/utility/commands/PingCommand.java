package com.yuuko.core.commands.utility.commands;

import com.yuuko.core.Configuration;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.utility.UtilityModule;
import com.yuuko.core.metrics.handlers.MetricsManager;
import com.yuuko.core.utilities.MessageHandler;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.time.Instant;

public class PingCommand extends Command {

    public PingCommand() {
        super("ping", UtilityModule.class, 0, new String[]{"-ping"}, false, null);
    }

    @Override
    public void onCommand(MessageReceivedEvent e, String[] command) {
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Ping")
                .setDescription("Current ping to Discord is " + MetricsManager.getDiscordMetrics().PING + "ms.")
                .setTimestamp(Instant.now())
                .setFooter(Configuration.STANDARD_STRINGS[1] + e.getMember().getEffectiveName(), e.getAuthor().getEffectiveAvatarUrl());
        MessageHandler.sendMessage(e, embed.build());
    }
}
