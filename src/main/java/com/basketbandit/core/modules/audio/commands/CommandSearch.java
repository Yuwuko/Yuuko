package com.basketbandit.core.modules.audio.commands;

import com.basketbandit.core.Configuration;
import com.basketbandit.core.modules.Command;
import com.basketbandit.core.modules.audio.ModuleAudio;
import com.basketbandit.core.modules.audio.handlers.YouTubeSearchHandler;
import com.basketbandit.core.utils.Utils;
import com.google.api.services.youtube.model.SearchResult;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.List;

public class CommandSearch extends Command {

    public CommandSearch() {
        super("search", "com.basketbandit.core.modules.audio.ModuleAudio", new String[]{"-search [term]"}, null);
    }

    public CommandSearch(MessageReceivedEvent e, String[] command) {
        executeCommand(e, command);
    }

    @Override
    protected void executeCommand(MessageReceivedEvent e, String[] command) {

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
                Utils.sendMessage(e, "Sorry " + e.getAuthor().getAsMention() + ", there was a problem processing your request.");
                return;
            }

            EmbedBuilder presentResults = new EmbedBuilder()
                    .setAuthor(e.getAuthor().getName() + ", results for: " + command[1], null, e.getAuthor().getAvatarUrl())
                    .setDescription("Type in the number of the track you would like to play or type cancel to stop me waiting for a response. \n\n" + resultString)
                    .setFooter("Version: " + Configuration.VERSION, null);

            ModuleAudio.searchUsers.put(e.getAuthor().getIdLong(), results);
            Utils.sendMessage(e, presentResults.build());

        } catch(Exception ex) {
            Utils.sendException(ex.getMessage());
        }
    }

}
