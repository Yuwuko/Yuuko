package com.yuuko.core.commands.animal;

import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.Module;
import com.yuuko.core.commands.animal.commands.BirdCommand;
import com.yuuko.core.commands.animal.commands.CatCommand;
import com.yuuko.core.commands.animal.commands.DogCommand;
import com.yuuko.core.commands.animal.commands.FoxCommand;

import java.util.Map;

import static java.util.Map.entry;

public class AnimalModule extends Module {
    private static final Map<String, Class<? extends Command>> commands = Map.ofEntries(
            entry("cat", CatCommand.class),
            entry("fox", FoxCommand.class),
            entry("dog", DogCommand.class),
            entry("bird", BirdCommand.class)
    );

    public AnimalModule() {
        super("animal", false, commands);
    }
}
