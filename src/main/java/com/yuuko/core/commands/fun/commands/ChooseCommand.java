package com.yuuko.core.commands.fun.commands;

import com.yuuko.core.Config;
import com.yuuko.core.MessageDispatcher;
import com.yuuko.core.commands.Command;
import com.yuuko.core.events.entity.MessageEvent;
import net.dv8tion.jda.api.EmbedBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Arrays;
import java.util.Random;

public class ChooseCommand extends Command {

    public ChooseCommand() {
        super("choose", Config.MODULES.get("fun"), 1, -1L, Arrays.asList("-choose <choice>, <choice>..."), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        String[] commandParameters = e.getParameters().split("\\s*(,)\\s*");
        String choices = (Arrays.asList(commandParameters).toString().length() > 1024) ? Arrays.asList(commandParameters).toString().substring(0, 1021) + "..." : Arrays.asList(commandParameters).toString();

        EmbedBuilder embed = new EmbedBuilder()
                .addField("Choices (" + commandParameters.length + ")", choices, false)
                .addField("Selected", (commandParameters.length > 1) ? commandParameters[new Random().nextInt(commandParameters.length)] : commandParameters[0], true)
                .addField("Probability", new BigDecimal(100.0/commandParameters.length).setScale(2, RoundingMode.HALF_UP) + "%", true)
                .setTimestamp(Instant.now())
                .setFooter(Config.STANDARD_STRINGS.get(1) + e.getMember().getEffectiveName(), e.getAuthor().getEffectiveAvatarUrl());
        MessageDispatcher.reply(e, embed.build());
    }
}
