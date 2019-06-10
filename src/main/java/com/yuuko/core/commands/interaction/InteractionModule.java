package com.yuuko.core.commands.interaction;

import com.yuuko.core.CommandExecutor;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.Module;
import com.yuuko.core.commands.interaction.commands.*;
import com.yuuko.core.events.entity.MessageEvent;

import java.util.Arrays;
import java.util.List;

public class InteractionModule extends Module {
    private static final List<Command> commands = Arrays.asList(
            new PokeCommand(),
            new HugCommand(),
            new AttackCommand(),
            new BiteCommand(),
            new AngryCommand(),
            new CryCommand(),
            new LaughCommand(),
            new PoutCommand(),
            new ShrugCommand(),
            new SleepCommand(),
            new TickleCommand(),
            new KissCommand(),
            new PetCommand(),
            new BlushCommand(),
            new DanceCommand(),
            new PatCommand(),
            new KillCommand()
    );

    public InteractionModule(MessageEvent e) {
        super("interaction", false, commands);
        new CommandExecutor(e, this);
    }

}
