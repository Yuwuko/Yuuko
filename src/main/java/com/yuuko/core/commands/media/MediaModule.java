package com.yuuko.core.commands.media;

import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.Module;
import com.yuuko.core.commands.media.commands.*;

import java.util.Map;

import static java.util.Map.entry;

public class MediaModule extends Module {
    private static final Map<String, Class<? extends Command>> commands = Map.ofEntries(
            entry("kitsu", KitsuCommand.class),
            entry("osu", OsuCommand.class),
            entry("github", GithubCommand.class),
            entry("underground", LondonUndergroundCommand.class),
            entry("weather", WeatherCommand.class),
            entry("tesco", TescoCommand.class),
            entry("natgeo", NationalGeographicCommand.class),
            entry("petition", UKParliamentPetitionCommand.class),
            entry("urban", UrbanDictionaryCommand.class)
    );

    public MediaModule() {
        super("media", false, commands);
    }

}
