package com.yuuko.core.commands.media.commands;

import com.google.gson.JsonObject;
import com.yuuko.core.Configuration;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.media.MediaModule;
import com.yuuko.core.events.extensions.MessageEvent;
import com.yuuko.core.utilities.Utilities;
import com.yuuko.core.utilities.json.JsonBuffer;
import net.dv8tion.jda.core.EmbedBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class GithubCommand extends Command {

    public GithubCommand() {
        super("github", MediaModule.class, 2, new String[]{"-github <user> <repository>"}, false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        String[] commandParameters = e.getCommandParameter().split("\\s+", 2);
        JsonObject json = new JsonBuffer("https://api.github.com/repos/" + commandParameters[0] + "/" + commandParameters[1] + "?access_token=" + Utilities.getApiKey("github"), "application/vnd.github.v3+json", "application/vnd.github.v3+json").getAsJsonObject();

        if(json == null) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("No Results").setDescription("Search for **_" + e.getCommandParameter() + "_** produced no results.");
            MessageHandler.sendMessage(e, embed.build());
            return;
        }

        String license = (json.get("license").isJsonNull() || json.get("license").getAsJsonObject().get("url").isJsonNull()) ? "" : "_License: [" + json.get("license").getAsJsonObject().get("name").getAsString() + "](" + json.get("license").getAsJsonObject().get("url").getAsString() + ")_";

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("GitHub: " + json.get("full_name").getAsString(), json.get("html_url").getAsString())
                .setThumbnail(json.get("owner").getAsJsonObject().get("avatar_url").getAsString())
                .setDescription(json.get("description").getAsString() + " " + license)
                .addField("Language", json.get("language").getAsString(),true)
                .addField("Latest Push", ZonedDateTime.parse(json.get("pushed_at").getAsString()).format(DateTimeFormatter.ofPattern("d MMM yyyy  hh:mma")),true)
                .addField("Stars", "[" + json.get("stargazers_count").getAsString() + "](https://github.com/" + json.get("full_name").getAsString() + "/stargazers)",true)
                .addField("Forks", "[" + json.get("forks_count").getAsString() + "](https://github.com/" + json.get("full_name").getAsString() + "/network/members)",true)
                .addField("Open Issues", "[" + json.get("open_issues_count").getAsString() + "](https://github.com/" + json.get("full_name").getAsString() + "/issues)",true)
                .addField("Pull Requests", "[link](https://github.com/" + json.get("full_name").getAsString() + "/pulls)", true)
                .addField("Commits", "[link](https://github.com/" + json.get("full_name").getAsString() + "/commits/" + json.get("default_branch").getAsString() + ")", true)
                .addField("Size", new BigDecimal(json.get("size").getAsInt()/1024.0).setScale(2, RoundingMode.HALF_UP) + "MB",true)
                .setFooter(Configuration.STANDARD_STRINGS[1] + e.getMember().getEffectiveName(), e.getAuthor().getEffectiveAvatarUrl());
        MessageHandler.sendMessage(e, embed.build());
    }
}