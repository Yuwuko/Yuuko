package com.yuuko.core.commands.fun.commands;

import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.fun.FunModule;
import com.yuuko.core.events.entity.MessageEvent;

import java.util.Arrays;

public class SpoilerifyCommand extends Command {

    public SpoilerifyCommand() {
        super("spoilerify", FunModule.class, 1, Arrays.asList("-spoilerify <string>"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        String[] characters = e.getParameters().replace("`", "").split("");
        StringBuilder spoiler = new StringBuilder();

        spoiler.append("`");
        for(String character: characters) {
            spoiler.append("||");
            spoiler.append(character);
            spoiler.append("||");
        }
        spoiler.append("`");

        MessageHandler.sendMessage(e, spoiler.toString());
    }
}
