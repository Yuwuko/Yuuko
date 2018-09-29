package com.basketbandit.core.modules.media.commands;

import com.basketbandit.core.Configuration;
import com.basketbandit.core.modules.Command;
import com.basketbandit.core.modules.media.kitsu.Attributes;
import com.basketbandit.core.modules.media.kitsu.KitsuContainer;
import com.basketbandit.core.utils.Utils;
import com.basketbandit.core.utils.json.JsonBuffer;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;

public class CommandKitsu extends Command {

    public CommandKitsu() {
        super("kitsu", "com.basketbandit.core.modules.media.ModuleMedia", new String[]{"-kitsu [type] [name]"}, null);
    }

    public CommandKitsu(MessageReceivedEvent e, String[] command) {
        executeCommand(e, command);
    }

    @Override
    protected void executeCommand(MessageReceivedEvent e, String[] command) {
        try {
            String[] commandParameters = command[1].split("\\s+", 2);
            commandParameters[1] = commandParameters[1].replace(" ", "%20");
            String json = "";

            if(commandParameters[0].toLowerCase().equals("show")) {
                json = new JsonBuffer().getString("https://kitsu.io/api/edge/anime?filter[text]=" + commandParameters[1] + "&page[limit]=1", "application/vnd.api+json", "application/vnd.api+json");
            } else if(commandParameters[0].toLowerCase().equals("character")) {
                Utils.sendMessage(e, "Sorry, this command isn't ready yet! (Waiting for the Kitsu.io API to be constructed to support this!)");
                // json = new JsonBuffer().getString("https://kitsu.io/api/edge/anime-characters?filter[text]=" + commandParameters[1] + "&page[limit]=1");
            } else {
                json = new JsonBuffer().getString("https://kitsu.io/api/edge/anime?filter[text]=" + commandParameters[1] + "&page[limit]=1", "application/vnd.api+json", "application/vnd.api+json");
            }

            if(json != null && json.equals("")) {
                Utils.sendMessage(e,"Sorry " + e.getAuthor().getAsMention() + ", the anime you were looking for wasn't found.");
                return;
            }

            KitsuContainer kitsu = new ObjectMapper().readValue(json, new TypeReference<KitsuContainer>(){});
            Attributes anime = kitsu.getData().get(0).getAttributes();

            EmbedBuilder embed = new EmbedBuilder()
                    .setColor(Color.RED)
                    .setTitle(anime.getCanonicalTitle() + " | " + anime.getTitles().getJaJp())
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
            Utils.sendMessage(e, embed.build());

        } catch(Exception ex) {
            Utils.sendException(ex);
            Utils.sendMessage(e, "There was an issue processing the request for command: " + e.getMessage().getContentDisplay());
        }
    }

}
