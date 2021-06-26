package com.yuuko.modules.fun.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.Yuuko;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.modules.Command;
import net.dv8tion.jda.api.EmbedBuilder;

import java.time.Instant;
import java.util.Arrays;
import java.util.Random;

public class CoinFlipCommand extends Command {

    public CoinFlipCommand() {
        super("flip", Arrays.asList("-flip"));
    }

    @Override
    public void onCommand(MessageEvent context) {
        final int rng = new Random().nextInt(10000);
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle(context.i18n( "title"))
                .setDescription((rng == 0) ? context.i18n( "edge") : (rng < 5000) ? context.i18n( "heads") : context.i18n( "tails"))
                .setTimestamp(Instant.now())
                .setFooter(Yuuko.STANDARD_STRINGS.get(1) + context.getAuthor().getAsTag(), context.getAuthor().getEffectiveAvatarUrl());
        MessageDispatcher.reply(context, embed.build());
    }
}
