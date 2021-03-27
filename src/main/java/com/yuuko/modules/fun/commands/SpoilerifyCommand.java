package com.yuuko.modules.fun.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.modules.Command;

import java.util.Arrays;

public class SpoilerifyCommand extends Command {

    public SpoilerifyCommand() {
        super("spoilerify", 1, -1L, Arrays.asList("-spoilerify <string>"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) throws Exception {
        StringBuilder spoiler = new StringBuilder();
        for(char character: e.getParameters().toCharArray()) {
            spoiler.append("||").append(character).append("||");
        }

        MessageDispatcher.reply(e, "`" + spoiler.toString() + "`");
    }
}
