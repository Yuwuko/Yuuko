package com.yuuko.core.commands.developer.commands;

import com.yuuko.core.Config;
import com.yuuko.core.commands.Command;
import com.yuuko.core.events.entity.MessageEvent;
import lavalink.client.io.Link;

import java.util.Arrays;

public class ShutdownCommand extends Command {

    public ShutdownCommand() {
        super("shutdown", Config.MODULES.get("developer"), 0, -1L, Arrays.asList("-shutdown"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        Config.LAVALINK.getLavalink().getLinks().forEach(Link::destroy);
        Config.SHARD_MANAGER.shutdown();
        System.exit(0);
    }
}
