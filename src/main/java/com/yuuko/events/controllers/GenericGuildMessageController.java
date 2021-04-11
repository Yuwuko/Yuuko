package com.yuuko.events.controllers;

import com.yuuko.CommandExecutor;
import com.yuuko.Yuuko;
import com.yuuko.database.function.GuildFunctions;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.metrics.MetricsManager;
import com.yuuko.modules.Command;
import com.yuuko.modules.setting.commands.CommandLogSetting;
import com.yuuko.modules.utility.commands.EventCommand;
import com.yuuko.modules.utility.commands.ReactionRoleCommand;
import com.yuuko.utilities.Sanitiser;
import net.dv8tion.jda.api.events.message.guild.GenericGuildMessageEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericGuildMessageController {
    private static final Logger log = LoggerFactory.getLogger(GenericGuildMessageController.class);

    public GenericGuildMessageController(GenericGuildMessageEvent event) {
        if(event instanceof GuildMessageReceivedEvent) {
            guildMessageReceivedEvent((GuildMessageReceivedEvent)event);
            return;
        }

        if(event instanceof GuildMessageDeleteEvent) {
            guildMessageDeleteEvent((GuildMessageDeleteEvent)event);
        }
    }

    private void guildMessageReceivedEvent(GuildMessageReceivedEvent event) {
        try {
            // Increments message event metric.
            MetricsManager.getDiscordMetrics(event.getJDA().getShardInfo().getShardId()).MESSAGE_EVENTS.getAndIncrement();

            // Don't process messages from bots. (prevents looping)
            if(event.getAuthor().isBot()) {
                return;
            }

            String prefix = GuildFunctions.getGuildSetting("prefix", event.getGuild().getId());
            final String message = event.getMessage().getContentRaw();

            // Checks null because the first few messages before the bot has fully initialised will cause exceptions.
            // Uses short-circuiting to set prefix to the variation used in the message or return if neither.
            if(prefix == null || !message.startsWith(prefix) && !message.startsWith(prefix = Yuuko.GLOBAL_PREFIX)) {
                return;
            }

            final String[] cmd = message.substring(prefix.length()).split("\\s+", 2);
            final String parameters = (cmd.length > 1) ? cmd[1] : null;
            final Command command = Yuuko.COMMANDS.get(cmd[0].toLowerCase());
            final String lang = GuildFunctions.getGuildLanguage(event.getGuild().getId());

            // Creates message event object, setting the event, prefix, command and parameters.
            MessageEvent context = new MessageEvent(event).setLanguage(lang).setPrefix(prefix).setCommand(command).setParameters(parameters);

            // fail-fast
            if(!Sanitiser.checks(context)) {
                return;
            }

            double execTime = System.nanoTime();
            new CommandExecutor(context);
            execTime = (System.nanoTime() - execTime)/1000000.0;

            MetricsManager.DatabaseInterface.updateCommandMetric(context, execTime);
            CommandLogSetting.execute(context, execTime);

        } catch(Exception e) {
            log.error("An error occurred while running the {} class, message: {}, input: {}", this, e.getMessage(), event.getMessage().getContentRaw(), e);
        }
    }

    private void guildMessageDeleteEvent(GuildMessageDeleteEvent event) {
        ReactionRoleCommand.DatabaseInterface.removeReactionRole(event.getMessageId());
        EventCommand.DatabaseInterface.removeEvent(event.getGuild().getId(), event.getMessageId());
    }

}
