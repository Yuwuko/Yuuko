package com.yuuko.core.commands.world;

import com.yuuko.core.CommandExecutor;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.Module;
import com.yuuko.core.commands.world.commands.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class WorldModule extends Module {

    public WorldModule(MessageReceivedEvent e, String[] command) {
        super("World", "moduleWorld", false, new Command[]{
                new LondonUndergroundCommand(),
                new WeatherCommand(),
                new TescoCommand(),
                new CountdownCommand(),
                new NationalGeographicCommand()
        });

        new CommandExecutor(e,this, command);
    }

}
