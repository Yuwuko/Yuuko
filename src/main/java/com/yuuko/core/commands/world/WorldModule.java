package com.yuuko.core.commands.world;

import com.yuuko.core.CommandExecutor;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.Module;
import com.yuuko.core.commands.world.commands.*;
import com.yuuko.core.events.entity.MessageEvent;

import java.util.Arrays;
import java.util.List;

public class WorldModule extends Module {
    private static final List<Command> commands = Arrays.asList(
            new LondonUndergroundCommand(),
            new WeatherCommand(),
            new TescoCommand(),
            new CountdownCommand(),
            new NationalGeographicCommand(),
            new UKParliamentPetitionCommand()
    );

    public WorldModule(MessageEvent e) {
        super("world", false, commands);
        new CommandExecutor(e, this);
    }

}
