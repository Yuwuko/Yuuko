package com.yuuko.core.commands.animal;

import com.yuuko.core.CommandExecutor;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.Module;
import com.yuuko.core.commands.animal.commands.BirdCommand;
import com.yuuko.core.commands.animal.commands.CatCommand;
import com.yuuko.core.commands.animal.commands.DogCommand;
import com.yuuko.core.commands.animal.commands.FoxCommand;
import com.yuuko.core.events.entity.MessageEvent;

import java.util.Arrays;
import java.util.List;

public class AnimalModule extends Module {
    private static final List<Command> commands = Arrays.asList(
            new CatCommand(),
            new FoxCommand(),
            new DogCommand(),
            new BirdCommand()
    );

    public AnimalModule(MessageEvent e) {
        super("animal", false, commands);
        new CommandExecutor(e, this);
    }
}
