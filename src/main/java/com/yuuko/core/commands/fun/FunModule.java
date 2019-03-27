package com.yuuko.core.commands.fun;

import com.yuuko.core.CommandExecutor;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.Module;
import com.yuuko.core.commands.fun.commands.*;
import com.yuuko.core.events.extensions.MessageEvent;

public class FunModule extends Module {
    private static final Command[] commands = new Command[]{
            new RollCommand(),
            new ChooseCommand(),
            new SpoilerifyCommand(),
            new EightBallCommand(),
            new CoinFlipCommand()
    };

    public FunModule(MessageEvent e) {
        super("Fun", "moduleFun", false, commands);
        new CommandExecutor(e, this);
    }

}
