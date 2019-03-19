package com.yuuko.core.commands.audio.commands;

import com.google.api.services.youtube.model.SearchResult;
import com.yuuko.core.Configuration;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.audio.AudioModule;
import com.yuuko.core.commands.audio.handlers.YouTubeSearchHandler;
import com.yuuko.core.utilities.MessageHandler;
import com.yuuko.core.utilities.Sanitiser;
import com.yuuko.core.utilities.Utils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.HashMap;
import java.util.List;

public class SearchCommand extends Command {
    private static final HashMap<Long, List<SearchResult>> audioSearchResults = new HashMap<>();

    public SearchCommand() {
        super("search", AudioModule.class, 1, new String[]{"-search <term>", "-search <value>", "-search cancel"}, false, null);
    }

    @Override
    public void onCommand(MessageReceivedEvent e, String[] command) {
        try {
            // If audioSearchResults contains the authors user ID and the command matches either 1-10 or "cancel".
            if(audioSearchResults.containsKey(e.getAuthor().getIdLong()) && (command[1].matches("^[0-9]{1,2}$") || command[1].equals("cancel"))) {
                if(command[1].equalsIgnoreCase("cancel")) {
                    audioSearchResults.remove(e.getAuthor().getIdLong());

                    EmbedBuilder embed = new EmbedBuilder().setTitle(e.getAuthor().getName()).setDescription("Search cancelled.");
                    MessageHandler.sendMessage(e, embed.build());
                    return;
                }

                if(Sanitiser.isNumber(command[1])) {
                    final int value = Integer.parseInt(command[1]);
                    if(value < 11 && value > 0) {
                        String videoId = audioSearchResults.get(e.getAuthor().getIdLong()).get(Integer.parseInt(command[1]) - 1).getId().getVideoId();
                        new PlayCommand().onCommand(e, new String[]{"play", "https://www.youtube.com/watch?v=" + videoId});
                        audioSearchResults.remove(e.getAuthor().getIdLong());
                        return;
                    } else {
                        EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Input").setDescription("Search input must be a number between 1 and 10, or 'cancel'.");
                        MessageHandler.sendMessage(e, embed.build());
                        return;
                    }
                } else {
                    EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Input").setDescription("Search input must be a number between 1 and 10, or 'cancel'.");
                    MessageHandler.sendMessage(e, embed.build());
                    return;
                }
            }

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

            audioSearchResults.put(e.getAuthor().getIdLong(), results);

            EmbedBuilder presentResults = new EmbedBuilder()
                    .setAuthor("Search results for " + command[1] + ".", null)
                    .setDescription("Type `" + Utils.getServerPrefix(e.getGuild().getId()) + "search <value>` to play the track of the given value or `" + Utils.getServerPrefix(e.getGuild().getId()) + "search cancel` to stop me waiting for a response. \n\n" + resultString)
                    .setFooter(Configuration.STANDARD_STRINGS[1] + e.getMember().getEffectiveName(), e.getAuthor().getEffectiveAvatarUrl());
            MessageHandler.sendMessage(e, presentResults.build());

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
        }
    }

}
