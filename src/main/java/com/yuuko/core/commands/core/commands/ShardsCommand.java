package com.yuuko.core.commands.core.commands;

import com.yuuko.core.MessageDispatcher;
import com.yuuko.core.Yuuko;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.audio.handlers.AudioManager;
import com.yuuko.core.database.function.ShardFunctions;
import com.yuuko.core.entity.Shard;
import com.yuuko.core.events.entity.MessageEvent;
import lavalink.client.io.LavalinkSocket;
import net.dv8tion.jda.api.EmbedBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Arrays;

public class ShardsCommand extends Command {

    public ShardsCommand() {
        super("shards", Yuuko.MODULES.get("core"), 0, -1L, Arrays.asList("-shards"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) throws Exception {
        EmbedBuilder shardEmbed = new EmbedBuilder()
                .setAuthor(Yuuko.BOT.getName() + "#" + Yuuko.BOT.getDiscriminator() + " - Shards", null, Yuuko.BOT.getAvatarUrl())
                .setTimestamp(Instant.now())
                .setFooter(Yuuko.STANDARD_STRINGS.get(1) + e.getMember().getEffectiveName(), e.getAuthor().getEffectiveAvatarUrl());

        StringBuilder shards = new StringBuilder();
        for(Shard shard : ShardFunctions.getShardStatistics()) {
            shards.append("**Yuuko #").append(shard.getId()).append("**")
                    .append("\n").append("Status: ").append(shard.getStatus())
                    .append("\n").append("Guilds: ").append(shard.getGuildCount())
                    .append("\n").append("Gateway Ping: ").append(shard.getGatewayPing()).append("ms")
                    .append("\n").append("Rest Ping: ").append(shard.getRestPing()).append("ms");
            shardEmbed.addField("", shards.toString(), true);
            shards = new StringBuilder();
        }

        StringBuilder nodes = new StringBuilder();
        for(LavalinkSocket socket : AudioManager.LAVALINK.getLavalink().getNodes()) {
            if(socket.getStats() != null) {
                nodes.append("**Yuuko-").append(socket.getName()).append("**")
                        .append("\n").append("System Load: ").append(BigDecimal.valueOf((socket.getStats().getSystemLoad() * 100) / 100.0).setScale(2, RoundingMode.HALF_UP)).append("%")
                        .append("\n").append("Memory Used: ").append(BigDecimal.valueOf(socket.getStats().getMemUsed() / 1000000.0).setScale(2, RoundingMode.HALF_UP)).append("MB")
                        .append("\n").append("Players: ").append(socket.getStats().getPlayers())
                        .append("\n").append("Active: ").append(socket.getStats().getPlayingPlayers());
                shardEmbed.addField("", nodes.toString(), true);
                nodes = new StringBuilder();
            }
        }

        MessageDispatcher.reply(e, shardEmbed.build());
    }
}
