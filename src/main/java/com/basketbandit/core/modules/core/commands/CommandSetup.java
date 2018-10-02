package com.basketbandit.core.modules.core.commands;

import com.basketbandit.core.database.DatabaseFunctions;
import com.basketbandit.core.modules.Command;
import com.basketbandit.core.utils.Utils;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandSetup extends Command {

    public CommandSetup() {
        super("setup", "com.basketbandit.core.modules.core.ModuleCore", new String[]{"-setup"}, Permission.ADMINISTRATOR);
    }

    public CommandSetup(MessageReceivedEvent e, String[] command) {
        executeCommand(e, command);
    }

    public CommandSetup(GuildJoinEvent e) {
        executeCommandAux(e);
    }

    @Override
    protected void executeCommand(MessageReceivedEvent e, String[] command) {
        String serverId = e.getGuild().getId();

        if(!new DatabaseFunctions().addNewServer(serverId)) {
            Utils.sendMessage(e, "Server setup successful.");
        } else {
            Utils.sendMessage(e, "Server setup was unsuccessful. (It is likely already setup... try the -about command!)");
        }
    }

    private void executeCommandAux(GuildJoinEvent e) {
        String serverId = e.getGuild().getId();
        try {
            new DatabaseFunctions().addNewServer(serverId);
        } catch(Exception ex) {
            Utils.sendException(ex, "Server setup was unsuccessful (" + e.getGuild().getId() + ") [CommandSetup] (Automated)");
        }
    }

}
