package com.yuuko.core.commands.developer;

import com.yuuko.core.CommandExecutor;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.Module;
import com.yuuko.core.commands.developer.commands.LavalinkNodeCommand;
import com.yuuko.core.commands.developer.commands.ReloadApiCommand;
import com.yuuko.core.commands.developer.commands.SetStatusCommand;
import com.yuuko.core.commands.developer.commands.SyncServersCommand;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class DeveloperModule extends Module {

    public DeveloperModule(MessageReceivedEvent e, String[] command) {
        super("Developer", null, false, new Command[]{
                new SetStatusCommand(),
                new SyncServersCommand(),
                new ReloadApiCommand(),
                new LavalinkNodeCommand()
        });

        if(e == null || e.getAuthor().getIdLong() != 215161101460045834L) {
            return;
        }

        new CommandExecutor(e,this, command);
    }

}
