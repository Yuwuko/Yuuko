package com.yuuko.core.commands.fun.commands;

import com.yuuko.core.Configuration;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.fun.FunModule;
import com.yuuko.core.utilities.MessageHandler;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.time.Instant;
import java.util.Random;

public class ChooseCommand extends Command {

    public ChooseCommand() {
        super("choose", FunModule.class, 1, new String[]{"-choose <choice>, <choice>..."}, false, null);
    }

    @Override
    public void onCommand(MessageReceivedEvent e, String[] command) {
        String[] commandParameters = command[1].split("\\s*(,)\\s*");

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Choose")
                .setDescription((commandParameters.length > 1) ? commandParameters[new Random().nextInt(commandParameters.length)] : commandParameters[0])
                .setTimestamp(Instant.now())
                .setFooter(Configuration.STANDARD_STRINGS[1] + e.getMember().getEffectiveName(), e.getAuthor().getEffectiveAvatarUrl());
        MessageHandler.sendMessage(e, embed.build());
    }
}
