package com.yuuko.core.commands.interaction;

import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.Module;
import com.yuuko.core.commands.interaction.commands.*;

import java.util.Map;

import static java.util.Map.entry;

public class InteractionModule extends Module {
    private static final Map<String, Class<? extends Command>> commands = Map.ofEntries(
            entry("poke", PokeCommand.class),
            entry("hug", HugCommand.class),
            entry("attack", AttackCommand.class),
            entry("bite", BiteCommand.class),
            entry("angry", AngryCommand.class),
            entry("cry", CryCommand.class),
            entry("laugh", LaughCommand.class),
            entry("pout", PoutCommand.class),
            entry("shrug", ShrugCommand.class),
            entry("sleep", SleepCommand.class),
            entry("tickle", TickleCommand.class),
            entry("kiss", KissCommand.class),
            entry("pet", PetCommand.class),
            entry("blush", BlushCommand.class),
            entry("dance", DanceCommand.class),
            entry("pat", PatCommand.class),
            entry("kill", KillCommand.class)
    );

    public InteractionModule() {
        super("interaction", false, commands);
    }

}
