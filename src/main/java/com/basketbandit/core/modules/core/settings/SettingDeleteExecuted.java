package com.basketbandit.core.modules.core.settings;

import com.basketbandit.core.database.DatabaseFunctions;
import com.basketbandit.core.utils.MessageHandler;
import com.basketbandit.core.utils.Utils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;

public class SettingDeleteExecuted {

    public SettingDeleteExecuted(MessageReceivedEvent e, String value) {
        executeCommand(e, value);
    }

    public boolean executeCommand(MessageReceivedEvent e, String value) {
        try {
            String intvalue = (value.equalsIgnoreCase("true")) ? "1" : "0";

            if(new DatabaseFunctions().setServerSettings("deleteExecuted", intvalue, e.getGuild().getId())) {
                if(Boolean.parseBoolean(value.toUpperCase())) {
                    EmbedBuilder embed = new EmbedBuilder().setColor(Color.GREEN).setAuthor("'deleteExecuted' set to TRUE.");
                    MessageHandler.sendMessage(e, embed.build());
                } else {
                    EmbedBuilder embed = new EmbedBuilder().setColor(Color.RED).setAuthor("'deleteExecuted' set to FALSE.");
                    MessageHandler.sendMessage(e, embed.build());
                }
                return true;
            } else {
                return false;
            }

        } catch(Exception ex) {
            Utils.sendException(ex, "SettingDeleteExecuted [" + value + "]");
            return false;
        }
    }

}
