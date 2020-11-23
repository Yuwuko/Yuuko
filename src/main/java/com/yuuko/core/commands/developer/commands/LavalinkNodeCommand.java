package com.yuuko.core.commands.developer.commands;

import com.yuuko.core.Config;
import com.yuuko.core.commands.Command;
import com.yuuko.core.events.entity.MessageEvent;

import java.net.URI;
import java.util.Arrays;

public class LavalinkNodeCommand extends Command {

    public LavalinkNodeCommand() {
        super("lavalink", Config.MODULES.get("developer"), 2, -1L, Arrays.asList("-lavalink <action> <node> <secret>"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        try {
            String[] params = e.getParameters().split("\\s+", 3);
            if(params[0].equals("add")) {
                Config.LAVALINK.getLavalink().addNode(URI.create(params[1]), params[2]);
            } else if(params[0].equals("remove")) {
                Config.LAVALINK.getLavalink().removeNode(Integer.parseInt(params[1]));
            }
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
        }
    }
}
