package com.basketbandit.core.modules.transport;

import com.basketbandit.core.Utils;
import com.basketbandit.core.modules.C;
import com.basketbandit.core.modules.Module;
import com.basketbandit.core.modules.transport.commands.CommandLineStatus;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ModuleTransport extends Module {

    public ModuleTransport() {
        super("ModuleTransport", "moduleTransport");
    }

    public ModuleTransport(MessageReceivedEvent e, String[] command) {
        super("ModuleTransport", "moduleTransport");

        if(checkModuleSettings(e)) {
            return;
        }

        executeCommand(e, command);
    }

    @Override
    protected void executeCommand(MessageReceivedEvent e, String[] command) {
        if(command[0].equals(C.LINE_STATUS.getCommandName())) {
            new CommandLineStatus(e, command);
            return;
        }

        Utils.sendMessage(e, "Sorry " + e.getAuthor().getAsMention() + ", you lack the required permissions to use that command.");
    }
}
