package com.yuuko.core.modules.nsfw;

import com.yuuko.core.CommandExecutor;
import com.yuuko.core.modules.Command;
import com.yuuko.core.modules.Module;
import com.yuuko.core.modules.nsfw.commands.CommandEfukt;
import com.yuuko.core.modules.nsfw.commands.CommandNeko;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ModuleNSFW extends Module {

    public ModuleNSFW(MessageReceivedEvent e, String[] command) {
        super("NSFW", "moduleNSFW", true, new Command[] {
                new CommandEfukt(),
                new CommandNeko()
        });

        new CommandExecutor(e, command, this);
    }

}
