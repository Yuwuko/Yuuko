package com.yuuko.core.commands.developer;

import com.yuuko.core.CommandExecutor;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.Module;
import com.yuuko.core.commands.developer.commands.*;
import com.yuuko.core.events.entity.MessageEvent;

import java.util.Arrays;
import java.util.List;

public class DeveloperModule extends Module {
    private static final List<Command> commands = Arrays.asList(
            new SetStatusCommand(),
            new SyncGuildsCommand(),
            new ReloadApiCommand(),
            new LavalinkNodeCommand(),
            new ReloadDatabaseCommand(),
            new LogMetricsCommand()
    );

    public DeveloperModule(MessageEvent e) {
        super("developer", false, commands);

        if(e == null || e.getAuthor().getIdLong() != 215161101460045834L) {
            return;
        }

        new CommandExecutor(e.setModule(this));
    }

}
