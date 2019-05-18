package com.yuuko.core.commands.media;

import com.yuuko.core.CommandExecutor;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.Module;
import com.yuuko.core.commands.media.commands.GithubCommand;
import com.yuuko.core.commands.media.commands.KitsuCommand;
import com.yuuko.core.commands.media.commands.OsuCommand;
import com.yuuko.core.events.extensions.MessageEvent;

import java.util.Arrays;
import java.util.List;

public class MediaModule extends Module {
    private static final List<Command> commands = Arrays.asList(
            new KitsuCommand(),
            new OsuCommand(),
            new GithubCommand()
    );

    public MediaModule(MessageEvent e) {
        super("media", false, commands);
        new CommandExecutor(e, this);
    }

}
