package com.basketbandit.core.modules.core.settings;

import com.basketbandit.core.database.DatabaseFunctions;
import com.basketbandit.core.utils.MessageHandler;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class SettingCommandPrefix {

    public SettingCommandPrefix(MessageReceivedEvent e, String value) {
        executeCommand(e, value);
    }

    public boolean executeCommand(MessageReceivedEvent e, String value) {

        if(new DatabaseFunctions().setServerSettings("commandPrefix", value, e.getGuild().getId())) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("COMMANDPREFIX set to " + value);
            MessageHandler.sendMessage(e, embed.build());
            return true;
        }

        return false;
    }

}
