package com.yuuko.core.modules.moderation;

import com.yuuko.core.CommandExecutor;
import com.yuuko.core.modules.Command;
import com.yuuko.core.modules.Module;
import com.yuuko.core.modules.moderation.commands.*;
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
