package com.yuuko.core.commands.core.settings;

import com.yuuko.core.MessageHandler;
import com.yuuko.core.database.function.GuildFunctions;
import com.yuuko.core.events.entity.MessageEvent;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;
import java.util.List;

public class SettingExecuteBoolean extends Setting {

    private static final List<String> booleanValues = Arrays.asList(
            "true",
            "yes",
            "false",
            "no"
    );

    public SettingExecuteBoolean(MessageEvent e) {
        onCommand(e);
    }

    protected void onCommand(MessageEvent e) {
        try {
            String[] parameters = e.getParameters().toLowerCase().split("\\s+", 2);

            if(parameters.length < 2) {
                return;
            }

            if(!booleanValues.contains(parameters[1])) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("_" + parameters[1].toUpperCase() + "_ is not a valid value. (Valid: TRUE, FALSE)");
                MessageHandler.sendMessage(e, embed.build());
                return;
            }

            String intValue = (parameters[1].equals("true") || parameters[1].equals("yes")) ? "1" : "0";

            if(GuildFunctions.setGuildSettings(parameters[0], intValue, e.getGuild().getId())) {
                if(Boolean.parseBoolean(parameters[1].toUpperCase())) {
                    EmbedBuilder embed = new EmbedBuilder().setTitle(parameters[0].toUpperCase() + " set to TRUE.");
                    MessageHandler.sendMessage(e, embed.build());
                } else {
                    EmbedBuilder embed = new EmbedBuilder().setTitle(parameters[0].toUpperCase() + " set to FALSE.");
                    MessageHandler.sendMessage(e, embed.build());
                }
            }
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
        }
    }
}
