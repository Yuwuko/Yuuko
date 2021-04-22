package com.yuuko.modules.fun.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.modules.Command;

import java.util.Arrays;

public class SpoilerifyCommand extends Command {

    public SpoilerifyCommand() {
        super("spoilerify", Arrays.asList("-spoilerify <string>"), 1);
    }

    @Override
    public void onCommand(MessageEvent context) throws Exception {
        StringBuilder spoiler = new StringBuilder();
        for(char character: context.getParameters().toCharArray()) {
            spoiler.append("||").append(character).append("||");
        }

        MessageDispatcher.reply(context, "`" + spoiler + "`");
    }
}
