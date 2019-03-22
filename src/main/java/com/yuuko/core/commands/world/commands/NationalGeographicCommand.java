package com.yuuko.core.commands.world.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.yuuko.core.Configuration;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.world.WorldModule;
import com.yuuko.core.utilities.MessageHandler;
import com.yuuko.core.utilities.Utils;
import com.yuuko.core.utilities.json.JsonBuffer;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.time.Instant;

public class NationalGeographicCommand extends Command {

    public NationalGeographicCommand() {
        super("natgeo", WorldModule.class, 0, new String[]{"-natgeo"}, false, null);
    }

    @Override
    public void onCommand(MessageReceivedEvent e, String[] command) {
        JsonObject json = new JsonBuffer("https://newsapi.org/v2/top-headlines?sources=national-geographic&apiKey=" + Utils.getApiKey("newsapi"), "default", "default").getAsJsonObject();
        JsonArray articles = json.get("articles").getAsJsonArray();

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Top headlines from National Geographic")
                .setDescription("Reporting our world daily: original nature and science news from [National Geographic](https://news.nationalgeographic.com). Powered by NewsAPI. \n\u200b")
                .setThumbnail(articles.get(0).getAsJsonObject().get("urlToImage").getAsString())
                .setTimestamp(Instant.now())
                .setFooter(Configuration.STANDARD_STRINGS[1] + e.getMember().getEffectiveName(), e.getAuthor().getEffectiveAvatarUrl());

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



