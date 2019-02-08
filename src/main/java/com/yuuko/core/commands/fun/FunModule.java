package com.yuuko.core.commands.fun;

import com.yuuko.core.CommandExecutor;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.Module;
import com.yuuko.core.commands.fun.commands.ChooseCommand;
import com.yuuko.core.commands.fun.commands.RollCommand;
import com.yuuko.core.commands.fun.commands.SpoilerifyCommand;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class FunModule extends Module {

    public FunModule(MessageReceivedEvent e, String[] command) {
        super("Fun", "moduleFun", false, new Command[]{
                new RollCommand(),
                new ChooseCommand(),
                new SpoilerifyCommand()
        });

        new CommandExecutor(e,this, command);
    }

}
