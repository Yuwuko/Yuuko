package com.yuuko.core.modules.interaction;

import com.yuuko.core.CommandExecutor;
import com.yuuko.core.modules.Command;
import com.yuuko.core.modules.Module;
import com.yuuko.core.modules.interaction.commands.AttackCommand;
import com.yuuko.core.modules.interaction.commands.BiteCommand;
import com.yuuko.core.modules.interaction.commands.HugCommand;
import com.yuuko.core.modules.interaction.commands.PokeCommand;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class InteractionModule extends Module {

    public InteractionModule(MessageReceivedEvent e, String[] command) {
        super("Interaction", "moduleInteraction", false, new Command[]{
                new PokeCommand(),
                new HugCommand(),
                new AttackCommand(),
                new BiteCommand()
        });

        new CommandExecutor(e, command, this);
    }

}