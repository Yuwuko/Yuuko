package com.basketbandit.core.modules.math;

import com.basketbandit.core.CommandExecutor;
import com.basketbandit.core.modules.Command;
import com.basketbandit.core.modules.Module;
import com.basketbandit.core.modules.math.commands.CommandRoll;
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
