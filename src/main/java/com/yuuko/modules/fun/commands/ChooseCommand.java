package com.yuuko.modules.fun.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.Yuuko;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.modules.Command;
import net.dv8tion.jda.api.EmbedBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Arrays;
import java.util.Random;

public class ChooseCommand extends Command {

    public ChooseCommand() {
        super("choose", Arrays.asList("-choose <choice>, <choice>..."), 1);
    }

    @Override
    public void onCommand(MessageEvent context) {
        String[] commandParameters = context.getParameters().split("\\s*(,)\\s*");
        String choices = (Arrays.asList(commandParameters).toString().length() > 1024) ? Arrays.asList(commandParameters).toString().substring(0, 1021) + "..." : Arrays.asList(commandParameters).toString();
        EmbedBuilder embed = new EmbedBuilder()
                .addField(context.i18n( "choices").formatted(commandParameters.length), choices, false)
                .addField(context.i18n( "selected"), (commandParameters.length > 1) ? commandParameters[new Random().nextInt(commandParameters.length)] : commandParameters[0], true)
                .addField(context.i18n( "probability"), new BigDecimal(100.0/commandParameters.length).setScale(2, RoundingMode.HALF_UP) + "%", true)
                .setTimestamp(Instant.now())
                .setFooter(Yuuko.STANDARD_STRINGS.get(1) + context.getAuthor().getAsTag(), context.getAuthor().getEffectiveAvatarUrl());
        MessageDispatcher.reply(context, embed.build());
    }
}
