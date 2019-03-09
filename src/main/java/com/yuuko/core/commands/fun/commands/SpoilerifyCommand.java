package com.yuuko.core.commands.fun.commands;

import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.fun.FunModule;
import com.yuuko.core.utilities.MessageHandler;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class SpoilerifyCommand extends Command {

    public SpoilerifyCommand() {
        super("spoilerify", FunModule.class, 1, new String[]{"-spoilerify <string>"}, false, null);
    }

    @Override
    public void onCommand(MessageReceivedEvent e, String[] command) {
        String[] characters = command[1].replace("`", "").split("");
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
