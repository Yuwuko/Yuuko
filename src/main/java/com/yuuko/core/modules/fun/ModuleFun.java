package com.yuuko.core.modules.fun;

import com.yuuko.core.CommandExecutor;
import com.yuuko.core.modules.Command;
import com.yuuko.core.modules.Module;
import com.yuuko.core.modules.fun.commands.CommandRoll;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ModuleFun extends Module {

    public ModuleFun(MessageReceivedEvent e, String[] command) {
        super("Fun", "moduleFun", false, new Command[]{
                new CommandRoll()
        });

        new CommandExecutor(e, command, this);
    }

}
