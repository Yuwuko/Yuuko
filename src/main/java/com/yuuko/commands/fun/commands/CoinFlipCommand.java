package com.yuuko.commands.fun.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.Yuuko;
import com.yuuko.commands.Command;
import com.yuuko.events.entity.MessageEvent;
import net.dv8tion.jda.api.EmbedBuilder;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class CoinFlipCommand extends Command {
    private static final List<String> responses = Arrays.asList(
            "Heads",
            "Tails",
            "Edge"
    );

    public CoinFlipCommand() {
        super("flip", 0, -1L, Arrays.asList("-flip"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) throws Exception {
        final int rng = new Random().nextInt(10000);
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Coin Flip")
                .setDescription((rng == 0) ? responses.get(2) : (rng < 5000) ? responses.get(0) : responses.get(1))
                .setTimestamp(Instant.now())
                .setFooter(Yuuko.STANDARD_STRINGS.get(1) + e.getAuthor().getAsTag(), e.getAuthor().getEffectiveAvatarUrl());
        MessageDispatcher.reply(e, embed.build());
    }
}
