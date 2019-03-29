package com.yuuko.core.events.controllers;

import com.yuuko.core.Configuration;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.core.settings.CommandLogSetting;
import com.yuuko.core.database.DatabaseFunctions;
import com.yuuko.core.database.GuildFunctions;
import com.yuuko.core.events.extensions.MessageEvent;
import com.yuuko.core.metrics.handlers.MetricsManager;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericMessageController {
    private static final Logger log = LoggerFactory.getLogger(GenericMessageController.class);

    public GenericMessageController(GenericMessageEvent e) {
        if(e instanceof MessageReceivedEvent) {
            messageReceivedEvent((MessageReceivedEvent)e);
        }
    }

    private void messageReceivedEvent(MessageReceivedEvent e) {
        try {
            double executionStart = System.nanoTime();

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

            // Iterate through the command list, get the command commands constructor from the command class.
            boolean executed = false;
            for(Command command: Configuration.COMMANDS) {
                if(event.getCommand()[0].equalsIgnoreCase(command.getName())) {
                    command.getModule().getConstructor(MessageEvent.class).newInstance(event);
                    executed = true;
                    break;
                }
            }

            if(executed) {
                DatabaseFunctions.updateCommandsLog(e.getGuild().getId(), event.getCommand()[0].toLowerCase());
                if(GuildFunctions.getGuildSetting("commandLog", e.getGuild().getId()) != null) {
                    CommandLogSetting.execute(e, (System.nanoTime() - executionStart)/1000000.0);
                }
            }

        } catch(NullPointerException ex) {
            // Do nothing, null pointers happen. (Should they though...)
        } catch (Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
        }
    }

}
