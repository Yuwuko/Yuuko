package com.yuuko.modules.fun.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.i18n.I18n;
import com.yuuko.modules.Command;
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
            EmbedBuilder embed = new EmbedBuilder().setTitle(I18n.getText(e, "invalid_input")).setDescription(I18n.getText(e, "invalid_input_desc"));
            MessageDispatcher.reply(e, embed.build());
            return;
        }

        int roll = Math.max(1, Integer.parseInt(e.getParameters()));
        EmbedBuilder embed = new EmbedBuilder().setTitle(I18n.getText(e, "roll")).setDescription(I18n.getText(e, "roll_desc").formatted(e.getAuthor().getAsTag(), roll, (ThreadLocalRandom.current().nextInt(roll) + 1)));
        MessageDispatcher.reply(e, embed.build());
    }

}
