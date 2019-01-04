package com.yuuko.core.modules.audio.commands;

import com.google.api.services.youtube.model.SearchResult;
import com.yuuko.core.Cache;
import com.yuuko.core.Configuration;
import com.yuuko.core.modules.Command;
import com.yuuko.core.modules.audio.AudioModule;
import com.yuuko.core.modules.audio.handlers.YouTubeSearchHandler;
import com.yuuko.core.utilities.MessageHandler;
import com.yuuko.core.utilities.Sanitiser;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.List;

public class SearchCommand extends Command {

    public SearchCommand() {
        super("search", AudioModule.class, 1, new String[]{"-search [term]"}, null);
    }

    @Override
    public void executeCommand(MessageReceivedEvent e, String[] command) {
        try {
            List<SearchResult> results = YouTubeSearchHandler.searchList(e, command);
            StringBuilder resultString = new StringBuilder();

            if(results != null) {
                int i = 1;
                for(SearchResult result : results) {
                    resultString.append("`").append(i).append(":` ").append(result.getSnippet().getTitle()).append("\n\n");
                    i++;
                }
            } else {
                EmbedBuilder embed = new EmbedBuilder().setTitle("There was an issue processing your request.");
                MessageHandler.sendMessage(e, embed.build());
                return;
            }

            Cache.audioSearchResults.put(e.getAuthor().getIdLong(), results);

            EmbedBuilder presentResults = new EmbedBuilder()
                    .setAuthor("Search results for " + command[1], null)
                    .setDescription("Input the number of the track you would like to play, or 'cancel' to stop me waiting for a response. \n\n" + resultString)
                    .setFooter(Cache.STANDARD_STRINGS[2] + e.getAuthor().getName(), e.getGuild().getMemberById(Configuration.BOT_ID).getUser().getAvatarUrl());
            MessageHandler.sendMessage(e, presentResults.build());

        } catch(Exception ex) {
            MessageHandler.sendException(ex, e.getMessage().getContentRaw());
        }
    }

    public void executeCommand(MessageReceivedEvent e, String input) {
        if(!input.equalsIgnoreCase("cancel")) {
            if(Sanitiser.isNumber(input)) {
                final int value = Integer.parseInt(input);
                if(value < 11 && value > 0) {
                    String videoId = Cache.audioSearchResults.get(e.getAuthor().getIdLong()).get(Integer.parseInt(input) - 1).getId().getVideoId();
                    new PlayCommand().executeCommand(e, new String[]{"play", "https://www.youtube.com/watch?v=" + videoId});
                    Cache.audioSearchResults.remove(e.getAuthor().getIdLong());
                } else {
                    EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Input").setDescription("Search input must be a number between 1 and 10, or 'cancel'.");
                    MessageHandler.sendMessage(e, embed.build());
                }
            } else {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Input").setDescription("Search input must be a number between 1 and 10, or 'cancel'.");
                MessageHandler.sendMessage(e, embed.build());
            }
        } else {
            Cache.audioSearchResults.remove(e.getAuthor().getIdLong());

            EmbedBuilder embed = new EmbedBuilder().setTitle(e.getAuthor().getName()).setDescription("Search cancelled.");
            MessageHandler.sendMessage(e, embed.build());
        }
    }

}
