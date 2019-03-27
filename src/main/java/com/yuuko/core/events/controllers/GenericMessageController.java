package com.yuuko.core.events.controllers;

import com.yuuko.core.Configuration;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.core.settings.CommandLogSetting;
import com.yuuko.core.database.DatabaseFunctions;
import com.yuuko.core.database.GuildFunctions;
import com.yuuko.core.events.extensions.MessageEvent;
import com.yuuko.core.metrics.handlers.MetricsManager;
import com.yuuko.core.utilities.Utilities;
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
            if(e.getAuthor().isBot()) {
                MetricsManager.getEventMetrics().BOT_MESSAGES_PROCESSED.getAndIncrement();
                return;
            }

            MetricsManager.getEventMetrics().HUMAN_MESSAGES_PROCESSED.getAndIncrement();

            // Used to help calculate execution time of functions.
            long startExecutionNano = System.nanoTime();

            String message = e.getMessage().getContentRaw().toLowerCase();

            // If message starts with server prefix, use it, if it starts with global prefix, use that, else it isn't a command.
            String prefix = message.startsWith(Utilities.getServerPrefix(e.getGuild())) ? Utilities.getServerPrefix(e.getGuild()) : message.startsWith(Configuration.GLOBAL_PREFIX) ? Configuration.GLOBAL_PREFIX : "";
            if(!prefix.equals("")) {
                String[] command = e.getMessage().getContentRaw().substring(prefix.length()).split("\\s+", 2);

                double executionTime = 0;
                boolean executed = false;

                // Iterate through the command list, get the command commands constructor from the command class.
                for(Command cmd: Configuration.COMMANDS) {
                    if(command[0].equalsIgnoreCase(cmd.getName())) {
                        cmd.getModule().getConstructor(MessageEvent.class).newInstance(new MessageEvent(e, command));
                        executionTime = (System.nanoTime() - startExecutionNano)/1000000.0;
                        executed = true;
                        break;
                    }
                }

                if(executed) {
                    DatabaseFunctions.updateCommandsLog(e.getGuild().getId(), command[0].toLowerCase());
                    if(GuildFunctions.getGuildSetting("commandLog", e.getGuild().getId()) != null) {
                        CommandLogSetting.execute(e, executionTime);
                    }
                }
            }

        } catch(NullPointerException ex) {
            // Do nothing, null pointers happen. (Should they though...)
        } catch (Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
        }
    }

}
