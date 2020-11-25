package com.yuuko.core.commands.core.commands;

import com.yuuko.core.Config;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
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
        super("shards", Config.MODULES.get("core"), 0, -1L, Arrays.asList("-shards"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        EmbedBuilder shardEmbed = new EmbedBuilder()
                .setAuthor(Config.BOT.getName() + "#" + Config.BOT.getDiscriminator() + " - Shards", null, Config.BOT.getAvatarUrl())
                .setTimestamp(Instant.now())
                .setFooter(Config.STANDARD_STRINGS.get(1) + e.getMember().getEffectiveName(), e.getAuthor().getEffectiveAvatarUrl());

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
        for(LavalinkSocket socket : Config.LAVALINK.getLavalink().getNodes()) {
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

        MessageHandler.reply(e, shardEmbed.build());
    }
}
