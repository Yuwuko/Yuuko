package com.basketbandit.core.modules.core.settings;

import com.basketbandit.core.database.DatabaseFunctions;
import com.basketbandit.core.utils.Utils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;

public class SettingCommandPrefix {

    public SettingCommandPrefix(MessageReceivedEvent e, String value) {
        executeCommand(e, value);
    }

    public boolean executeCommand(MessageReceivedEvent e, String value) {

        if(new DatabaseFunctions().setServerSettings("commandPrefix", value, e.getGuild().getId())) {
            EmbedBuilder embed = new EmbedBuilder().setColor(Color.DARK_GRAY).setAuthor("'commandPrefix' set to " + value);
            Utils.sendMessage(e, "Server prefix set to: " + value);
            return true;
        }

        return false;
    }

}
