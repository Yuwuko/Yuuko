package com.yuuko.core.modules.audio.commands;

import com.google.api.services.youtube.model.SearchResult;
import com.yuuko.core.Configuration;
import com.yuuko.core.modules.Command;
import com.yuuko.core.modules.audio.ModuleAudio;
import com.yuuko.core.modules.audio.handlers.YouTubeSearchHandler;
import com.yuuko.core.utils.MessageHandler;
import com.yuuko.core.utils.Utils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.List;

public class CommandSearch extends Command {

    public CommandSearch() {
        super("search", "com.yuuko.core.modules.audio.ModuleAudio", 1, new String[]{"-search [term]"}, null);
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

            ModuleAudio.searchUsers.put(e.getAuthor().getIdLong(), results);

            EmbedBuilder presentResults = new EmbedBuilder()
                    .setAuthor("Search results for " + command[1], null)
                    .setDescription("Input the number of the track you would like to play, or 'cancel' to stop me waiting for a response. \n\n" + resultString)
                    .setFooter(Configuration.VERSION + " · Requested by " + e.getAuthor().getName(), e.getGuild().getMemberById(Configuration.BOT_ID).getUser().getAvatarUrl());
            MessageHandler.sendMessage(e, presentResults.build());

        } catch(Exception ex) {
            Utils.sendException(ex, e.getMessage().getContentRaw());
        }
    }

    public void executeCommand(MessageReceivedEvent e, String input) {
        if(!input.equalsIgnoreCase("cancel")) {
            if(Integer.parseInt(input) < 11 && Integer.parseInt(input) > 0) {
                String videoId = ModuleAudio.searchUsers.get(e.getAuthor().getIdLong()).get(Integer.parseInt(input) - 1).getId().getVideoId();
                new CommandPlay().executeCommand(e, new String[]{"play", "https://www.youtube.com/watch?v=" + videoId});
                ModuleAudio.searchUsers.remove(e.getAuthor().getIdLong());
            } else {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Input").setDescription("Search input must be a number between 1 and 10, or 'cancel'.");
                MessageHandler.sendMessage(e, embed.build());
            }
        } else {
            ModuleAudio.searchUsers.remove(e.getAuthor().getIdLong());

            EmbedBuilder embed = new EmbedBuilder().setTitle(e.getAuthor().getName()).setDescription("Search cancelled.");
            MessageHandler.sendMessage(e, embed.build());
        }
    }

}
