package com.basketbandit.core.modules.core.commands;

import com.basketbandit.core.database.DatabaseFunctions;
import com.basketbandit.core.modules.Command;
import com.basketbandit.core.utils.Utils;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandSetPrefix extends Command {

    public CommandSetPrefix() {
        super("setprefix", "com.basketbandit.core.modules.core.ModuleCore", new String[]{"-setprefix [prefix]"}, Permission.ADMINISTRATOR);
    }

    public CommandSetPrefix(MessageReceivedEvent e, String[] command) {
        executeCommand(e, command);
    }

    @Override
    protected void executeCommand(MessageReceivedEvent e, String[] command) {
        String value = command[1].toLowerCase();
        String serverLong = e.getGuild().getId();

        if(!new DatabaseFunctions().setServerPrefix(value, serverLong)) {
            Utils.sendMessage(e, "Server prefix set to: " + value);
        }
    }

}
