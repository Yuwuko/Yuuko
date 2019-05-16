package com.yuuko.core.commands.media.commands;

import com.google.gson.JsonObject;
import com.yuuko.core.Configuration;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.media.MediaModule;
import com.yuuko.core.events.extensions.MessageEvent;
import com.yuuko.core.utilities.json.JsonBuffer;
import net.dv8tion.jda.core.EmbedBuilder;

public class KitsuCommand extends Command {

    public KitsuCommand() {
        super("kitsu", MediaModule.class, 1, new String[]{"-kitsu <title>", "-kitsu show <title>", "-kitsu character <name>"}, false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        try {
            JsonObject json = new JsonBuffer("https://kitsu.io/api/edge/anime?filter[text]=" + e.getCommand()[1].replace(" ", "%20") + "&page[limit]=1", "application/vnd.api+json", "application/vnd.api+json").getAsJsonObject();

            if(json == null || json.getAsJsonArray("data").size() < 1) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("No Results").setDescription("Search for **_" + e.getCommand()[1] + "_** produced no results.");
                MessageHandler.sendMessage(e, embed.build());
                return;
            }

            JsonObject data = json.getAsJsonArray("data").get(0).getAsJsonObject().get("attributes").getAsJsonObject(); // It's important to find the item in the array where the data is stored.

            final String ageRating = data.get("ageRating").getAsString() + ": " + data.get("ageRatingGuide").getAsString();
            final String episodes = data.get("episodeCount").getAsString();
            final String episodeLength = data.get("episodeLength").getAsString() + " minutes";
            final String totalLength = data.get("totalLength").getAsInt()/60 + " hours";
            final String type = data.get("showType").getAsString();
            final String approvalRating = data.get("averageRating").getAsString() + "%";
            final String status = data.get("status").getAsString();
            final String startDate = data.get("startDate").getAsString();
            final String endDate = data.get("endDate").getAsString();

            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(data.get("canonicalTitle").getAsString() + " | " + data.get("titles").getAsJsonObject().get("ja_jp").getAsString(), (data.get("youtubeVideoId").isJsonNull()) ? "" : "https://www.youtube.com/watch?v=" + data.get("youtubeVideoId").toString())
                    .setImage(data.get("posterImage").getAsJsonObject().get("medium").getAsString())
                    .setDescription(data.get("synopsis").getAsString())
                    .addField("Age Rating", ageRating, true)
                    .addField("Episodes", episodes, true)
                    .addField("Episode Length", episodeLength, true)
                    .addField("Total Length", totalLength, true)
                    .addField("Type", type, true)
                    .addField("Kitsu Approval Rating", approvalRating, true)
                    .addField("Status", status, true)
                    .addField("Start Date", startDate, true)
                    .addField("End Date", endDate, true)
                    .setFooter(Configuration.STANDARD_STRINGS[1] + e.getMember().getEffectiveName(), e.getAuthor().getEffectiveAvatarUrl());
            MessageHandler.sendMessage(e, embed.build());

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
        }
    }

}
