package com.yuuko.core.modules.fun.commands;

import com.yuuko.core.modules.Command;
import com.yuuko.core.modules.fun.FunModule;
import com.yuuko.core.utilities.MessageHandler;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Random;

public class ChooseCommand extends Command {

    public ChooseCommand() {
        super("choose", FunModule.class, 1, new String[]{"-choose [choice], [choice]..."}, null);
    }

    @Override
    public void executeCommand(MessageReceivedEvent e, String[] command) {
        String[] commandParameters = command[1].split("\\s*(,)\\s*");

        EmbedBuilder embed = new EmbedBuilder().setTitle((commandParameters.length > 1) ? commandParameters[new Random().nextInt(commandParameters.length)] : commandParameters[0]);
        MessageHandler.sendMessage(e, embed.build());
    }
}
