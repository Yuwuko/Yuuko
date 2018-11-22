package com.yuuko.core.modules.media.commands;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuuko.core.Configuration;
import com.yuuko.core.modules.Command;
import com.yuuko.core.modules.media.kitsu.Attributes;
import com.yuuko.core.modules.media.kitsu.KitsuContainer;
import com.yuuko.core.utils.MessageHandler;
import com.yuuko.core.utils.Utils;
import com.yuuko.core.utils.json.JsonBuffer;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandKitsu extends Command {

    public CommandKitsu() {
        super("kitsu", "com.yuuko.core.modules.media.ModuleMedia", 2, new String[]{"-kitsu [type] [name]"}, null);
    }

    @Override
    public void executeCommand(MessageReceivedEvent e, String[] command) {
        try {
            String[] commandParameters = command[1].split("\\s+", 2);
            commandParameters[1] = commandParameters[1].replace(" ", "%20");
            String json = "";

            if(commandParameters[0].toLowerCase().equals("character")) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("That parameter isn't ready yet. (waiting for the kitsu.io API to be constructed)");
                MessageHandler.sendMessage(e, embed.build());
                // json = new JsonBuffer().getString("https://kitsu.io/api/edge/anime-characters?filter[text]=" + commandParameters[1] + "&page[limit]=1");
            } else {
                json = new JsonBuffer().getString("https://kitsu.io/api/edge/anime?filter[text]=" + commandParameters[0] + "%20" + commandParameters[1] + "&page[limit]=1", "application/vnd.api+json", "application/vnd.api+json");
            }

            if(json != null && json.equals("")) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("That search parameter didn't return any results.");
                MessageHandler.sendMessage(e, embed.build());
                return;
            }

            KitsuContainer kitsu = new ObjectMapper().readValue(json, new TypeReference<KitsuContainer>(){});
            Attributes anime = kitsu.getData().get(0).getAttributes();

            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(anime.getCanonicalTitle() + " | " + anime.getTitles().getJaJp(), "https://www.youtube.com/watch?v=" + anime.getYoutubeVideoId())
                    .setImage(anime.getPosterImage().getMedium())
                    .setDescription(anime.getSynopsis())
                    .addField("Age Rating", anime.getAgeRating() + ": " + anime.getAgeRatingGuide(), true)
                    .addField("Episodes", anime.getEpisodeCount() + "", true)
                    .addField("Episode Length", anime.getEpisodeLength() + "m", true)
                    .addField("Type", anime.getSubtype(), true)
                    .addField("NSFW", anime.getNsfw() + "", true)
                    .addField("Kitsu Approval Rating", anime.getAverageRating() + "%", true)
                    .addField("Status", anime.getStatus(), true)
                    .addField("Start Date", anime.getStartDate(), true)
                    .addField("End Date", anime.getEndDate(), true)
                    .setFooter("Version: " + Configuration.VERSION + ", data provided by kitsu.io" , e.getGuild().getMemberById(Configuration.BOT_ID).getUser().getAvatarUrl());
            MessageHandler.sendMessage(e, embed.build());

        } catch(Exception ex) {
            Utils.sendException(ex, e.getMessage().getContentRaw());
            MessageHandler.sendMessage(e, "There was an issue processing the request for command: " + e.getMessage().getContentDisplay());
        }
    }

}
