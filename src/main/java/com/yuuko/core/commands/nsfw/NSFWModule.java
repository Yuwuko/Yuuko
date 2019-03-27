package com.yuuko.core.commands.nsfw;

import com.yuuko.core.CommandExecutor;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.Module;
import com.yuuko.core.commands.nsfw.commands.EfuktCommand;
import com.yuuko.core.commands.nsfw.commands.NekoCommand;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class NSFWModule extends Module {
    private static final Command[] commands = new Command[]{
            new EfuktCommand(),
            new NekoCommand()
    };

    public NSFWModule(MessageReceivedEvent e, String[] command) {
        super("NSFW", "moduleNSFW", true, commands);
        new CommandExecutor(e,this, command);
    }

}
