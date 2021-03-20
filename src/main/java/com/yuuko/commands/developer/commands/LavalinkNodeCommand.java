package com.yuuko.commands.developer.commands;

import com.yuuko.commands.Command;
import com.yuuko.commands.audio.handlers.AudioManager;
import com.yuuko.events.entity.MessageEvent;

import java.net.URI;
import java.util.Arrays;

public class LavalinkNodeCommand extends Command {

    public LavalinkNodeCommand() {
        super("lavalink", 2, -1L, Arrays.asList("-lavalink <action> <node> <secret>"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) throws Exception {
        String[] params = e.getParameters().split("\\s+", 3);
        switch(params[0]) {
            case "add" -> AudioManager.LAVALINK.getLavalink().addNode(URI.create(params[1]), params[2]);
            case "remove" -> AudioManager.LAVALINK.getLavalink().removeNode(Integer.parseInt(params[1]));
            default -> {}
        }
    }
}
