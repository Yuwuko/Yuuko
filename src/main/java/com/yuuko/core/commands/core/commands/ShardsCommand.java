package com.yuuko.core.commands.core.commands;

import com.yuuko.core.Configuration;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.core.CoreModule;
import com.yuuko.core.utilities.MessageHandler;
import com.yuuko.core.utilities.TextUtility;
import lavalink.client.io.LavalinkSocket;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ShardsCommand extends Command {

    public ShardsCommand() {
        super("shards", CoreModule.class, 0, new String[]{"-shards"}, false, null);
    }

    @Override
    public void onCommand(MessageReceivedEvent e, String[] command) {
        StringBuilder shards = new StringBuilder();
        for(JDA shard : Configuration.SHARD_MANAGER.getShards()) {
            int userCount = 0;
            int channelCount = 0;
            for(Guild guild: shard.getGuildCache()) {
                channelCount += guild.getChannels().size();
                userCount += guild.getMemberCache().size();
            }

            shards.append("**Yuuko_").append(shard.getShardInfo().getShardId())
                    .append("** - ").append(shard.getStatus())
                    .append("\n").append("Guilds: ").append(shard.getGuildCache().size())
                    .append("\n").append("Channels: ").append(channelCount)
                    .append("\n").append("Users: ").append(userCount)
                    .append("\n\n");
        }
        TextUtility.removeLastOccurrence(shards, "\n\n");

        StringBuilder nodes = new StringBuilder();
        for(LavalinkSocket socket : Configuration.LAVALINK.getLavalink().getNodes()) {
            nodes.append("**Yuuko-").append(socket.getName())
                    .append("** - ").append(TextUtility.getTimestamp(socket.getStats().getUptime()))
                    .append("\n").append("System Load: ").append(new BigDecimal((socket.getStats().getSystemLoad()*100)/100.0).setScale(2, RoundingMode.HALF_UP))
                    .append("\n").append("CPU Cores: ").append(socket.getStats().getCpuCores())
                    .append("\n").append("Memory Used: ").append(new BigDecimal(socket.getStats().getMemUsed()/1000000.0).setScale(2, RoundingMode.HALF_UP)).append("MB")
                    .append("\n").append("Players: ").append(socket.getStats().getPlayers())
                    .append("\n").append("Playing: ").append(socket.getStats().getPlayingPlayers())
                    .append("\n\n");
        }
        TextUtility.removeLastOccurrence(nodes, "\n\n");

        EmbedBuilder shardEmbed = new EmbedBuilder()
                .setAuthor(Configuration.BOT.getName() + "#" + Configuration.BOT.getDiscriminator() + " - Shards", null, Configuration.BOT.getAvatarUrl())
                .addField("Yuuko", shards.toString(), false)
                .addField("Lavalink", nodes.toString(), false);
        MessageHandler.sendMessage(e, shardEmbed.build());
    }
}
