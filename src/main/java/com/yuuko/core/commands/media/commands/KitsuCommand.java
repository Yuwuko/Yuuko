package com.yuuko.core.commands.media.commands;

import com.google.gson.JsonObject;
import com.yuuko.core.Cache;
import com.yuuko.core.Configuration;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.media.MediaModule;
import com.yuuko.core.utilities.MessageHandler;
import com.yuuko.core.utilities.json.JsonBuffer;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class KitsuCommand extends Command {

    public KitsuCommand() {
        super("kitsu", MediaModule.class, 1, new String[]{"-kitsu [name]"}, null);
    }

    @Override
    public void executeCommand(MessageReceivedEvent e, String[] command) {
        try {
            JsonObject json = new JsonBuffer("https://kitsu.io/api/edge/anime?filter[text]=" + command[1].replace(" ", "%20") + "&page[limit]=1", "application/vnd.api+json", "application/vnd.api+json", null, null).getAsJsonObject();

            if(json == null || json.getAsJsonArray("data").size() < 1) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("No Results").setDescription("Search for **_" + command[1] + "_** produced no results.");
                MessageHandler.sendMessage(e, embed.build());
                return;
            }

            JsonObject data = json.getAsJsonArray("data").get(0).getAsJsonObject().get("attributes").getAsJsonObject(); // It's important to find the item in the array where the data is stored.

            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(data.get("canonicalTitle").getAsString() + " | " + data.get("titles").getAsJsonObject().get("ja_jp").getAsString(), (data.get("youtubeVideoId").isJsonNull()) ? "" : "https://www.youtube.com/watch?v=" + data.get("youtubeVideoId").toString())
                    .setImage(data.get("posterImage").getAsJsonObject().get("medium").getAsString())
                    .setDescription(data.get("synopsis").getAsString())
                    .addField("Age Rating", data.get("ageRating").getAsString() + ": " + data.get("ageRatingGuide").getAsString(), true)
                    .addField("Episodes", data.get("episodeCount").getAsString(), true)
                    .addField("Episode Length", data.get("episodeLength").getAsString() + " minutes", true)
                    .addField("Total Length", data.get("totalLength").getAsInt()/60 + " hours", true)
                    .addField("Type", data.get("showType").getAsString(), true)
                    .addField("Kitsu Approval Rating", data.get("averageRating").getAsString() + "%", true)
                    .addField("Status", data.get("status").getAsString(), true)
                    .addField("Start Date", data.get("startDate").getAsString(), true)
                    .addField("End Date", data.get("endDate").getAsString(), true)
                    .setFooter(Cache.STANDARD_STRINGS[1] + e.getMember().getEffectiveName() , e.getGuild().getMemberById(Configuration.BOT_ID).getUser().getAvatarUrl());
            MessageHandler.sendMessage(e, embed.build());

        } catch(Exception ex) {
            MessageHandler.sendException(ex, e.getMessage().getContentRaw());
        }
    }

}
