package com.yuuko.core.commands.moderation;

import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.Module;
import com.yuuko.core.commands.moderation.commands.BanCommand;
import com.yuuko.core.commands.moderation.commands.KickCommand;
import com.yuuko.core.commands.moderation.commands.MuteCommand;
import com.yuuko.core.commands.moderation.commands.NukeCommand;

import java.util.Map;

import static java.util.Map.entry;

public class ModerationModule extends Module {
    private static final Map<String, Class<? extends Command>> commands = Map.ofEntries(
            entry("nuke", NukeCommand.class),
            entry("mute", MuteCommand.class),
            entry("ban", BanCommand.class),
            entry("kick", KickCommand.class)
    );

    public ModerationModule() {
        super("moderation", false, commands);
    }

}
