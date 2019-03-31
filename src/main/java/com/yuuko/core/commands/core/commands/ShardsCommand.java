package com.yuuko.core.commands.core.commands;

import com.yuuko.core.Configuration;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.core.CoreModule;
import com.yuuko.core.events.extensions.MessageEvent;
import lavalink.client.io.LavalinkSocket;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;

public class ShardsCommand extends Command {

    public ShardsCommand() {
        super("shards", CoreModule.class, 0, new String[]{"-shards"}, false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        EmbedBuilder shardEmbed = new EmbedBuilder()
                .setAuthor(Configuration.BOT.getName() + "#" + Configuration.BOT.getDiscriminator() + " - Shards", null, Configuration.BOT.getAvatarUrl())
                .setTimestamp(Instant.now())
                .setFooter(Configuration.STANDARD_STRINGS[1] + e.getMember().getEffectiveName(), e.getAuthor().getEffectiveAvatarUrl());

        StringBuilder shards = new StringBuilder();
        for(JDA shard : Configuration.SHARD_MANAGER.getShards()) {
            int userCount = 0;
            int channelCount = 0;
            for(Guild guild: shard.getGuildCache()) {
                channelCount += guild.getChannels().size();
                userCount += guild.getMemberCache().size();
            }

            shards.append("**Yuuko_").append(shard.getShardInfo().getShardId()).append("**")
                    .append("\n").append("Status: ").append(shard.getStatus())
                    .append("\n").append("Guilds: ").append(shard.getGuildCache().size())
                    .append("\n").append("Channels: ").append(channelCount)
                    .append("\n").append("Users: ").append(userCount);

            shardEmbed.addField("", shards.toString(), true);
            shards = new StringBuilder();
        }

        shardEmbed.addBlankField(false);

        StringBuilder nodes = new StringBuilder();
        for(LavalinkSocket socket : Configuration.LAVALINK.getLavalink().getNodes()) {
            nodes.append("**Yuuko-").append(socket.getName()).append("**")
                    .append("\n").append("System Load: ").append(new BigDecimal((socket.getStats().getSystemLoad()*100)/100.0).setScale(2, RoundingMode.HALF_UP))
                    .append("\n").append("CPU Cores: ").append(socket.getStats().getCpuCores())
                    .append("\n").append("Memory Used: ").append(new BigDecimal(socket.getStats().getMemUsed()/1000000.0).setScale(2, RoundingMode.HALF_UP)).append("MB")
                    .append("\n").append("Players: ").append(socket.getStats().getPlayers())
                    .append("\n").append("Playing: ").append(socket.getStats().getPlayingPlayers());
            shardEmbed.addField("", nodes.toString(), true);
            nodes = new StringBuilder();
        }

        MessageHandler.sendMessage(e, shardEmbed.build());
    }
}
