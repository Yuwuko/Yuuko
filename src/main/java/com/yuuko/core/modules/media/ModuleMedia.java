package com.yuuko.core.modules.media;

import com.yuuko.core.CommandExecutor;
import com.yuuko.core.modules.Command;
import com.yuuko.core.modules.Module;
import com.yuuko.core.modules.media.commands.CommandKitsu;
import com.yuuko.core.modules.media.commands.CommandOsu;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ModuleMedia extends Module {

    public ModuleMedia(MessageReceivedEvent e, String[] command) {
        super("Media", "moduleMedia", false, new Command[]{
                new CommandKitsu(),
                new CommandOsu()
        });

        new CommandExecutor(e, command, this);
    }

}
