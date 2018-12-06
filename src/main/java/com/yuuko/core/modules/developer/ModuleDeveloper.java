package com.yuuko.core.modules.developer;

import com.yuuko.core.CommandExecutor;
import com.yuuko.core.modules.Command;
import com.yuuko.core.modules.Module;
import com.yuuko.core.modules.developer.commands.CommandAddServers;
import com.yuuko.core.modules.developer.commands.CommandReloadApi;
import com.yuuko.core.modules.developer.commands.CommandSetStatus;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ModuleDeveloper extends Module {

    public ModuleDeveloper(MessageReceivedEvent e, String[] command) {
        super("ModuleDeveloper", null, new Command[]{
                new CommandSetStatus(),
                new CommandAddServers(),
                new CommandReloadApi()
        });

        if(e != null && command != null) {
            if(e.getAuthor().getIdLong() == 215161101460045834L) {
                new CommandExecutor(e, command, this);
            }
        }

    }

}
