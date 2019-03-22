package com.yuuko.core.commands.core.settings;

import com.yuuko.core.MessageHandler;
import com.yuuko.core.database.GuildFunctions;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;

public class SettingExecuteBoolean extends Setting {

    public SettingExecuteBoolean(MessageReceivedEvent e, String setting, String value) {
        if(e != null && setting != null && value != null) {
            onCommand(e, setting, value);
        }
    }

    protected void onCommand(MessageReceivedEvent e, String setting, String value) {
        try {
            String intValue = (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("yes")) ? "1" : "0";

            if(GuildFunctions.setGuildSettings(setting, intValue, e.getGuild().getId())) {
                if(Boolean.parseBoolean(value.toUpperCase())) {
                    EmbedBuilder embed = new EmbedBuilder().setColor(Color.GREEN).setTitle(setting.toUpperCase() + " set to TRUE.");
                    MessageHandler.sendMessage(e, embed.build());
                } else {
                    EmbedBuilder embed = new EmbedBuilder().setColor(Color.RED).setTitle(setting.toUpperCase() + " set to FALSE.");
                    MessageHandler.sendMessage(e, embed.build());
                }
            }
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
        }
    }

    @Override
    protected void onCommand(MessageReceivedEvent e, String command) {
        //
    }
}
