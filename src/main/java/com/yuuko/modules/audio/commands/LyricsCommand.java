package com.yuuko.modules.audio.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yuuko.MessageDispatcher;
import com.yuuko.Yuuko;
import com.yuuko.api.entity.Api;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.io.RequestHandler;
import com.yuuko.io.entity.RequestProperty;
import com.yuuko.modules.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LyricsCommand extends Command {
    private static final Api api = Yuuko.API_MANAGER.getApi("genius");

    public LyricsCommand() {
        super("lyrics", 1, -1L, Arrays.asList("-lyrics <song|artist>"), false, null, api.isAvailable());
    }

    @Override
    public void onCommand(MessageEvent context) throws Exception {
        final String url = "https://api.genius.com/search?q=" + context.getParameters().replace(" ", "%20");
        final JsonObject json = new RequestHandler(url, new RequestProperty("Authorization", "Bearer " + api.getKey())).getJsonObject();
        int response = json.get("meta").getAsJsonObject().get("status").getAsInt();

        JsonArray hits;
        JsonObject data;
        Elements elements;

        if(response != 200
                || (hits = json.get("response").getAsJsonObject().get("hits").getAsJsonArray()).size() < 1
                || (data = hits.get(0).getAsJsonObject().get("result").getAsJsonObject()) == null || data.isJsonNull()
                || (elements = Jsoup.connect(data.get("url").getAsString()).get().getElementsByClass("lyrics")).size() < 1) {
            EmbedBuilder embed = new EmbedBuilder().setTitle(context.i18n( "no_results").formatted(context.getParameters()));
            MessageDispatcher.reply(context, embed.build());
            return;
        }

        String lyrics = elements.get(0).text().replace("[", "\n\n[").replace("]", "]\n");
        if(lyrics.contains("\n \n\n")) {
            lyrics = lyrics.substring(lyrics.indexOf("\n \n\n"));
        }

        List<String> lyricsList = new ArrayList<>();
        if(lyrics.length() > 2048) {
            int index = 0;
            while(index < lyrics.length()) {
                lyricsList.add(lyrics.substring(index, Math.min(index + 2048, lyrics.length())));
                index += 2048;
               }
        }

        if(lyricsList.isEmpty()) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setAuthor(context.i18n( "lyrics"))
                    .setTitle(data.get("full_title").getAsString())
                    .setThumbnail(data.get("header_image_url").getAsString())
                    .setDescription(lyrics)
                    .setFooter(Yuuko.STANDARD_STRINGS.get(1) + context.getAuthor().getAsTag(), context.getAuthor().getEffectiveAvatarUrl());
            MessageDispatcher.reply(context, embed.build());
        } else {
            for(int i = 0; i < lyricsList.size(); i++) {
                EmbedBuilder embed = new EmbedBuilder()
                        .setAuthor(context.i18n( "lyrics"))
                        .setTitle(data.get("full_title").getAsString() + " (" + (i+1) + "/" + lyricsList.size() + ")")
                        .setThumbnail(data.get("header_image_url").getAsString())
                        .setDescription(lyricsList.get(i));
                MessageDispatcher.reply(context, embed.build());
            }
        }
    }

}

