package com.yuuko.core.commands.audio.commands;

import com.google.gson.JsonObject;
import com.yuuko.core.Configuration;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.audio.AudioModule;
import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.io.RequestHandler;
import com.yuuko.core.io.entity.RequestProperty;
import com.yuuko.core.utilities.Utilities;
import net.dv8tion.jda.core.EmbedBuilder;
import org.jsoup.Jsoup;

import java.util.Arrays;
import java.util.List;

public class LyricsCommand extends Command {

    public LyricsCommand() {
        super("lyrics", AudioModule.class, 1, Arrays.asList("-lyrics <song|artist>"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        try {
            final String url = "https://api.genius.com/search?q=" + e.getCommand().get(1).replace(" ", "%20");
            final JsonObject json = new RequestHandler(url, new RequestProperty("Authorization", "Bearer " + Utilities.getApiKey("genius"))).getJsonObject();
            int response = json.get("meta").getAsJsonObject().get("status").getAsInt();

            if(response != 200) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Parameter").setDescription("There were no matches found for **" + e.getCommand().get(1) + "**.");
                MessageHandler.sendMessage(e, embed.build());
                return;
            }

            JsonObject data = json.get("response").getAsJsonObject().get("hits").getAsJsonArray().get(0).getAsJsonObject().get("result").getAsJsonObject();
            List<String> lyricsList = null;
            String lyrics = Jsoup.connect(data.get("url").getAsString()).get().getElementsByClass("lyrics").get(0).text().replace("[", "\n\n[").replace("]", "]\n");
            int noInfo = lyrics.indexOf("\n \n\n");

            if(noInfo > -1) {
                lyrics = lyrics.substring(lyrics.indexOf("\n \n\n"));
            }

            if(lyrics.length() > 2048) {
                lyricsList = Arrays.asList(lyrics.substring(0, 2047), lyrics.substring(2047));
            }

            if(lyricsList == null) {
                EmbedBuilder embed = new EmbedBuilder()
                        .setAuthor("Lyrics")
                        .setTitle(data.get("full_title").getAsString())
                        .setThumbnail(data.get("header_image_url").getAsString())
                        .setDescription(lyrics)
                        .setFooter(Configuration.STANDARD_STRINGS.get(1) + e.getMember().getEffectiveName(), e.getAuthor().getEffectiveAvatarUrl());
                MessageHandler.sendMessage(e, embed.build());
            } else {
                for(int i = 0; i < lyricsList.size() ; i++) {
                    EmbedBuilder embed = new EmbedBuilder()
                            .setAuthor("Lyrics")
                            .setTitle(data.get("full_title").getAsString() + " (" + (i+1) + "/" + lyricsList.size() + ")")
                            .setThumbnail(data.get("header_image_url").getAsString())
                            .setDescription(lyricsList.get(i));
                    MessageHandler.sendMessage(e, embed.build());
                }
            }

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
        }

    }

}

