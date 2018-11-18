package com.basketbandit.core.modules.profile;

import com.basketbandit.core.CommandExecutor;
import com.basketbandit.core.modules.Command;
import com.basketbandit.core.modules.Module;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ModuleProfile extends Module {

    public ModuleProfile(MessageReceivedEvent e, String[] command) {
        super("ModuleProfile", "moduleProfile", new Command[]{
                // Commands here
        });

        if(e != null && command != null) {
            if(!checkModuleSettings(e)) {
                new CommandExecutor(e, command, this);
            }
        }
    }

}
