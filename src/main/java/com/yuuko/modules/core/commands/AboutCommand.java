package com.yuuko.modules.core.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yuuko.MessageDispatcher;
import com.yuuko.Yuuko;
import com.yuuko.database.function.GuildFunctions;
import com.yuuko.database.function.ShardFunctions;
import com.yuuko.entity.Shard;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.io.RequestHandler;
import com.yuuko.metrics.MetricsManager;
import com.yuuko.modules.Command;
import com.yuuko.utilities.TextUtilities;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;

public class AboutCommand extends Command {

    public AboutCommand() {
        super("about", Arrays.asList("-about"));
    }

    @Override
    public void onCommand(MessageEvent context) throws Exception {
        int totalGuilds = 0;
        for(Shard shard: ShardFunctions.getShardStatistics()) {
            totalGuilds += shard.getGuildCount();
        }

        // Commit feature first seen used by Senither @AvaIre.
        JsonArray git = new RequestHandler("https://api.github.com/repos/Yuuko-oh/Yuuko/commits").getJsonArray();
        StringBuilder latestUpdates = new StringBuilder();

        for(int i = 0; i < 5; i++) {
            JsonObject obj = git.get(i).getAsJsonObject();
            String sha = obj.get("sha").getAsString();
            String message = obj.get("commit").getAsJsonObject().get("message").getAsString();
            String truncatedMessage = (message.length() > 59) ? message.substring(0,56) + "..." : message;
            String commitString = "[`" + sha.substring(0,7) + "`](https://github.com/Yuuko-oh/Yuuko/commit/" + sha + ") " + truncatedMessage + "\n";

            if((commitString.length() + latestUpdates.length()) < 1024) {
                latestUpdates.append(commitString);
            } else {
                break;
            }
        }

        EmbedBuilder about = new EmbedBuilder()
                .setAuthor(Yuuko.BOT.getName() + "#" + Yuuko.BOT.getDiscriminator(), null, Yuuko.BOT.getAvatarUrl())
                .setDescription(context.i18n( "desc").formatted(Yuuko.SUPPORT_GUILD, context.getPrefix(), context.getPrefix()))
                .addField(context.i18n( "author"), "[" + Yuuko.AUTHOR + "](" + Yuuko.AUTHOR_WEBSITE + ")", true)
                .addField(context.i18n( "version"), Yuuko.VERSION, true)
                .addField(context.i18n( "prefix"), Yuuko.GLOBAL_PREFIX + ", " + GuildFunctions.getGuildSetting("prefix", context.getGuild().getId()), true)
                .addField(context.i18n( "shard_id"), context.getShardId() + "", true)
                .addField(context.i18n( "shard_guilds"), MetricsManager.getDiscordMetrics(context.getShardId()).GUILD_COUNT + "", true)
                .addField(context.i18n( "shard_guilds_total"), totalGuilds + "", true)
                .addField(context.i18n( "commands"), Yuuko.COMMANDS.size() + "", true)
                .addField(context.i18n( "uptime"), TextUtilities.getTimestamp(MetricsManager.getSystemMetrics().UPTIME), true)
                .addField(context.i18n( "ping"), MetricsManager.getDiscordMetrics(context.getShardId()).GATEWAY_PING + "ms (" + MetricsManager.getDiscordMetrics(context.getShardId()).REST_PING + "ms)",true)
                .addField(context.i18n( "latest_updates"), latestUpdates.toString(), false);
        MessageDispatcher.reply(context, about.build());
    }
}
