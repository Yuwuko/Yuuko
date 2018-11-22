package com.yuuko.core.modules.profile;

import com.yuuko.core.CommandExecutor;
import com.yuuko.core.modules.Command;
import com.yuuko.core.modules.Module;
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
