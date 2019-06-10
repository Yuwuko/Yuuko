package com.yuuko.core.commands.utility;

import com.yuuko.core.CommandExecutor;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.Module;
import com.yuuko.core.commands.utility.commands.*;
import com.yuuko.core.events.entity.MessageEvent;

import java.util.Arrays;
import java.util.List;

public class UtilityModule extends Module {
    private static final List<Command> commands = Arrays.asList(
            new UserCommand(),
            new GuildCommand(),
            new BindCommand(),
            new ChannelCommand(),
            new AvatarCommand(),
            new RolesCommand(),
            new PingCommand(),
            new ReactionRoleCommand()
    );

    public UtilityModule(MessageEvent e) {
        super("utility", false, commands);
        new CommandExecutor(e, this);
    }

}