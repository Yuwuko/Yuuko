package com.yuuko.core.commands.fun;

import com.yuuko.core.CommandExecutor;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.Module;
import com.yuuko.core.commands.fun.commands.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class FunModule extends Module {

    public FunModule(MessageReceivedEvent e, String[] command) {
        super("Fun", "moduleFun", false, new Command[]{
                new RollCommand(),
                new ChooseCommand(),
                new SpoilerifyCommand(),
                new EightBallCommand(),
                new CoinFlipCommand()
        });

        new CommandExecutor(e,this, command);
    }

}
