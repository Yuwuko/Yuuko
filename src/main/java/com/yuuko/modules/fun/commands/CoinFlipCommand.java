package com.yuuko.modules.fun.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.Yuuko;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.i18n.I18n;
import com.yuuko.modules.Command;
import net.dv8tion.jda.api.EmbedBuilder;

import java.time.Instant;
import java.util.Arrays;
import java.util.Random;

public class CoinFlipCommand extends Command {

    public CoinFlipCommand() {
        super("flip", 0, -1L, Arrays.asList("-flip"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) throws Exception {
        final int rng = new Random().nextInt(10000);
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle(I18n.getText(e, "title"))
                .setDescription((rng == 0) ? I18n.getText(e, "edge") : (rng < 5000) ? I18n.getText(e, "heads") : I18n.getText(e, "tails"))
                .setTimestamp(Instant.now())
                .setFooter(Yuuko.STANDARD_STRINGS.get(1) + e.getAuthor().getAsTag(), e.getAuthor().getEffectiveAvatarUrl());
        MessageDispatcher.reply(e, embed.build());
    }
}
