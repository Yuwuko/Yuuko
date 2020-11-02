package com.yuuko.core.commands.core.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yuuko.core.Configuration;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.database.function.DatabaseFunctions;
import com.yuuko.core.database.function.GuildFunctions;
import com.yuuko.core.entity.Shard;
import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.io.RequestHandler;
import com.yuuko.core.metrics.MetricsManager;
import com.yuuko.core.utilities.TextUtilities;
import net.dv8tion.jda.api.EmbedBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

public class AboutCommand extends Command {

    public AboutCommand() {
        super("about", Configuration.MODULES.get("core"), 0, -1L, Arrays.asList("-about"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        int totalGuilds = 0;
        for(Shard shard: DatabaseFunctions.getShardStatistics()) {
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
                .setAuthor(Configuration.BOT.getName() + "#" + Configuration.BOT.getDiscriminator(), null, Configuration.BOT.getAvatarUrl())
                .setDescription(
                        Configuration.BOT.getName() + ", programmed in [Java](https://www.oracle.com/uk/java/index.html), using [Gradle](https://gradle.org/) for dependencies.  " +
                        "If you would like me in your guild, [invite me!](https://discordapp.com/api/oauth2/authorize?client_id=420682957007880223&permissions=8&scope=bot) " +
                        "If I already am, thank you for your continued support! \n Use `" + e.getPrefix() + "help` for a list of commands, or `" + e.getPrefix() + "vote` if you wish to vote for " + Configuration.BOT.getName() + "."
                )
                .addField("Author", "[" + Configuration.AUTHOR + "](" + Configuration.AUTHOR_WEBSITE + ")", true)
                .addField("Version", Configuration.VERSION, true)
                .addField("Prefix", Configuration.GLOBAL_PREFIX + ", " + GuildFunctions.getGuildSetting("prefix", e.getGuild().getId()), true)
                .addField("Shard ID", Configuration.SHARD_ID + "", true)
                .addField("Guilds", MetricsManager.getDiscordMetrics().GUILD_COUNT + "", true)
                .addField("Commands", Configuration.COMMANDS.size() + "", true)
                .addField("Uptime", TextUtilities.getTimestamp(MetricsManager.getSystemMetrics().UPTIME), true)
                .addField("Ping", MetricsManager.getDiscordMetrics().GATEWAY_PING + "ms (" + MetricsManager.getDiscordMetrics().REST_PING + "ms)",true)
                .addField("Memory Usage", BigDecimal.valueOf((MetricsManager.getSystemMetrics().MEMORY_USED / MetricsManager.getSystemMetrics().MEMORY_TOTAL) * 100).setScale(2, RoundingMode.HALF_UP) + "%", true)
                .addField("Latest Updates", latestUpdates.toString(), false);
        MessageHandler.sendMessage(e, about.build());
    }
}
