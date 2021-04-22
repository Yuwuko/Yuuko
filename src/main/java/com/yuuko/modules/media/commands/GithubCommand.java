package com.yuuko.modules.media.commands;

import com.google.gson.JsonObject;
import com.yuuko.MessageDispatcher;
import com.yuuko.Yuuko;
import com.yuuko.api.entity.Api;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.io.RequestHandler;
import com.yuuko.io.entity.RequestProperty;
import com.yuuko.modules.Command;
import net.dv8tion.jda.api.EmbedBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class GithubCommand extends Command {
    private static final Api api = Yuuko.API_MANAGER.getApi("github");
    private static final String BASE_URL = "https://api.github.com/repos/";

    public GithubCommand() {
        super("github", Arrays.asList("-github <user> <repository>"), 2);
        setEnabled(api.isAvailable());
    }

    @Override
    public void onCommand(MessageEvent context) throws Exception {
        String[] params = context.getParameters().split("\\s+", 2);
        final String url = BASE_URL + URLEncoder.encode(params[0], StandardCharsets.UTF_8).replace("+", "%20") + "/" + URLEncoder.encode(params[1], StandardCharsets.UTF_8).replace("+", "%20");
        final JsonObject json = new RequestHandler(url, new RequestProperty("Authorization", "token " + api.getKey())).getJsonObject();
        if(json == null) {
            EmbedBuilder embed = new EmbedBuilder().setTitle(context.i18n( "no_results")).setDescription(context.i18n( "no_results_desc").formatted(context.getParameters()));
            MessageDispatcher.reply(context, embed.build());
            return;
        }

        final String license = (json.get("license").isJsonNull() || json.get("license").getAsJsonObject().get("url").isJsonNull()) ? "" : "_License: [" + json.get("license").getAsJsonObject().get("name").getAsString() + "](" + json.get("license").getAsJsonObject().get("url").getAsString() + ")_";
        final String language = (json.get("language").isJsonNull()) ? "None" : json.get("language").getAsString();
        final String latestPush = (json.get("pushed_at").isJsonNull()) ? "Never Pushed" : ZonedDateTime.parse(json.get("pushed_at").getAsString()).format(DateTimeFormatter.ofPattern("d MMM yyyy hh:mma"));
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
                .addField(context.i18n( "language"), language,true)
                .addField(context.i18n( "latest_push"), latestPush,true)
                .addField(context.i18n( "size"), size,true)
                .addField(context.i18n( "stars"), stars,true)
                .addField(context.i18n( "forks"), forks,true)
                .addField(context.i18n( "issues"), openIssues,true)
                .addField(context.i18n( "commits"), commits, true)
                .addField(context.i18n( "pull_requests"), pullRequests, true)
                .setFooter(Yuuko.STANDARD_STRINGS.get(1) + context.getAuthor().getAsTag(), context.getAuthor().getEffectiveAvatarUrl());
        MessageDispatcher.reply(context, embed.build());
    }
}