package com.yuuko.modules.audio.commands;

import com.google.api.services.youtube.model.SearchResult;
import com.yuuko.MessageDispatcher;
import com.yuuko.Yuuko;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.i18n.I18n;
import com.yuuko.modules.Command;
import com.yuuko.modules.audio.handlers.YouTubeSearchHandler;
import com.yuuko.utilities.Sanitiser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SearchCommand extends Command {
    private static final HashMap<String, List<SearchResult>> audioSearchResults = new HashMap<>();

    public SearchCommand() {
        super("search", 1, -1L, Arrays.asList("-search <term>", "-search <value>", "-search cancel"), false, Arrays.asList(Permission.VOICE_CONNECT, Permission.VOICE_SPEAK));
    }

    @Override
    public void onCommand(MessageEvent e) throws Exception {
        // If audioSearchResults contains the authors user ID and the command matches either 1-10 or "cancel".
        if((audioSearchResults.containsKey(e.getAuthor().getId()) && Sanitiser.isNumeric(e.getParameters())) || e.getParameters().equals("cancel")) {
            if(e.getParameters().equalsIgnoreCase("cancel")) {
                audioSearchResults.remove(e.getAuthor().getId());
                EmbedBuilder embed = new EmbedBuilder().setTitle(e.getAuthor().getName()).setDescription(I18n.getText(e, "cancelled"));
                MessageDispatcher.reply(e, embed.build());
                return;
            }

            final int value = Integer.parseInt(e.getParameters());
            if(value < 0 || value > 10) {
                EmbedBuilder embed = new EmbedBuilder().setTitle(I18n.getText(e, "title")).setDescription(I18n.getText(e, "desc"));
                MessageDispatcher.reply(e, embed.build());
                return;
            }

            String videoId = audioSearchResults.get(e.getAuthor().getId()).get(value == 0 ? 0 : value - 1).getId().getVideoId();
            MessageEvent event = new MessageEvent(e).setCommand(new PlayCommand()).setParameters("https://www.youtube.com/watch?v=" + videoId);
            event.getCommand().onCommand(event);
            audioSearchResults.remove(e.getAuthor().getId());
            return;
        }

        List<SearchResult> results = YouTubeSearchHandler.search(e);
        if(results == null) {
            EmbedBuilder embed = new EmbedBuilder().setTitle(I18n.getText(e, "error_processing"));
            MessageDispatcher.reply(e, embed.build());
            return;
        }

        StringBuilder resultString = new StringBuilder();
        int i = 1;
        for(SearchResult result : results) {
            resultString.append("`").append(i).append(":` ").append(result.getSnippet().getTitle()).append("\n\n");
            i++;
        }

        audioSearchResults.put(e.getAuthor().getId(), results);
        EmbedBuilder embed = new EmbedBuilder()
                .setAuthor(I18n.getText(e, "results").formatted(e.getParameters()), null)
                .setDescription(I18n.getText(e, "desc").formatted(e.getPrefix(), e.getPrefix(), resultString))
                .setFooter(Yuuko.STANDARD_STRINGS.get(1) + e.getAuthor().getAsTag(), e.getAuthor().getEffectiveAvatarUrl());
        MessageDispatcher.reply(e, embed.build());
    }

}
