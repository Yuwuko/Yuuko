package com.yuuko.core.modules.world.commands;

import com.yuuko.core.modules.Command;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandTesco extends Command {

    public CommandTesco() {
        super("tesco", "com.yuuko.core.modules.world.ModuleWorld", 1, new String[]{"-tesco [product]"}, null);
    }

    @Override
    public void executeCommand(MessageReceivedEvent e, String[] command) {

    }
}
