package com.yuuko.core.commands.fun;

import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.Module;
import com.yuuko.core.commands.fun.commands.*;

import java.util.Map;

import static java.util.Map.entry;

public class FunModule extends Module {
    private static final Map<String, Class<? extends Command>> commands = Map.ofEntries(
            entry("roll", RollCommand.class),
            entry("choose", ChooseCommand.class),
            entry("spoilerify", SpoilerifyCommand.class),
            entry("8ball", EightBallCommand.class),
            entry("flip", CoinFlipCommand.class),
            entry("advice", AdviceCommand.class),
            entry("joke", JokeCommand.class),
            entry("horoscope", HoroscopeCommand.class)
    );

    public FunModule() {
        super("fun", false, commands);
    }

}
