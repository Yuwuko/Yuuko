package com.yuuko.core.commands.developer;

import com.yuuko.core.CommandExecutor;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.Module;
import com.yuuko.core.commands.developer.commands.*;
import com.yuuko.core.events.extensions.MessageEvent;

public class DeveloperModule extends Module {
    private static final Command[] commands = new Command[]{
            new SetStatusCommand(),
            new SyncGuildsCommand(),
            new ReloadApiCommand(),
            new LavalinkNodeCommand(),
            new ReloadDatabaseCommand()
    };

    public DeveloperModule(MessageEvent e) {
        super("Developer", null, false, commands);

        if(e == null || e.getAuthor().getIdLong() != 215161101460045834L) {
            return;
        }

        new CommandExecutor(e, this);
    }

}
