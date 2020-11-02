package com.yuuko.core.commands.developer.commands;

import com.yuuko.core.Configuration;
import com.yuuko.core.commands.Command;
import com.yuuko.core.events.entity.MessageEvent;
import lavalink.client.io.jda.JdaLink;

import java.util.Arrays;

public class ShutdownCommand extends Command {

    public ShutdownCommand() {
        super("shutdown", Configuration.MODULES.get("developer"), 1, -1L, Arrays.asList("-shutdown"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        for(JdaLink link: Configuration.LAVALINK.getLavalink().getLinks()) {
            link.destroy();
        }
        Configuration.SHARD_MANAGER.shutdown();
        System.exit(0);
    }
}
