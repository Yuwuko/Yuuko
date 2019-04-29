package com.yuuko.core.commands.world;

import com.yuuko.core.CommandExecutor;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.Module;
import com.yuuko.core.commands.world.commands.*;
import com.yuuko.core.events.extensions.MessageEvent;

public class WorldModule extends Module {
    private static final Command[] commands = new Command[]{
            new LondonUndergroundCommand(),
            new WeatherCommand(),
            new TescoCommand(),
            new CountdownCommand(),
            new NationalGeographicCommand(),
            new UKParliamentPetitionCommand(),
            new UrbanDictionaryCommand()
    };

    public WorldModule(MessageEvent e) {
        super("world", false, commands);
        new CommandExecutor(e, this);
    }

}
