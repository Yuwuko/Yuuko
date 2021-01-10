package com.yuuko.core.commands.audio.commands;

import com.google.api.services.youtube.model.SearchResult;
import com.yuuko.core.Config;
import com.yuuko.core.MessageDispatcher;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.audio.handlers.AudioLoadHandler;
import com.yuuko.core.commands.audio.handlers.AudioManager;
import com.yuuko.core.commands.audio.handlers.GuildAudioManager;
import com.yuuko.core.commands.audio.handlers.YouTubeSearchHandler;
import com.yuuko.core.events.entity.MessageEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;

import java.util.Arrays;
import java.util.List;

public class PlayCommand extends Command {

    public PlayCommand() {
        super("play", Config.MODULES.get("audio"), 0, -1L, Arrays.asList("-play", "-play <url>", "-play <term>"), false, Arrays.asList(Permission.VOICE_CONNECT, Permission.VOICE_SPEAK));
    }

    @Override
    public void onCommand(MessageEvent e) throws Exception {
        GuildAudioManager manager = AudioManager.getGuildAudioManager(e.getGuild());
        manager.openConnection(e.getMember().getVoiceState().getChannel());

        if(!e.hasParameters()) {
            if(manager.getPlayer().isPaused()) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Resuming").setDescription("The player has been resumed.");
                MessageDispatcher.reply(e, embed.build());
                manager.getPlayer().setPaused(false);
            }
            return;
        }

        if(e.getParameters().startsWith("https://") || e.getParameters().startsWith("http://")) {
            AudioLoadHandler.loadAndPlay(manager, e, AudioLoadHandler.Playback.PLAY);
            return;
        }

        List<SearchResult> results = YouTubeSearchHandler.search(e);
        if(results == null || results.size() == 0 || results.get(0).getId().getVideoId().equals("")) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Those search parameters failed to return a result.");
            MessageDispatcher.reply(e, embed.build());
            return;
        }

        String trackId = "https://www.youtube.com/watch?v=" + results.get(0).getId().getVideoId();
        AudioLoadHandler.loadAndPlay(manager, e.setParameters(trackId), AudioLoadHandler.Playback.PLAY);
    }

}
