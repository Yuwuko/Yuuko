package com.yuuko.core.commands.moderation;

import com.yuuko.core.CommandExecutor;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.Module;
import com.yuuko.core.commands.moderation.commands.BanCommand;
import com.yuuko.core.commands.moderation.commands.KickCommand;
import com.yuuko.core.commands.moderation.commands.MuteCommand;
import com.yuuko.core.commands.moderation.commands.NukeCommand;
import com.yuuko.core.events.entity.MessageEvent;

import java.util.Arrays;
import java.util.List;

public class ModerationModule extends Module {
    private static final List<Command> commands = Arrays.asList(
            new NukeCommand(),
            new MuteCommand(),
            new BanCommand(),
            new KickCommand()
    );

    public ModerationModule(MessageEvent e) {
        super("moderation", false, commands);
        if(e != null) {
            new CommandExecutor(e.setModule(this));
        }
    }

}
