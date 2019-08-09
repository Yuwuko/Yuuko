package com.yuuko.core.commands.developer;

import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.Module;
import com.yuuko.core.commands.developer.commands.*;

import java.util.Arrays;
import java.util.List;

public class DeveloperModule extends Module {
    private static final List<Command> commands = Arrays.asList(
            new SetStatusCommand(),
            new SyncGuildsCommand(),
            new ReloadApiCommand(),
            new LavalinkNodeCommand(),
            new ReloadDatabaseCommand(),
            new LogMetricsCommand(),
            new ShutdownCommand()
    );

    public DeveloperModule() {
        super("developer", false, commands);
    }

}
