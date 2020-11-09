package com.yuuko.core.commands.fun.commands;

import com.yuuko.core.Configuration;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.events.entity.MessageEvent;

import java.util.Arrays;

public class SpoilerifyCommand extends Command {

    public SpoilerifyCommand() {
        super("spoilerify", Configuration.MODULES.get("fun"), 1, -1L, Arrays.asList("-spoilerify <string>"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        StringBuilder spoiler = new StringBuilder();
        for(char character: e.getParameters().toCharArray()) {
            spoiler.append("||").append(character).append("||");
        }

        MessageHandler.sendMessage(e, "`" + spoiler.toString() + "`");
    }
}
