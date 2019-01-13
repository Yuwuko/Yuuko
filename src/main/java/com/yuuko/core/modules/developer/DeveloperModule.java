package com.yuuko.core.modules.developer;

import com.yuuko.core.CommandExecutor;
import com.yuuko.core.modules.Command;
import com.yuuko.core.modules.Module;
import com.yuuko.core.modules.developer.commands.ReloadApiCommand;
import com.yuuko.core.modules.developer.commands.SetStatusCommand;
import com.yuuko.core.modules.developer.commands.SyncServersCommand;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class DeveloperModule extends Module {

    public DeveloperModule(MessageReceivedEvent e, String[] command) {
        super("Developer", null, false, new Command[]{
                new SetStatusCommand(),
                new SyncServersCommand(),
                new ReloadApiCommand()
        });

        if(e == null || e.getAuthor().getIdLong() != 215161101460045834L) {
            return;
        }

        new CommandExecutor(e,this, command);
    }

}
