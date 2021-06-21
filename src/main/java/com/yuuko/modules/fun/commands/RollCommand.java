package com.yuuko.modules.fun.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.modules.Command;
import com.yuuko.utilities.Sanitiser;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class RollCommand extends Command {

    public RollCommand() {
        super("roll", Arrays.asList("-roll <number>"), 1);
    }

    @Override
    public void onCommand(MessageEvent context) throws Exception {
        if(!Sanitiser.isNumeric(context.getParameters())) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(context.i18n( "invalid_input"))
                    .setDescription(context.i18n( "invalid_input_desc"));
            MessageDispatcher.reply(context, embed.build());
            return;
        }

        int roll = Math.max(1, Integer.parseInt(context.getParameters()));
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle(context.i18n( "roll"))
                .setDescription(context.i18n( "roll_desc").formatted(context.getAuthor().getAsTag(), roll, (ThreadLocalRandom.current().nextInt(roll) + 1)));
        MessageDispatcher.reply(context, embed.build());
    }

}
