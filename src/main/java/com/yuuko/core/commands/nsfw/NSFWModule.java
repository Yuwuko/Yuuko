package com.yuuko.core.commands.nsfw;

import com.yuuko.core.CommandExecutor;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.Module;
import com.yuuko.core.commands.nsfw.commands.EfuktCommand;
import com.yuuko.core.commands.nsfw.commands.NekoCommand;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class NSFWModule extends Module {

    public NSFWModule(MessageReceivedEvent e, String[] command) {
        super("NSFW", "moduleNSFW", true, new Command[] {
                new EfuktCommand(),
                new NekoCommand()
        });

        new CommandExecutor(e,this, command);
    }

}
