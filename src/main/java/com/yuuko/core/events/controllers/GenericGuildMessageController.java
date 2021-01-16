package com.yuuko.core.events.controllers;

import com.yuuko.core.CommandExecutor;
import com.yuuko.core.Yuuko;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.setting.commands.CommandLogSetting;
import com.yuuko.core.commands.utility.commands.ReactionRoleCommand;
import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.metrics.MetricsManager;
import com.yuuko.core.utilities.Sanitiser;
import com.yuuko.core.utilities.Utilities;
import net.dv8tion.jda.api.events.message.guild.GenericGuildMessageEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericGuildMessageController {
    private static final Logger log = LoggerFactory.getLogger(GenericGuildMessageController.class);

    public GenericGuildMessageController(GenericGuildMessageEvent e) {
        if(e instanceof GuildMessageReceivedEvent) {
            guildMessageReceivedEvent((GuildMessageReceivedEvent)e);
            return;
        }

        if(e instanceof GuildMessageDeleteEvent) {
            guildMessageDeleteEvent((GuildMessageDeleteEvent)e);
        }
    }

    private void guildMessageReceivedEvent(GuildMessageReceivedEvent e) {
        try {
            // Increments message event metric.
            MetricsManager.getDiscordMetrics(e.getJDA().getShardInfo().getShardId()).MESSAGE_EVENTS.getAndIncrement();

            // Don't process messages from bots. (prevents looping)
            if(e.getAuthor().isBot()) {
                return;
            }

            String prefix = Utilities.getServerPrefix(e.getGuild());
            final String message = e.getMessage().getContentRaw();

            // Checks null because the first few messages before the bot has fully initialised will cause exceptions.
            // Uses short-circuiting to set prefix to the variation used in the message or return if neither.
            if(prefix == null || !message.startsWith(prefix) && !message.startsWith(prefix = Yuuko.GLOBAL_PREFIX)) {
                return;
            }

            final String[] cmd = message.substring(prefix.length()).split("\\s+", 2);
            final String parameters = (cmd.length > 1) ? cmd[1] : null;
            final Command command = Yuuko.COMMANDS.get(cmd[0].toLowerCase());

            // Creates message event object, setting the event, prefix, command and parameters.
            MessageEvent event = new MessageEvent(e).setPrefix(prefix).setCommand(command).setParameters(parameters);

            // fail-fast
            if(!Sanitiser.checks(event)) {
                return;
            }

            double execTime = System.nanoTime();
            new CommandExecutor(event);
            execTime = (System.nanoTime() - execTime)/1000000.0;

            MetricsManager.DatabaseInterface.updateCommandMetric(event, execTime);
            CommandLogSetting.execute(event, execTime);

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}, input: {}", this, ex.getMessage(), e.getMessage().getContentRaw(), ex);
        }
    }

    private void guildMessageDeleteEvent(GuildMessageDeleteEvent e) {
        ReactionRoleCommand.DatabaseInterface.removeReactionRole(e.getMessageId());
    }

}
