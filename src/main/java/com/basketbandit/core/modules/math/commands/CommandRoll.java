package com.basketbandit.core.modules.math.commands;

import com.basketbandit.core.modules.Command;
import com.basketbandit.core.utils.MessageHandler;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.Random;

public class CommandRoll extends Command {

    public CommandRoll() {
        super("roll", "com.basketbandit.core.modules.math.ModuleMath", 1, new String[]{"-roll [number]", "-roll 00"}, null);
    }

    @Override
    public void executeCommand(MessageReceivedEvent e, String[] command) {
        int num;
        int rollNum = 0;

        // I assume someone will try to roll something that isn't a number.
        if(command[1].matches("[0-9]+")) {
            rollNum = Integer.parseInt(command[1]);
        } else if(command[1].contains("-")) {
            MessageHandler.sendMessage(e, "Sorry " + e.getAuthor().getAsMention() +", you can't roll on a negative number or anything that isn't a number");
            return;
        }

        if(command[1].equals("00")) {
            num = (new Random().nextInt(10) + 1) * 10;
        } else {
            num = new Random().nextInt(rollNum) + 1;
        }

        EmbedBuilder embed = new EmbedBuilder()
                .setColor(Color.DARK_GRAY)
                .setAuthor(e.getGuild().getMemberById(e.getAuthor().getIdLong()).getEffectiveName() + " rolled a " + num + ".", null, e.getAuthor().getAvatarUrl());

        if(num != 0) {
            MessageHandler.sendMessage(e, embed.build());
        } else {
            MessageHandler.sendMessage(e, "Sorry " + e.getAuthor().getAsMention() +", something has gone wrong...");
        }

    }

}
