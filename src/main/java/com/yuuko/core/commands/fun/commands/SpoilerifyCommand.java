package com.yuuko.core.commands.fun.commands;

import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.fun.FunModule;
import com.yuuko.core.events.extensions.MessageEvent;

public class SpoilerifyCommand extends Command {

    public SpoilerifyCommand() {
        super("spoilerify", FunModule.class, 1, new String[]{"-spoilerify <string>"}, false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        String[] characters = e.getCommand()[1].replace("`", "").split("");
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
