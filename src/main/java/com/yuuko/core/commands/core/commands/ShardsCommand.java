package com.yuuko.core.commands.core.commands;

import com.yuuko.core.Cache;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.core.CoreModule;
import com.yuuko.core.utilities.MessageHandler;
import com.yuuko.core.utilities.TextUtility;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ShardsCommand extends Command {

    public ShardsCommand() {
        super("shards", CoreModule.class, 0, new String[]{"-shards"}, null);
    }

    @Override
    public void executeCommand(MessageReceivedEvent e, String[] command) {
        StringBuilder shards = new StringBuilder();
        for(JDA shard : Cache.SHARD_MANAGER.getShards()) {
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

        EmbedBuilder shardEmbed = new EmbedBuilder()
                .setAuthor(Cache.BOT.getName() + "#" + Cache.BOT.getDiscriminator() + " - Shards", null, Cache.BOT.getAvatarUrl())
                .setDescription(shards.toString());
        MessageHandler.sendMessage(e, shardEmbed.build());
    }
}
