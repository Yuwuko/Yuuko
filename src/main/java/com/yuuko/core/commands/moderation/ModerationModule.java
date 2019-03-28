package com.yuuko.core.commands.moderation;

import com.yuuko.core.CommandExecutor;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.Module;
import com.yuuko.core.commands.moderation.commands.BanCommand;
import com.yuuko.core.commands.moderation.commands.KickCommand;
import com.yuuko.core.commands.moderation.commands.MuteCommand;
import com.yuuko.core.commands.moderation.commands.NukeCommand;
import com.yuuko.core.events.extensions.MessageEvent;

public class ModerationModule extends Module {
    private static final Command[] commands = new Command[]{
            new NukeCommand(),
            new MuteCommand(),
            new BanCommand(),
            new KickCommand()
    };

    public ModerationModule(MessageEvent e) {
        super("Moderation", false, commands);
        new CommandExecutor(e, this);
    }

}
