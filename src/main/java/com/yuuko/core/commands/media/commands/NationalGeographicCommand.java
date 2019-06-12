package com.yuuko.core.commands.media.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.yuuko.core.Configuration;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.media.MediaModule;
import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.io.RequestHandler;
import com.yuuko.core.utilities.Utilities;
import net.dv8tion.jda.core.EmbedBuilder;

import java.time.Instant;
import java.util.Arrays;

public class NationalGeographicCommand extends Command {

    public NationalGeographicCommand() {
        super("natgeo", MediaModule.class, 0, Arrays.asList("-natgeo"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        final String url = "https://newsapi.org/v2/top-headlines?sources=national-geographic&apiKey=" + Utilities.getApiKey("newsapi");
        JsonObject json = new RequestHandler(url).getJsonObject();
        JsonArray articles = json.get("articles").getAsJsonArray();

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Top headlines from National Geographic")
                .setDescription("Reporting our world daily: original nature and science news from [National Geographic](https://news.nationalgeographic.com). Powered by NewsAPI. \n\u200b")
                .setThumbnail(articles.get(0).getAsJsonObject().get("urlToImage").getAsString())
                .setTimestamp(Instant.now())
                .setFooter(Configuration.STANDARD_STRINGS.get(1) + e.getMember().getEffectiveName(), e.getAuthor().getEffectiveAvatarUrl());

        for(JsonElement article: articles) {
            JsonObject articleAsJsonObject = article.getAsJsonObject();

            String title = (!articleAsJsonObject.get("title").isJsonNull()) ? articleAsJsonObject.get("title").getAsString(): "Unknown";
            String author = (!articleAsJsonObject.get("author").isJsonNull()) ? "_" + articleAsJsonObject.get("author").getAsString() + "_" : "_Unknown_";
            String description;

            if(!articleAsJsonObject.get("description").isJsonNull()) {
                if(!articleAsJsonObject.get("url").isJsonNull()) {
                    description = articleAsJsonObject.get("description").getAsString() + "\n [full article](" + articleAsJsonObject.get("url").getAsString() +") \n\u200b";
                } else {
                    description = articleAsJsonObject.get("description").getAsString() + "\n\u200b";
                }
            } else {
                description = "Unknown";
            }

            embed.addField(title + " - " + author, description,false);
        }

        MessageHandler.sendMessage(e, embed.build());
    }

}



