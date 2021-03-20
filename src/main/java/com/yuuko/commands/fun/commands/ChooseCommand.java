package com.yuuko.commands.fun.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.Yuuko;
import com.yuuko.commands.Command;
import com.yuuko.events.entity.MessageEvent;
import net.dv8tion.jda.api.EmbedBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Arrays;
import java.util.Random;

public class ChooseCommand extends Command {

    public ChooseCommand() {
        super("choose", 1, -1L, Arrays.asList("-choose <choice>, <choice>..."), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) throws Exception {
        String[] commandParameters = e.getParameters().split("\\s*(,)\\s*");
        String choices = (Arrays.asList(commandParameters).toString().length() > 1024) ? Arrays.asList(commandParameters).toString().substring(0, 1021) + "..." : Arrays.asList(commandParameters).toString();
        EmbedBuilder embed = new EmbedBuilder()
                .addField("Choices (" + commandParameters.length + ")", choices, false)
                .addField("Selected", (commandParameters.length > 1) ? commandParameters[new Random().nextInt(commandParameters.length)] : commandParameters[0], true)
                .addField("Probability", new BigDecimal(100.0/commandParameters.length).setScale(2, RoundingMode.HALF_UP) + "%", true)
                .setTimestamp(Instant.now())
                .setFooter(Yuuko.STANDARD_STRINGS.get(1) + e.getAuthor().getAsTag(), e.getAuthor().getEffectiveAvatarUrl());
        MessageDispatcher.reply(e, embed.build());
    }
}
