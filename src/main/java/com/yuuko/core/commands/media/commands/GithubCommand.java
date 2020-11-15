package com.yuuko.core.commands.media.commands;

import com.google.gson.JsonObject;
import com.yuuko.core.Config;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.api.entity.Api;
import com.yuuko.core.commands.Command;
import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.io.RequestHandler;
import com.yuuko.core.utilities.Sanitiser;
import net.dv8tion.jda.api.EmbedBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class GithubCommand extends Command {
    private static final Api api = Config.API_MANAGER.getApi("github");
    private static final String BASE_URL = "https://api.github.com/repos/";

    public GithubCommand() {
        super("github", Config.MODULES.get("media"), 2, -1L, Arrays.asList("-github <user> <repository>"), false, null, api.isAvailable());
    }

    @Override
    public void onCommand(MessageEvent e) {
        String[] commandParameters = e.getParameters().split("\\s+", 2);

        final String url = BASE_URL + Sanitiser.scrub(commandParameters[0], true) + "/" + Sanitiser.scrub(commandParameters[1], true) + "?access_token=" + api.getKey();
        final JsonObject json = new RequestHandler(url).getJsonObject();

        if(json == null) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("No Results").setDescription("Search for `" + e.getParameters() + "` produced no results.");
            MessageHandler.sendMessage(e, embed.build());
            return;
        }

        final String license = (json.get("license").isJsonNull() || json.get("license").getAsJsonObject().get("url").isJsonNull()) ? "" : "_License: [" + json.get("license").getAsJsonObject().get("name").getAsString() + "](" + json.get("license").getAsJsonObject().get("url").getAsString() + ")_";
        final String language = json.get("language").getAsString();
        final String latestPush = ZonedDateTime.parse(json.get("pushed_at").getAsString()).format(DateTimeFormatter.ofPattern("d MMM yyyy  hh:mma"));
        final String stars = "[" + json.get("stargazers_count").getAsString() + "](https://github.com/" + json.get("full_name").getAsString() + "/stargazers)";
        final String forks = "[" + json.get("forks_count").getAsString() + "](https://github.com/" + json.get("full_name").getAsString() + "/network/members)";
        final String openIssues = "[" + json.get("open_issues_count").getAsString() + "](https://github.com/" + json.get("full_name").getAsString() + "/issues)";
        final String pullRequests = "[link](https://github.com/" + json.get("full_name").getAsString() + "/pulls)";
        final String commits = "[link](https://github.com/" + json.get("full_name").getAsString() + "/commits/" + json.get("default_branch").getAsString() + ")";
        final String size = BigDecimal.valueOf(json.get("size").getAsInt() / 1024.0).setScale(2, RoundingMode.HALF_UP) + "MB";

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("GitHub: " + json.get("full_name").getAsString(), json.get("html_url").getAsString())
                .setThumbnail(json.get("owner").getAsJsonObject().get("avatar_url").getAsString())
                .setDescription(json.get("description").getAsString() + " " + license)
                .addField("Language", language,true)
                .addField("Latest Push", latestPush,true)
                .addField("Stars", stars,true)
                .addField("Forks", forks,true)
                .addField("Open Issues", openIssues,true)
                .addField("Pull Requests", pullRequests, true)
                .addField("Commits", commits, true)
                .addField("Size", size,true)
                .setFooter(Config.STANDARD_STRINGS.get(1) + e.getMember().getEffectiveName(), e.getAuthor().getEffectiveAvatarUrl());
        MessageHandler.sendMessage(e, embed.build());
    }
}