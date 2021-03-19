package com.yuuko.commands.fun.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.Yuuko;
import com.yuuko.commands.Command;
import com.yuuko.events.entity.MessageEvent;

import java.util.Arrays;

public class SpoilerifyCommand extends Command {

    public SpoilerifyCommand() {
        super("spoilerify", Yuuko.MODULES.get("fun"), 1, -1L, Arrays.asList("-spoilerify <string>"), false, null);
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
