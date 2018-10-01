package com.basketbandit.core.modules.core.settings;

import com.basketbandit.core.database.DatabaseFunctions;
import com.basketbandit.core.utils.Utils;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class SettingCommandPrefix {

    public SettingCommandPrefix(MessageReceivedEvent e, String value) {
        executeCommand(e, value);
    }

    public boolean executeCommand(MessageReceivedEvent e, String value) {

        if(new DatabaseFunctions().setServerSettings("commandPrefix", value, e.getGuild().getId())) {
            Utils.sendMessage(e, "Server prefix set to: " + value);
            return true;
        }

        return false;
    }

}
