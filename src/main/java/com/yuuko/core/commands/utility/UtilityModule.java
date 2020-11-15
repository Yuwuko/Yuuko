package com.yuuko.core.commands.utility;

import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.Module;
import com.yuuko.core.commands.utility.commands.*;

import java.util.Map;

import static java.util.Map.entry;

public class UtilityModule extends Module {
    private static final Map<String, Class<? extends Command>> commands = Map.ofEntries(
            entry("user", UserCommand.class),
            entry("guild", GuildCommand.class),
            entry("avatar", AvatarCommand.class),
            entry("roles", RolesCommand.class),
            entry("ping", PingCommand.class),
            entry("reactrole", ReactionRoleCommand.class)
    );

    public UtilityModule() {
        super("utility", false, commands);
    }
}