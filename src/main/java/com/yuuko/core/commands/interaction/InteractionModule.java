package com.yuuko.core.commands.interaction;

import com.yuuko.core.CommandExecutor;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.Module;
import com.yuuko.core.commands.interaction.commands.*;
import com.yuuko.core.events.extensions.MessageEvent;

public class InteractionModule extends Module {
    private static final Command[] commands = new Command[]{
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
            new BlushCommand()
    };

    public InteractionModule(MessageEvent e) {
        super("Interaction", false, commands);
        new CommandExecutor(e, this);
    }

}
