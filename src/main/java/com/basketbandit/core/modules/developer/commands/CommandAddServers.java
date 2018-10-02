package com.basketbandit.core.modules.developer.commands;

import com.basketbandit.core.database.DatabaseFunctions;
import com.basketbandit.core.modules.Command;
import com.basketbandit.core.utils.Utils;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandAddServers extends Command {

    public CommandAddServers() {
        super("addservers", "com.basketbandit.core.modules.developer.ModuleDeveloper", 0, new String[]{"-addservers"}, null);
    }

    @Override
    public void executeCommand(MessageReceivedEvent e, String[] command) {
        try {
            if(new DatabaseFunctions().addServers(e)) {
                Utils.sendMessage(e, "Servers added successfully.");
            }
        } catch(Exception ex) {
            Utils.sendException(ex, command[0]);
        }
    }

}
