package com.yuuko.core.commands.developer;

import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.Module;
import com.yuuko.core.commands.developer.commands.*;

import java.util.Map;

import static java.util.Map.entry;

public class DeveloperModule extends Module {
    private static final Map<String, Class<? extends Command>> commands = Map.ofEntries(
            entry("setstatus", SetStatusCommand.class),
            entry("syncguilds", SyncGuildsCommand.class),
            entry("reloadapi", ReloadApiCommand.class),
            entry("lavalink", LavalinkNodeCommand.class),
            entry("logmetrics", LogMetricsCommand.class),
            entry("shutdown", ShutdownCommand.class)
    );

    public DeveloperModule() {
        super("developer", false, commands);
    }

}
