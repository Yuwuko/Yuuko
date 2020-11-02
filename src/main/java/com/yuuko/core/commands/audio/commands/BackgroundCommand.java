package com.yuuko.core.commands.audio.commands;

import com.google.api.services.youtube.model.SearchResult;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.yuuko.core.Configuration;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.audio.handlers.AudioManagerController;
import com.yuuko.core.commands.audio.handlers.GuildAudioManager;
import com.yuuko.core.commands.audio.handlers.YouTubeSearchHandler;
import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.utilities.TextUtilities;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;

import java.util.Arrays;
import java.util.List;

public class BackgroundCommand extends Command {

    public BackgroundCommand() {
        super("background", Configuration.MODULES.get("audio"), 0, -1L, Arrays.asList("-background", "-background <url>", "-background <term>"), false, Arrays.asList(Permission.VOICE_CONNECT, Permission.VOICE_SPEAK));
    }

    @Override
    public void onCommand(MessageEvent e) {
        GuildAudioManager manager = AudioManagerController.getGuildAudioManager(e.getGuild());

        if(!e.hasParameters()) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Removing").setDescription("The background track has been removed.");
            MessageHandler.sendMessage(e, embed.build());
            manager.getScheduler().setBackground(null);
            return;
        }

        manager.openConnection(e.getMember().getVoiceState().getChannel());
        manager.getPlayer().setPaused(false);

        if(e.getParameters().startsWith("https://") || e.getParameters().startsWith("http://")) {
            setAndPlay(manager, e, e.getParameters());
            return;
        }

        List<SearchResult> results = YouTubeSearchHandler.search(e);
        if(results == null || results.size() == 0 || results.get(0).getId().getVideoId().equals("")) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Those search parameters failed to return a result.");
            MessageHandler.sendMessage(e, embed.build());
            return;
        }

        String trackId = "https://www.youtube.com/watch?v=" + results.get(0).getId().getVideoId();
        setAndPlay(manager, e, trackId);
    }

    /**
     * Loads a track from a given url and plays it if possible, else adds it to the queue.
     *
     * @param manager {@link GuildAudioManager}
     * @param e {@link MessageEvent}
     * @param url {@link String}
     */
    private void setAndPlay(GuildAudioManager manager, MessageEvent e, String url) {
        final String param = e.getParameters();
        final String trackUrl = param.startsWith("<") && param.endsWith(">") ? param.substring(1, param.length() - 1) : param;

        AudioManagerController.getPlayerManager().loadItemOrdered(manager, trackUrl, new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack track) {
                if(manager.getScheduler().queue.size() == 0) {
                    manager.getScheduler().queue(track);
                }

                manager.getScheduler().setBackground(track);

                EmbedBuilder embed = new EmbedBuilder()
                        .setAuthor(e.getMember().getEffectiveName() + " set the background track!",null, e.getAuthor().getAvatarUrl())
                        .setTitle(track.getInfo().title, trackUrl)
                        .addField("Duration", TextUtilities.getTimestamp(track.getDuration()), true)
                        .addField("Channel", track.getInfo().author, true)
                        .setFooter(Configuration.VERSION, Configuration.BOT.getAvatarUrl());
                MessageHandler.sendMessage(e, embed.build());
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                // Playlist as background not currently supported but method needed implementation.
                EmbedBuilder embed = new EmbedBuilder().setTitle("Adding playlists to the background is currently unsupported.");
                MessageHandler.sendMessage(e, embed.build());
            }

            @Override
            public void noMatches() {
                EmbedBuilder embed = new EmbedBuilder().setTitle("No matches found using that parameter.");
                MessageHandler.sendMessage(e, embed.build());
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Loading failed: " + exception.getMessage());
                MessageHandler.sendMessage(e, embed.build());
            }
        });
    }
}
