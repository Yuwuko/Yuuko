package com.yuuko.core.commands.core.settings;

import com.yuuko.core.database.DatabaseFunctions;
import com.yuuko.core.utilities.MessageHandler;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;

public class SettingExecuteBoolean {

    public SettingExecuteBoolean(MessageReceivedEvent e, String setting, String value) {
        if(e != null && setting != null && value != null) {
            onCommand(e, setting, value);
        }
    }

    private void onCommand(MessageReceivedEvent e, String setting, String value) {
        try {
            String intValue = (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("yes")) ? "1" : "0";

            if(DatabaseFunctions.setGuildSettings(setting, intValue, e.getGuild().getId())) {
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

}
