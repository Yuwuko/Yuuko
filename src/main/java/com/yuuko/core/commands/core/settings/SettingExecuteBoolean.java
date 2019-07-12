package com.yuuko.core.commands.core.settings;

import com.yuuko.core.MessageHandler;
import com.yuuko.core.database.function.GuildFunctions;
import com.yuuko.core.events.entity.MessageEvent;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;

public class SettingExecuteBoolean extends Setting {

    public SettingExecuteBoolean(MessageEvent e) {
        onCommand(e);
    }

    protected void onCommand(MessageEvent e) {
        try {
            String[] parameters = e.getParameters().toLowerCase().split("\\s+", 2);

            if(parameters.length > 1) {
                String intValue = (parameters[1].equals("true") || parameters[1].equals("yes")) ? "1" : "0";

                if(GuildFunctions.setGuildSettings(parameters[0], intValue, e.getGuild().getId())) {
                    if(Boolean.parseBoolean(parameters[1].toUpperCase())) {
                        EmbedBuilder embed = new EmbedBuilder().setColor(Color.GREEN).setTitle(parameters[0].toUpperCase() + " set to TRUE.");
                        MessageHandler.sendMessage(e, embed.build());
                    } else {
                        EmbedBuilder embed = new EmbedBuilder().setColor(Color.RED).setTitle(parameters[0].toUpperCase() + " set to FALSE.");
                        MessageHandler.sendMessage(e, embed.build());
                    }
                }
            }
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
        }
    }
}
