package com.yuuko.core.commands.fun.commands;

import com.yuuko.core.Configuration;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.fun.FunModule;
import com.yuuko.core.events.entity.MessageEvent;
import net.dv8tion.jda.core.EmbedBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Arrays;
import java.util.Random;

public class ChooseCommand extends Command {

    public ChooseCommand() {
        super("choose", FunModule.class, 1, Arrays.asList("-choose <choice>, <choice>..."), false, null);
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
                .setFooter(Configuration.STANDARD_STRINGS.get(1) + e.getMember().getEffectiveName(), e.getAuthor().getEffectiveAvatarUrl());
        MessageHandler.sendMessage(e, embed.build());
    }
}
