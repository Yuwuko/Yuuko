package com.yuuko.core.modules.math;

import com.yuuko.core.CommandExecutor;
import com.yuuko.core.modules.Command;
import com.yuuko.core.modules.Module;
import com.yuuko.core.modules.math.commands.CommandRoll;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ModuleMath extends Module {

    public ModuleMath(MessageReceivedEvent e, String[] command) {
        super("ModuleMath", "moduleMath", new Command[]{
                new CommandRoll()
        });

        if(e != null && command != null) {
            if(!checkModuleSettings(e)) {
                new CommandExecutor(e, command, this);
            }
        }
    }

}
