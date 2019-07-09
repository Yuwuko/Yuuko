package com.yuuko.core.commands.fun;

import com.yuuko.core.CommandExecutor;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.Module;
import com.yuuko.core.commands.fun.commands.*;
import com.yuuko.core.events.entity.MessageEvent;

import java.util.Arrays;
import java.util.List;

public class FunModule extends Module {
    private static final List<Command> commands = Arrays.asList(
            new RollCommand(),
            new ChooseCommand(),
            new SpoilerifyCommand(),
            new EightBallCommand(),
            new CoinFlipCommand(),
            new AdviceCommand(),
            new JokeCommand()
    );

    public FunModule(MessageEvent e) {
        super("fun", false, commands);
        if(e != null) {
            new CommandExecutor(e.setModule(this));
        }
    }

}
