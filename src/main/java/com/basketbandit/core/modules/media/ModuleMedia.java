package com.basketbandit.core.modules.media;

import com.basketbandit.core.CommandExecutor;
import com.basketbandit.core.modules.Command;
import com.basketbandit.core.modules.Module;
import com.basketbandit.core.modules.media.commands.CommandKitsu;
import com.basketbandit.core.modules.media.commands.CommandOsu;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ModuleMedia extends Module {

    public ModuleMedia(MessageReceivedEvent e, String[] command) {
        super("ModuleMedia", "moduleMedia", new Command[]{
                new CommandKitsu(),
                new CommandOsu()
        });

        if(e != null && command != null) {
            if(!checkModuleSettings(e)) {
                new CommandExecutor(e, command, this);
            }
        }
    }

}
