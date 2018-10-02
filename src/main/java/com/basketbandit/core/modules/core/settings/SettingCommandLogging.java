package com.basketbandit.core.modules.core.settings;

import com.basketbandit.core.Configuration;
import com.basketbandit.core.database.DatabaseFunctions;
import com.basketbandit.core.utils.Utils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.List;

public class SettingCommandLogging {

    public SettingCommandLogging(MessageReceivedEvent e, String value) {
        if(e != null && value != null) {
            executeCommand(e, value);
        }
    }

    public boolean executeCommand(MessageReceivedEvent e, String value) {

        if(new DatabaseFunctions().setServerSettings("commandLogging", value, e.getGuild().getId())) {
            EmbedBuilder embed = new EmbedBuilder().setColor(Color.DARK_GRAY).setAuthor("'commandLogging' set to " + value);
            Utils.sendMessage(e, embed.build());
            return true;
        }

        return false;
    }

    public void executeSetting(MessageReceivedEvent e, long executionTimeMs) {
        try {
            List<TextChannel> log = e.getGuild().getTextChannelsByName("command-log", true);
            log.get(0).sendMessage("```" + e.getAuthor().getName() + "#" + e.getAuthor().getDiscriminator() + " used command " + e.getMessage().getContentDisplay() + " in " + e.getMessage().getChannel().getName() + ". (Execution time: "+ executionTimeMs +"ms)```").queue();
        } catch(Exception ex) {
            Utils.sendMessage(e, "This server has logging enabled but has no 'command-log' text channel, please add that channel or disable the module by typing the '" + Configuration.GLOBAL_PREFIX + "module logging' command to stop this message.");
        }
    }
}
