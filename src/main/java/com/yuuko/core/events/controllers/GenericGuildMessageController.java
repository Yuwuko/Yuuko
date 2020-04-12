package com.yuuko.core.events.controllers;

import com.yuuko.core.CommandExecutor;
import com.yuuko.core.Configuration;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.setting.commands.CommandLogSetting;
import com.yuuko.core.database.function.DatabaseFunctions;
import com.yuuko.core.database.function.GuildFunctions;
import com.yuuko.core.database.function.ReactionRoleFunctions;
import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.metrics.MetricsManager;
import com.yuuko.core.metrics.pathway.EventMetrics;
import com.yuuko.core.utilities.Sanitiser;
import com.yuuko.core.utilities.Utilities;
import net.dv8tion.jda.api.events.message.guild.GenericGuildMessageEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericGuildMessageController {
    private static final Logger log = LoggerFactory.getLogger(GenericGuildMessageController.class);
    private static final EventMetrics metrics = MetricsManager.getEventMetrics();

    public GenericGuildMessageController(GenericGuildMessageEvent e) {
        if(e instanceof GuildMessageReceivedEvent) {
            guildMessageReceivedEvent((GuildMessageReceivedEvent)e);
            metrics.GUILD_MESSAGE_RECEIVED_EVENT.getAndIncrement();
            return;
        }

        if(e instanceof GuildMessageDeleteEvent) {
            guildMessageDeleteEvent((GuildMessageDeleteEvent)e);
            metrics.GUILD_MESSAGE_DELETE_EVENT.getAndIncrement();
        }
    }

    private void guildMessageReceivedEvent(GuildMessageReceivedEvent e) {
        try {
            if(e.getAuthor().isBot()) {
                return;
            }

            String prefix = Utilities.getServerPrefix(e.getGuild());
            final String message = e.getMessage().getContentRaw();

            // Checks null because the first few messages before the bot has fully initialised will cause exceptions.
            // Uses short-circuiting to set prefix to the variation used in the message or return if neither.
            if(prefix == null || !message.startsWith(prefix) && !message.startsWith(prefix = Configuration.GLOBAL_PREFIX)) {
                return;
            }

            final String[] cmd = message.substring(prefix.length()).split("\\s+", 2);
            final String parameters = (cmd.length > 1) ? cmd[1] : null;
            final Command command = Configuration.COMMANDS.get(cmd[0].toLowerCase());

            // Creates message event object, setting the event, prefix, command and parameters.
            MessageEvent event = new MessageEvent(e).setPrefix(prefix).setCommand(command).setParameters(parameters);

            // fail-fast
            if(!Sanitiser.checks(event)) {
                return;
            }

            double execTime = System.nanoTime();
            new CommandExecutor(event);
            execTime = (System.nanoTime() - execTime)/1000000.0;

            DatabaseFunctions.updateCommandLog(e.getGuild().getId(), event.getCommand().getName(), execTime);
            if(GuildFunctions.getGuildSetting("comlog", e.getGuild().getId()) != null) {
                CommandLogSetting.execute(event, execTime);
            }

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}, input: {}", this, ex.getMessage(), e.getMessage().getContentRaw(), ex);
        }
    }

    private void guildMessageDeleteEvent(GuildMessageDeleteEvent e) {
        // Reaction Role
        if(ReactionRoleFunctions.hasReactionRole(e.getMessageId())) {
            ReactionRoleFunctions.removeReactionRole(e.getMessageId());
        }
    }

}
