package com.yuuko.commands.fun.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.commands.Command;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.utilities.Sanitiser;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class RollCommand extends Command {

    public RollCommand() {
        super("roll", 1, -1L, Arrays.asList("-roll <number>"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) throws Exception {
        if(!Sanitiser.isNumeric(e.getParameters())) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Input.").setDescription("Input must be a non-negative numeric value.");
            MessageDispatcher.reply(e, embed.build());
            return;
        }

        int roll = Math.max(1, Integer.parseInt(e.getParameters()));
        EmbedBuilder embed = new EmbedBuilder().setTitle("Roll").setDescription("`" + e.getAuthor().getAsTag() + "` rolled a `d" + roll + "` and got `" + (ThreadLocalRandom.current().nextInt(roll) + 1) + "`.");
        MessageDispatcher.reply(e, embed.build());
    }

}
