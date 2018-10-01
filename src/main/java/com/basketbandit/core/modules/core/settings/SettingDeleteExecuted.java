package com.basketbandit.core.modules.core.settings;

import com.basketbandit.core.database.DatabaseFunctions;
import com.basketbandit.core.utils.Utils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;

public class SettingDeleteExecuted {

    public SettingDeleteExecuted(MessageReceivedEvent e, String value) {
        executeCommand(e, value);
    }

    public boolean executeCommand(MessageReceivedEvent e, String value) {

        if(new DatabaseFunctions().setServerSettings("deleteExecuted", value, e.getGuild().getId())) {
            if(Boolean.parseBoolean(value)) {
                EmbedBuilder embed = new EmbedBuilder().setColor(Color.GREEN).setAuthor("'deleteExecuted' set to TRUE.");
                Utils.sendMessage(e, embed.build());
            } else {
                EmbedBuilder embed = new EmbedBuilder().setColor(Color.RED).setAuthor("'deleteExecuted' set to FALSE.");
                Utils.sendMessage(e, embed.build());
            }
            return true;

        } else {
            return false;
        }
    }

}
