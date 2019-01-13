package com.yuuko.core.modules.core.settings;

import com.yuuko.core.database.DatabaseFunctions;
import com.yuuko.core.utilities.MessageHandler;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class PrefixSetting {

    public PrefixSetting(MessageReceivedEvent e, String value) {
        executeCommand(e, value);
    }

    public void executeCommand(MessageReceivedEvent e, String value) {
        if(value.length() == 0 || value.length() > 5) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Parameter").setDescription("Prefixes have a minimum length of **1** and a maximum length of **5** characters.");
            MessageHandler.sendMessage(e, embed.build());
            return;
        }

        if(new DatabaseFunctions().setServerSettings("commandPrefix", value, e.getGuild().getId())) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Prefix Changed").setDescription("The prefix used for this guild has been set to " + value);
            MessageHandler.sendMessage(e, embed.build());
        }
    }

}
