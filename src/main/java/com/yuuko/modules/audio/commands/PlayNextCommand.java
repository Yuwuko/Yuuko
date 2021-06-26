package com.yuuko.modules.audio.commands;

import com.google.api.services.youtube.model.SearchResult;
import com.yuuko.MessageDispatcher;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.modules.Command;
import com.yuuko.modules.audio.handlers.AudioLoadHandler;
import com.yuuko.modules.audio.handlers.AudioManager;
import com.yuuko.modules.audio.handlers.GuildAudioManager;
import com.yuuko.modules.audio.handlers.YouTubeSearchHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;

import java.util.Arrays;
import java.util.List;

public class PlayNextCommand extends Command {

    public PlayNextCommand() {
        super("playnext", Arrays.asList("-playnext", "-playnext <url>", "-playnext <term>"), Arrays.asList(Permission.VOICE_CONNECT, Permission.VOICE_SPEAK));
    }

    @Override
    public void onCommand(MessageEvent context) throws Exception {
        if(!context.hasParameters()) {
            return;
        }

        GuildAudioManager manager = AudioManager.getGuildAudioManager(context.getGuild());

        if(context.getParameters().startsWith("https://") || context.getParameters().startsWith("http://")) {
            AudioLoadHandler.loadAndPlay(manager, context, AudioLoadHandler.Playback.PLAYNEXT);
            return;
        }

        List<SearchResult> results = YouTubeSearchHandler.search(context);
        if(results == null || results.size() == 0 || results.get(0).getId().getVideoId().equals("")) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(context.i18n( "no_results"));
            MessageDispatcher.reply(context, embed.build());
            return;
        }

        String trackId = "https://www.youtube.com/watch?v=" + results.get(0).getId().getVideoId();
        AudioLoadHandler.loadAndPlay(manager, context.setParameters(trackId), AudioLoadHandler.Playback.PLAYNEXT);
    }
}
