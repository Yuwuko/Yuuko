package com.yuuko.core.commands.media;

import com.yuuko.core.CommandExecutor;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.Module;
import com.yuuko.core.commands.media.commands.*;
import com.yuuko.core.events.entity.MessageEvent;

import java.util.Arrays;
import java.util.List;

public class MediaModule extends Module {
    private static final List<Command> commands = Arrays.asList(
            new KitsuCommand(),
            new OsuCommand(),
            new GithubCommand(),
            new LondonUndergroundCommand(),
            new WeatherCommand(),
            new TescoCommand(),
            new NationalGeographicCommand(),
            new UKParliamentPetitionCommand()
    );

    public MediaModule(MessageEvent e) {
        super("media", false, commands);
        if(e != null) {
            new CommandExecutor(e.setModule(this));
        }
    }

}
