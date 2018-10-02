package com.basketbandit.core.modules.transport;

import com.basketbandit.core.CommandExecutor;
import com.basketbandit.core.modules.Command;
import com.basketbandit.core.modules.Module;
import com.basketbandit.core.modules.transport.commands.CommandLineStatus;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ModuleTransport extends Module {

    public ModuleTransport(MessageReceivedEvent e, String[] command) {
        super("ModuleTransport", "moduleTransport", new Command[]{
                new CommandLineStatus()
        });

        if(e != null && command != null) {
            if(!checkModuleSettings(e)) {
                new CommandExecutor(e, command, this);
            }
        }
    }

}
