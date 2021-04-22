package com.yuuko.modules.media.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.yuuko.MessageDispatcher;
import com.yuuko.Yuuko;
import com.yuuko.api.entity.Api;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.io.RequestHandler;
import com.yuuko.modules.Command;
import net.dv8tion.jda.api.EmbedBuilder;

import java.time.Instant;
import java.util.Arrays;

public class NationalGeographicCommand extends Command {
    private static final Api api = Yuuko.API_MANAGER.getApi("newsapi");
    private static final String BASE_URL = "https://newsapi.org/v2/top-headlines?sources=national-geographic&apiKey=" + api.getKey();

    public NationalGeographicCommand() {
        super("natgeo", Arrays.asList("-natgeo"));
        setEnabled(api.isAvailable());
    }

    @Override
    public void onCommand(MessageEvent context) throws Exception {
        JsonObject json = new RequestHandler(BASE_URL).getJsonObject();
        JsonArray articles = json.get("articles").getAsJsonArray();

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle(context.i18n( "title"))
                .setDescription(context.i18n( "desc") + " [National Geographic](https://news.nationalgeographic.com). Powered by NewsAPI. \n\u200b")
                .setThumbnail(articles.get(0).getAsJsonObject().get("urlToImage").getAsString())
                .setTimestamp(Instant.now())
                .setFooter(Yuuko.STANDARD_STRINGS.get(1) + context.getAuthor().getAsTag(), context.getAuthor().getEffectiveAvatarUrl());

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

        MessageDispatcher.reply(context, embed.build());
    }

}



