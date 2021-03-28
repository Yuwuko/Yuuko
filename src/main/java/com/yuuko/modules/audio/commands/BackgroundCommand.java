package com.yuuko.modules.audio.commands;

import com.google.api.services.youtube.model.SearchResult;
import com.yuuko.MessageDispatcher;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.i18n.I18n;
import com.yuuko.modules.Command;
import com.yuuko.modules.audio.handlers.AudioLoadHandler;
import com.yuuko.modules.audio.handlers.AudioManager;
import com.yuuko.modules.audio.handlers.GuildAudioManager;
import com.yuuko.modules.audio.handlers.YouTubeSearchHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;

import java.util.Arrays;
import java.util.List;

public class BackgroundCommand extends Command {

    public BackgroundCommand() {
        super("background", 0, -1L, Arrays.asList("-background", "-background <url>", "-background <term>"), false, Arrays.asList(Permission.VOICE_CONNECT, Permission.VOICE_SPEAK));
    }

    @Override
    public void onCommand(MessageEvent e) throws Exception {
        GuildAudioManager manager = AudioManager.getGuildAudioManager(e.getGuild());
        manager.openConnection(e.getMember().getVoiceState().getChannel());

        // Remove background
        if(!e.hasParameters()) {
            EmbedBuilder embed = new EmbedBuilder().setTitle(I18n.getText(e, "title")).setDescription(I18n.getText(e,"desc"));
            MessageDispatcher.reply(e, embed.build());
            manager.getScheduler().setBackground(null);
            return;
        }

        // Set background via URL
        if(e.getParameters().startsWith("https://") || e.getParameters().startsWith("http://")) {
            AudioLoadHandler.loadAndPlay(manager, e, AudioLoadHandler.Playback.BACKGROUND);
            return;
        }

        // Set background via search
        List<SearchResult> results = YouTubeSearchHandler.search(e);
        if(results == null || results.size() == 0 || results.get(0).getId().getVideoId().equals("")) {
            EmbedBuilder embed = new EmbedBuilder().setTitle(I18n.getText(e, "params_failed"));
            MessageDispatcher.reply(e, embed.build());
            return;
        }
        String trackId = "https://www.youtube.com/watch?v=" + results.get(0).getId().getVideoId();
        AudioLoadHandler.loadAndPlay(manager, e.setParameters(trackId), AudioLoadHandler.Playback.BACKGROUND);
    }
}
