package com.yuuko.modules.audio.commands;

import com.google.api.services.youtube.model.SearchResult;
import com.yuuko.MessageDispatcher;
import com.yuuko.Yuuko;
import com.yuuko.events.entity.MessageEvent;
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
    public void onCommand(MessageEvent context) throws Exception {
        // If audioSearchResults contains the authors user ID and the command matches either 1-10 or "cancel".
        if((audioSearchResults.containsKey(context.getAuthor().getId()) && Sanitiser.isNumeric(context.getParameters())) || context.getParameters().equals("cancel")) {
            if(context.getParameters().equalsIgnoreCase("cancel")) {
                audioSearchResults.remove(context.getAuthor().getId());
                EmbedBuilder embed = new EmbedBuilder().setTitle(context.getAuthor().getName()).setDescription(context.i18n( "cancelled"));
                MessageDispatcher.reply(context, embed.build());
                return;
            }

            final int value = Integer.parseInt(context.getParameters());
            if(value < 0 || value > 10) {
                EmbedBuilder embed = new EmbedBuilder().setTitle(context.i18n( "title")).setDescription(context.i18n( "desc"));
                MessageDispatcher.reply(context, embed.build());
                return;
            }

            String videoId = audioSearchResults.get(context.getAuthor().getId()).get(value == 0 ? 0 : value - 1).getId().getVideoId();
            MessageEvent event = new MessageEvent(context).setCommand(new PlayCommand()).setParameters("https://www.youtube.com/watch?v=" + videoId);
            event.getCommand().onCommand(event);
            audioSearchResults.remove(context.getAuthor().getId());
            return;
        }

        List<SearchResult> results = YouTubeSearchHandler.search(context);
        if(results == null) {
            EmbedBuilder embed = new EmbedBuilder().setTitle(context.i18n( "error_processing"));
            MessageDispatcher.reply(context, embed.build());
            return;
        }

        StringBuilder resultString = new StringBuilder();
        int i = 1;
        for(SearchResult result : results) {
            resultString.append("`").append(i).append(":` ").append(result.getSnippet().getTitle()).append("\n\n");
            i++;
        }

        audioSearchResults.put(context.getAuthor().getId(), results);
        EmbedBuilder embed = new EmbedBuilder()
                .setAuthor(context.i18n( "results").formatted(context.getParameters()), null)
                .setDescription(context.i18n( "desc").formatted(context.getPrefix(), context.getPrefix(), resultString))
                .setFooter(Yuuko.STANDARD_STRINGS.get(1) + context.getAuthor().getAsTag(), context.getAuthor().getEffectiveAvatarUrl());
        MessageDispatcher.reply(context, embed.build());
    }

}
