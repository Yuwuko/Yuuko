package com.yuuko.core.modules.fun;

import com.yuuko.core.CommandExecutor;
import com.yuuko.core.modules.Command;
import com.yuuko.core.modules.Module;
import com.yuuko.core.modules.fun.commands.ChooseCommand;
import com.yuuko.core.modules.fun.commands.RollCommand;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class FunModule extends Module {

    public FunModule(MessageReceivedEvent e, String[] command) {
        super("Fun", "moduleFun", false, new Command[]{
                new RollCommand(),
                new ChooseCommand()
        });

        new CommandExecutor(e, command, this);
    }

}
