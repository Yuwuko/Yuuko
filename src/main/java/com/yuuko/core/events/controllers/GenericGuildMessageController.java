package com.yuuko.core.events.controllers;

import com.yuuko.core.Configuration;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.core.settings.CommandLogSetting;
import com.yuuko.core.database.function.DatabaseFunctions;
import com.yuuko.core.database.function.GuildFunctions;
import com.yuuko.core.database.function.ReactionRoleFunctions;
import com.yuuko.core.events.extensions.MessageEvent;
import com.yuuko.core.metrics.MetricsManager;
import net.dv8tion.jda.core.events.message.guild.GenericGuildMessageEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericGuildMessageController {
    private static final Logger log = LoggerFactory.getLogger(GenericGuildMessageController.class);

    public GenericGuildMessageController(GenericGuildMessageEvent e) {
        if(e instanceof GuildMessageReceivedEvent) {
            guildMessageReceivedEvent((GuildMessageReceivedEvent)e);
        } else if(e instanceof GuildMessageDeleteEvent) {
            guildMessageDeleteEvent((GuildMessageDeleteEvent)e);
        }
    }

    private void guildMessageReceivedEvent(GuildMessageReceivedEvent e) {
        try {
            final double executionStart = System.nanoTime();

            if(e.getAuthor().isBot()) {
                MetricsManager.getEventMetrics().BOT_MESSAGES_PROCESSED.getAndIncrement();
                return;
            } else {
                MetricsManager.getEventMetrics().HUMAN_MESSAGES_PROCESSED.getAndIncrement();
            }

            MessageEvent event = new MessageEvent(e);

            if(event.getPrefix().equals("")) {
                return;
            }

            boolean executed = false;

            // Iterate through the command list, get the command commands constructor from the command class.
            Command command = Configuration.COMMANDS.get(event.getCommand().get(0));
            if(command != null) {
                command.getModule().getConstructor(MessageEvent.class).newInstance(event);
                executed = true;
            }

            if(executed) {
                final double executionTime = (System.nanoTime() - executionStart)/1000000.0;
                DatabaseFunctions.updateCommandsLog(e.getGuild().getId(), event.getCommand().get(0), executionTime);
                if(GuildFunctions.getGuildSetting("comlog", e.getGuild().getId()) != null) {
                    CommandLogSetting.execute(event, executionTime);
                }
            }

        } catch(NullPointerException ex) {
            // Do nothing, null pointers happen. (Should they though...)
        } catch (Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
        }
    }

    private void guildMessageDeleteEvent(GuildMessageDeleteEvent e) {

        // Reaction Role
        if(ReactionRoleFunctions.hasReactionRole(e.getMessageId())) {
            ReactionRoleFunctions.removeReactionRole(e.getMessageId());
        }
    }

}
