package com.yuuko.modules.media.commands;

import com.google.gson.JsonObject;
import com.yuuko.MessageDispatcher;
import com.yuuko.Yuuko;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.i18n.I18n;
import com.yuuko.io.RequestHandler;
import com.yuuko.io.entity.RequestProperty;
import com.yuuko.modules.Command;
import com.yuuko.utilities.Sanitiser;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;

public class AnimeCommand extends Command {
    private static final String BASE_URL = "https://kitsu.io/api/edge/anime?filter[text]=";

    public AnimeCommand() {
        super("anime", 1, -1L, Arrays.asList("-anime <title>"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) throws Exception {
        final String url = BASE_URL + Sanitiser.scrub(e.getParameters(), true) + "&page[limit]=1";
        JsonObject json = new RequestHandler(url, new RequestProperty("Accept", "application/vnd.api+json"), new RequestProperty("Content-Type","application/vnd.api+json")).getJsonObject();
        if(json == null || json.isJsonNull() || json.getAsJsonArray("data").size() < 1) {
            EmbedBuilder embed = new EmbedBuilder().setTitle(I18n.getText(e, "no_results")).setDescription(I18n.getText(e, "no_results_desc").formatted(e.getParameters()));
            MessageDispatcher.reply(e, embed.build());
            return;
        }

        JsonObject data = json.getAsJsonArray("data").get(0).getAsJsonObject().get("attributes").getAsJsonObject(); // It's important to find the item in the array where the data is stored.
        final String ageRating = data.get("ageRating").isJsonNull() ? "Unknown" : data.get("ageRating").getAsString() + ": " + data.get("ageRatingGuide").getAsString();
        final String episodes = data.get("episodeCount").isJsonNull() ? "Unknown" : data.get("episodeCount").getAsString();
        final String episodeLength = data.get("episodeLength").isJsonNull() ? "Unknown" : data.get("episodeLength").getAsString() + " minutes";
        final String totalLength = (data.get("episodeLength").isJsonNull() || data.get("episodeCount").isJsonNull()) ? "Unknown" : ((data.get("episodeLength").getAsInt()*data.get("episodeCount").getAsInt())/60) + " hours";
        final String type = data.get("showType").isJsonNull() ? "Unknown" : data.get("showType").getAsString();
        final String approvalRating = data.get("averageRating").isJsonNull() ? "Unknown" : data.get("averageRating").getAsString() + "%";
        final String status = data.get("status").isJsonNull() ? "Unknown" : data.get("status").getAsString();
        final String startDate = data.get("startDate").isJsonNull() ? "Unknown" : data.get("startDate").getAsString();
        final String endDate = data.get("endDate").isJsonNull() ? "Unknown" : data.get("endDate").getAsString();

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle(data.get("canonicalTitle").getAsString() + " | " + data.get("titles").getAsJsonObject().get("ja_jp").getAsString(), data.get("youtubeVideoId").isJsonNull() ? "" : "https://www.youtube.com/watch?v=" + data.get("youtubeVideoId").getAsString())
                .setImage(data.get("posterImage").getAsJsonObject().get("medium").getAsString())
                .setDescription(data.get("synopsis").getAsString())
                .addField(I18n.getText(e, "age"), ageRating, true)
                .addField(I18n.getText(e, "episodes"), episodes, true)
                .addField(I18n.getText(e, "episode_length"), episodeLength, true)
                .addField(I18n.getText(e, "total_length"), totalLength, true)
                .addField(I18n.getText(e, "type"), type, true)
                .addField(I18n.getText(e, "approval"), approvalRating, true)
                .addField(I18n.getText(e, "status"), status, true)
                .addField(I18n.getText(e, "start"), startDate, true)
                .addField(I18n.getText(e, "end"), endDate, true)
                .setFooter(Yuuko.STANDARD_STRINGS.get(1) + e.getAuthor().getAsTag(), e.getAuthor().getEffectiveAvatarUrl());
        MessageDispatcher.reply(e, embed.build());
    }

}
