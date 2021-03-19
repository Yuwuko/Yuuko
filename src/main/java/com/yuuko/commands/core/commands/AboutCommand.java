package com.yuuko.commands.core.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yuuko.MessageDispatcher;
import com.yuuko.Yuuko;
import com.yuuko.commands.Command;
import com.yuuko.database.function.GuildFunctions;
import com.yuuko.database.function.ShardFunctions;
import com.yuuko.entity.Shard;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.io.RequestHandler;
import com.yuuko.metrics.MetricsManager;
import com.yuuko.utilities.TextUtilities;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;

public class AboutCommand extends Command {

    public AboutCommand() {
        super("about", Yuuko.MODULES.get("core"), 0, -1L, Arrays.asList("-about"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) throws Exception {
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
                .setDescription(
                        Yuuko.BOT.getName() + ", programmed in [Java](https://www.oracle.com/uk/java/index.html) " +
                        "If you would like me in your guild, [invite me!](https://discordapp.com/api/oauth2/authorize?client_id=420682957007880223&permissions=8&scope=bot) " +
                        "If I already am, thank you for your continued support, and please consider supporting " + Yuuko.BOT.getName() + " on [patreon](https://www.patreon.com/yuwuko/)!" +
                                "\n Use `" + e.getPrefix() + "help` for a list of commands, or `" + e.getPrefix() + "vote` if you wish to vote for " + Yuuko.BOT.getName() + "."
                )
                .addField("Author", "[" + Yuuko.AUTHOR + "](" + Yuuko.AUTHOR_WEBSITE + ")", true)
                .addField("Version", Yuuko.VERSION, true)
                .addField("Prefix", Yuuko.GLOBAL_PREFIX + ", " + GuildFunctions.getGuildSetting("prefix", e.getGuild().getId()), true)
                .addField("Shard ID", e.getShardId() + "", true)
                .addField("Shard Guilds", MetricsManager.getDiscordMetrics(e.getShardId()).GUILD_COUNT + "", true)
                .addField("Total Guilds", totalGuilds + "", true)
                .addField("Commands", Yuuko.COMMANDS.size() + "", true)
                .addField("Uptime", TextUtilities.getTimestamp(MetricsManager.getSystemMetrics().UPTIME), true)
                .addField("Ping", MetricsManager.getDiscordMetrics(e.getShardId()).GATEWAY_PING + "ms (" + MetricsManager.getDiscordMetrics(e.getShardId()).REST_PING + "ms)",true)
                .addField("Latest Updates", latestUpdates.toString(), false);
        MessageDispatcher.reply(e, about.build());
    }
}
