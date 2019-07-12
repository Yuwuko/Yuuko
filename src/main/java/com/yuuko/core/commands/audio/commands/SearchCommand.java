package com.yuuko.core.commands.audio.commands;

import com.google.api.services.youtube.model.SearchResult;
import com.yuuko.core.Configuration;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.audio.AudioModule;
import com.yuuko.core.commands.audio.handlers.YouTubeSearchHandler;
import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.utilities.Sanitiser;
import com.yuuko.core.utilities.Utilities;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SearchCommand extends Command {
    private static final HashMap<String, List<SearchResult>> audioSearchResults = new HashMap<>();

    public SearchCommand() {
        super("search", AudioModule.class, 1, Arrays.asList("-search <term>", "-search <value>", "-search cancel"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        try {
            // If audioSearchResults contains the authors user ID and the command matches either 1-10 or "cancel".
            if(audioSearchResults.containsKey(e.getAuthor().getId()) && (e.getParameters().matches("^[0-9]{1,2}$") || e.getParameters().equals("cancel"))) {
                if(e.getParameters().equalsIgnoreCase("cancel")) {
                    audioSearchResults.remove(e.getAuthor().getId());
                    EmbedBuilder embed = new EmbedBuilder().setTitle(e.getAuthor().getName()).setDescription("Search cancelled.");
                    MessageHandler.sendMessage(e, embed.build());
                    return;
                }

                if(!Sanitiser.isNumber(e.getParameters())) {
                    EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Input").setDescription("Search input must be a number between `1` and `10`, or `cancel`.");
                    MessageHandler.sendMessage(e, embed.build());
                    return;
                }

                final int value = Integer.parseInt(e.getParameters());
                if(value < 11 && value > 0) {
                    String videoId = audioSearchResults.get(e.getAuthor().getId()).get(Integer.parseInt(e.getParameters()) - 1).getId().getVideoId();
                    MessageEvent event = new MessageEvent(e).setCommand(new PlayCommand()).setParameters("https://www.youtube.com/watch?v=" + videoId);
                    event.getCommand().onCommand(event);
                    audioSearchResults.remove(e.getAuthor().getId());
                    return;
                } else {
                    EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Input").setDescription("Search input must be a number between `1` and `10`, or `cancel`.");
                    MessageHandler.sendMessage(e, embed.build());
                    return;
                }
            }

            List<SearchResult> results = YouTubeSearchHandler.searchList(e);
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

            audioSearchResults.put(e.getAuthor().getId(), results);

            EmbedBuilder presentResults = new EmbedBuilder()
                    .setAuthor("Search results for " + e.getParameters() + ".", null)
                    .setDescription("Type `" + Utilities.getServerPrefix(e.getGuild()) + "search <value>` to play the track of the given value or `" + Utilities.getServerPrefix(e.getGuild()) + "search cancel` to stop me waiting for a response. \n\n" + resultString)
                    .setFooter(Configuration.STANDARD_STRINGS.get(1) + e.getMember().getEffectiveName(), e.getAuthor().getEffectiveAvatarUrl());
            MessageHandler.sendMessage(e, presentResults.build());

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
        }
    }

}
