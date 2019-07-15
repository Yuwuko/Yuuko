package com.yuuko.core.commands.fun.commands;

import com.yuuko.core.Configuration;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.events.entity.MessageEvent;
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
        super("flip", Configuration.MODULES.get("fun"), 0, Arrays.asList("-flip"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        int rng = new Random().nextInt(6000);
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Coin Flip")
                .setDescription((rng == 0) ? responses.get(2) : (rng < 3000) ? responses.get(0) : responses.get(1))
                .setTimestamp(Instant.now())
                .setFooter(Configuration.STANDARD_STRINGS.get(1) + e.getMember().getEffectiveName(), e.getAuthor().getEffectiveAvatarUrl());
        MessageHandler.sendMessage(e, embed.build());
    }
}
