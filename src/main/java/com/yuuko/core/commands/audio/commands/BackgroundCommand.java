package com.yuuko.core.commands.audio.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.yuuko.core.Configuration;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.audio.AudioModule;
import com.yuuko.core.commands.audio.handlers.AudioManagerManager;
import com.yuuko.core.commands.audio.handlers.GuildAudioManager;
import com.yuuko.core.commands.audio.handlers.YouTubeSearchHandler;
import com.yuuko.core.events.extensions.MessageEvent;
import com.yuuko.core.utilities.TextUtilities;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class BackgroundCommand extends Command {

    public BackgroundCommand() {
        super("background", AudioModule.class, 0, new String[]{"-background", "-background <url>", "-background <term>"}, false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        GuildAudioManager manager = AudioManagerManager.getGuildAudioManager(e.getGuild().getId());

        if(e.hasParameters()) {
            Configuration.LAVALINK.openConnection(e.getMember().getVoiceState().getChannel());
            manager.player.setPaused(false);

            if(e.getCommand()[1].startsWith("https://") || e.getCommand()[1].startsWith("http://")) {
                setAndPlay(manager, e, e.getCommand()[1]);

            } else {
                String trackUrl = YouTubeSearchHandler.search(e.getCommand()[1]);

                if(trackUrl == null) {
                    EmbedBuilder embed = new EmbedBuilder().setTitle("Those search parameters failed to return a result.");
                    MessageHandler.sendMessage(e, embed.build());
                } else {
                    setAndPlay(manager, e, trackUrl);
                }
            }
        } else {
            // If no parameters are given, unset the background track.
            EmbedBuilder embed = new EmbedBuilder().setTitle("Removing").setDescription("The background track has been removed.");
            MessageHandler.sendMessage(e, embed.build());
            manager.scheduler.setBackground(null);
        }
    }

    /**
     * Loads a track from a given url and plays it if possible, else adds it to the queue.
     *
     * @param manager; GuildAudioManager.
     * @param e; MessageReceivedEvent.
     * @param url; TrackUrl.
     */
    private void setAndPlay(GuildAudioManager manager, MessageReceivedEvent e, String url) {
        final String trackUrl;

        if(url.startsWith("<") && url.endsWith(">")) {
            trackUrl = url.substring(1, url.length() - 1);
        } else {
            trackUrl = url;
        }

        AudioManagerManager.getPlayerManager().loadItemOrdered(manager, trackUrl, new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack track) {
                if(manager.scheduler.queue.size() == 0) {
                    manager.scheduler.queue(track);
                }

                manager.scheduler.setBackground(track);

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
