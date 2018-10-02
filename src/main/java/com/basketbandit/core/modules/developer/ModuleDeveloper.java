package com.basketbandit.core.modules.developer;

import com.basketbandit.core.CommandExecutor;
import com.basketbandit.core.modules.Command;
import com.basketbandit.core.modules.Module;
import com.basketbandit.core.modules.developer.commands.CommandAddServers;
import com.basketbandit.core.modules.developer.commands.CommandSetStatus;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ModuleDeveloper extends Module {

    public ModuleDeveloper(MessageReceivedEvent e, String[] command) {
        super("ModuleDeveloper", null, new Command[]{
                new CommandSetStatus(),
                new CommandAddServers()
        });

        if(e != null && command != null) {
            if(e.getAuthor().getIdLong() == 215161101460045834L) {
                new CommandExecutor(e, command, this);
            }
        }

    }

}
