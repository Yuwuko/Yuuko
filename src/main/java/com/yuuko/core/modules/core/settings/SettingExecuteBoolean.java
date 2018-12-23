package com.yuuko.core.modules.core.settings;

import com.yuuko.core.Configuration;
import com.yuuko.core.database.DatabaseFunctions;
import com.yuuko.core.utils.MessageHandler;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.List;

public class SettingExecuteBoolean {

    public SettingExecuteBoolean(MessageReceivedEvent e, String setting, String value) {
        if(e != null && setting != null && value != null) {
            executeCommand(e, setting, value);
        }
    }

    public void executeCommand(MessageReceivedEvent e, String setting, String value) {
        try {
            String intValue = (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("yes")) ? "1" : "0";

            if(new DatabaseFunctions().setServerSettings(setting, intValue, e.getGuild().getId())) {
                if(Boolean.parseBoolean(value.toUpperCase())) {
                    EmbedBuilder embed = new EmbedBuilder().setColor(Color.GREEN).setTitle(setting.toUpperCase() + " set to TRUE.");
                    MessageHandler.sendMessage(e, embed.build());
                } else {
                    EmbedBuilder embed = new EmbedBuilder().setColor(Color.RED).setTitle(setting.toUpperCase() + " set to FALSE.");
                    MessageHandler.sendMessage(e, embed.build());
                }
            }
        } catch(Exception ex) {
            MessageHandler.sendException(ex, setting + "[" + value + "]");
        }
    }

    /**
     * Executes the logging feature of the bot.
     * @param e MessageReceivedEvent
     * @param executionTimeMs long
     */
    public void executeLogging(MessageReceivedEvent e, long executionTimeMs) {
        try {
            List<TextChannel> log = e.getGuild().getTextChannelsByName("command-log", true);
            log.get(0).sendMessage("```" + e.getAuthor().getName() + "#" + e.getAuthor().getDiscriminator() + " used command " + e.getMessage().getContentDisplay() + " in " + e.getMessage().getChannel().getName() + ". (Execution time: "+ executionTimeMs +"ms)```").queue();
        } catch(Exception ex) {
            MessageHandler.sendMessage(e, "This server has logging enabled but has no 'command-log' text channel, please add that channel or disable the module by typing the '" + Configuration.GLOBAL_PREFIX + "module logging' command to stop this message.");
        }
    }

}
