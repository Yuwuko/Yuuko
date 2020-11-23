package com.yuuko.core.commands.audio.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yuuko.core.Config;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.api.entity.Api;
import com.yuuko.core.commands.Command;
import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.io.RequestHandler;
import com.yuuko.core.io.entity.RequestProperty;
import net.dv8tion.jda.api.EmbedBuilder;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LyricsCommand extends Command {
    private static final Api api = Config.API_MANAGER.getApi("genius");

    public LyricsCommand() {
        super("lyrics", Config.MODULES.get("audio"), 1, -1L, Arrays.asList("-lyrics <song|artist>"), false, null, api.isAvailable());
    }

    @Override
    public void onCommand(MessageEvent e) {
        try {
            final String url = "https://api.genius.com/search?q=" + e.getParameters().replace(" ", "%20");
            final JsonObject json = new RequestHandler(url, new RequestProperty("Authorization", "Bearer " + api.getKey())).getJsonObject();
            int response = json.get("meta").getAsJsonObject().get("status").getAsInt();

            if(response != 200) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("No Results").setDescription("Search for `" + e.getParameters() + "` produced no results.");
                MessageHandler.reply(e, embed.build());
                return;
            }

            JsonArray hits = json.get("response").getAsJsonObject().get("hits").getAsJsonArray();
            if(hits.size() < 1) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("No Results").setDescription("Search for `" + e.getParameters() + "` produced no results.");
                MessageHandler.reply(e, embed.build());
                return;
            }

            JsonObject data = hits.get(0).getAsJsonObject().get("result").getAsJsonObject();
            Elements elements = Jsoup.connect(data.get("url").getAsString()).get().getElementsByClass("lyrics");
            if(elements.size() < 1) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("No Results").setDescription("Search for `" + e.getParameters() + "` produced no results.");
                MessageHandler.reply(e, embed.build());
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
                        .setAuthor("Lyrics")
                        .setTitle(data.get("full_title").getAsString())
                        .setThumbnail(data.get("header_image_url").getAsString())
                        .setDescription(lyrics)
                        .setFooter(Config.STANDARD_STRINGS.get(1) + e.getMember().getEffectiveName(), e.getAuthor().getEffectiveAvatarUrl());
                MessageHandler.reply(e, embed.build());
            } else {
                for(int i = 0; i < lyricsList.size(); i++) {
                    EmbedBuilder embed = new EmbedBuilder()
                            .setAuthor("Lyrics")
                            .setTitle(data.get("full_title").getAsString() + " (" + (i+1) + "/" + lyricsList.size() + ")")
                            .setThumbnail(data.get("header_image_url").getAsString())
                            .setDescription(lyricsList.get(i));
                    MessageHandler.reply(e, embed.build());
                }
            }

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
        }
    }

}

