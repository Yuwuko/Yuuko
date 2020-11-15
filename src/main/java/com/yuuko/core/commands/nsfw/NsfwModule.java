package com.yuuko.core.commands.nsfw;

import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.Module;
import com.yuuko.core.commands.nsfw.commands.EfuktCommand;
import com.yuuko.core.commands.nsfw.commands.NekoCommand;
import com.yuuko.core.commands.nsfw.commands.Rule34Command;

import java.util.Map;

import static java.util.Map.entry;

public class NsfwModule extends Module {
    private static final Map<String, Class<? extends Command>> commands = Map.ofEntries(
            entry("efukt", EfuktCommand.class),
            entry("neko", NekoCommand.class),
            entry("rule34", Rule34Command.class)
    );

    public NsfwModule() {
        super("nsfw", true, commands);
    }

}
