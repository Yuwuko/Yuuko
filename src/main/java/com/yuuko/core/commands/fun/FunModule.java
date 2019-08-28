package com.yuuko.core.commands.fun;

import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.Module;
import com.yuuko.core.commands.fun.commands.*;

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
            new JokeCommand(),
            new HoroscopeCommand()
    );

    public FunModule() {
        super("fun", false, commands);
    }

}
