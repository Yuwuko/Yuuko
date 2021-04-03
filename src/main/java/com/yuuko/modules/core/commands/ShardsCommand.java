package com.yuuko.modules.core.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.Yuuko;
import com.yuuko.database.function.ShardFunctions;
import com.yuuko.entity.Shard;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.modules.Command;
import com.yuuko.modules.audio.handlers.AudioManager;
import lavalink.client.io.LavalinkSocket;
import net.dv8tion.jda.api.EmbedBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Arrays;

public class ShardsCommand extends Command {

    public ShardsCommand() {
        super("shards", 0, -1L, Arrays.asList("-shards"), false, null);
    }

    @Override
    public void onCommand(MessageEvent context) throws Exception {
        EmbedBuilder shardEmbed = new EmbedBuilder()
                .setAuthor(context.i18n( "title").formatted(Yuuko.BOT.getName(), Yuuko.BOT.getDiscriminator()), null, Yuuko.BOT.getAvatarUrl())
                .setTimestamp(Instant.now())
                .setFooter(Yuuko.STANDARD_STRINGS.get(1) + context.getAuthor().getAsTag(), context.getAuthor().getEffectiveAvatarUrl());
        for(Shard shard : ShardFunctions.getShardStatistics()) {
            shardEmbed.addField("", context.i18n( "shard").formatted(shard.getId(), shard.getStatus(), shard.getGuildCount(), shard.getGatewayPing(), shard.getRestPing()), true);
        }

        StringBuilder nodes = new StringBuilder();
        for(LavalinkSocket socket : AudioManager.LAVALINK.getLavalink().getNodes()) {
            if(socket.getStats() != null) {
                shardEmbed.addField("", context.i18n( "lavalink").formatted(socket.getName(),
                        BigDecimal.valueOf((socket.getStats().getSystemLoad() * 100) / 100.0).setScale(2, RoundingMode.HALF_UP),
                        BigDecimal.valueOf(socket.getStats().getMemUsed() / 1000000.0).setScale(2, RoundingMode.HALF_UP),
                        socket.getStats().getPlayers(),
                        socket.getStats().getPlayingPlayers()), true);
            }
        }

        MessageDispatcher.reply(context, shardEmbed.build());
    }
}
