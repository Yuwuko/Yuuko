package com.yuuko.core.commands.interaction;

import com.yuuko.core.CommandExecutor;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.Module;
import com.yuuko.core.commands.interaction.commands.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

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

    public InteractionModule(MessageReceivedEvent e, String[] command) {
        super("Interaction", "moduleInteraction", false, commands);
        new CommandExecutor(e,this, command);
    }

}
