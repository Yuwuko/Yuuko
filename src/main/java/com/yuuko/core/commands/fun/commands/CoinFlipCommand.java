package com.yuuko.core.commands.fun.commands;

import com.yuuko.core.Configuration;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.fun.FunModule;
import com.yuuko.core.events.extensions.MessageEvent;
import net.dv8tion.jda.core.EmbedBuilder;

import java.time.Instant;
import java.util.Random;

public class CoinFlipCommand extends Command {

    private static final String[] responses = new String[]{
            "Heads",
            "Tails",
            "Edge"
    };

    public CoinFlipCommand() {
        super("flip", FunModule.class, 0, new String[]{"-flip"}, false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        int rng = new Random().nextInt(6000);
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Coin Flip")
                .setDescription((rng == 0) ? responses[2] : (rng < 3000) ? responses[0] : responses[1])
                .setTimestamp(Instant.now())
                .setFooter(Configuration.STANDARD_STRINGS[1] + e.getMember().getEffectiveName(), e.getAuthor().getEffectiveAvatarUrl());
        MessageHandler.sendMessage(e, embed.build());
    }
}
