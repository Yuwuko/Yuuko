package com.yuuko.core.commands.core.commands;

import com.yuuko.core.Configuration;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.core.CoreModule;
import com.yuuko.core.database.function.DatabaseFunctions;
import com.yuuko.core.entity.Shard;
import com.yuuko.core.events.extensions.MessageEvent;
import lavalink.client.io.LavalinkSocket;
import net.dv8tion.jda.core.EmbedBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Arrays;

public class ShardsCommand extends Command {

    public ShardsCommand() {
        super("shards", CoreModule.class, 0, Arrays.asList("-shards"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        EmbedBuilder shardEmbed = new EmbedBuilder()
                .setAuthor(Configuration.BOT.getName() + "#" + Configuration.BOT.getDiscriminator() + " - Shards", null, Configuration.BOT.getAvatarUrl())
                .setTimestamp(Instant.now())
                .setFooter(Configuration.STANDARD_STRINGS.get(1) + e.getMember().getEffectiveName(), e.getAuthor().getEffectiveAvatarUrl());

        StringBuilder shards = new StringBuilder();
        for(Shard shard : DatabaseFunctions.getShardStatistics()) {
            shards.append("**Yuuko #").append(shard.getId()).append("**")
                    .append("\n").append("Status: ").append(shard.getStatus())
                    .append("\n").append("Guilds: ").append(shard.getGuildCount())
                    .append("\n").append("Users: ").append(shard.getUserCount())
                    .append("\n").append("Ping: ").append(shard.getPing()).append("ms");

            shardEmbed.addField("", shards.toString(), true);
            shards = new StringBuilder();
        }

        StringBuilder nodes = new StringBuilder();
        for(LavalinkSocket socket : Configuration.LAVALINK.getLavalink().getNodes()) {
            if(socket.getStats() != null) {
                nodes.append("**Yuuko-").append(socket.getName()).append("**")
                        .append("\n").append("System Load: ").append(new BigDecimal((socket.getStats().getSystemLoad() * 100) / 100.0).setScale(2, RoundingMode.HALF_UP)).append("%")
                        .append("\n").append("Memory Used: ").append(new BigDecimal(socket.getStats().getMemUsed() / 1000000.0).setScale(2, RoundingMode.HALF_UP)).append("MB")
                        .append("\n").append("Players: ").append(socket.getStats().getPlayers())
                        .append("\n").append("Active: ").append(socket.getStats().getPlayingPlayers());
                shardEmbed.addField("", nodes.toString(), true);
                nodes = new StringBuilder();
            }
        }

        MessageHandler.sendMessage(e, shardEmbed.build());
    }
}
