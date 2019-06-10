package com.yuuko.core.commands.nsfw;

import com.yuuko.core.CommandExecutor;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.Module;
import com.yuuko.core.commands.nsfw.commands.EfuktCommand;
import com.yuuko.core.commands.nsfw.commands.NekoCommand;
import com.yuuko.core.commands.nsfw.commands.UrbanDictionaryCommand;
import com.yuuko.core.events.entity.MessageEvent;

import java.util.Arrays;
import java.util.List;

public class NsfwModule extends Module {
    private static final List<Command> commands = Arrays.asList(
            new EfuktCommand(),
            new NekoCommand(),
            new UrbanDictionaryCommand()
    );

    public NsfwModule(MessageEvent e) {
        super("nsfw", true, commands);
        new CommandExecutor(e, this);
    }

}
