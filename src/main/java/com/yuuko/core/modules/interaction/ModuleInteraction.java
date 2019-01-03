package com.yuuko.core.modules.interaction;

import com.yuuko.core.CommandExecutor;
import com.yuuko.core.modules.Command;
import com.yuuko.core.modules.Module;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ModuleInteraction extends Module {

    public ModuleInteraction(MessageReceivedEvent e, String[] command) {
        super("Profile", "moduleProfile", false, new Command[]{
                // Commands here
        });

        new CommandExecutor(e, command, this);
    }

}
