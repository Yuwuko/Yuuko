package com.basketbandit.core.modules.moderation;

import com.basketbandit.core.CommandExecutor;
import com.basketbandit.core.modules.Command;
import com.basketbandit.core.modules.Module;
import com.basketbandit.core.modules.moderation.commands.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ModuleModeration extends Module {

    public ModuleModeration(MessageReceivedEvent e, String[] command) {
        super("ModuleModeration", "moduleModeration", new Command[]{
                new CommandNuke(),
                new CommandMute(),
                new CommandUnmute(),
                new CommandBan(),
                new CommandKick()
        });

        if(e != null && command != null) {
            if(!checkModuleSettings(e)) {
                new CommandExecutor(e, command, this);
            }
        }
    }

}
