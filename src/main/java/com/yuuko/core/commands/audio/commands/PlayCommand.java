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
import com.yuuko.core.utilities.LavalinkUtilities;
import com.yuuko.core.utilities.TextUtilities;
import lavalink.client.io.Link;
import net.dv8tion.jda.core.EmbedBuilder;

import java.util.List;

public class PlayCommand extends Command {

    public PlayCommand() {
        super("play", AudioModule.class, 0, new String[]{"-play", "-play <url>", "-play <term>"}, false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        GuildAudioManager manager = AudioManagerManager.getGuildAudioManager(e.getGuild().getId());

        if(!LavalinkUtilities.isState(e.getGuild(), Link.State.CONNECTED)) {
            Configuration.LAVALINK.openConnection(e.getMember().getVoiceState().getChannel());
        }

        if(e.getCommand().length == 1) {
            if(manager.player.isPaused()) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Resuming").setDescription("The player has been resumed.");
                MessageHandler.sendMessage(e, embed.build());
                manager.player.setPaused(false);
                new CurrentCommand();
            }

        } else {
            manager.player.setPaused(false);

            if(e.getCommand()[1].startsWith("https://") || e.getCommand()[1].startsWith("http://")) {
                loadAndPlay(manager, e, e.getCommand()[1]);

            } else {
                String trackId = YouTubeSearchHandler.search(e.getCommand()[1]);

                if(trackId == null || trackId.equals("")) {
                    EmbedBuilder embed = new EmbedBuilder().setTitle("Those search parameters failed to return a result.");
                    MessageHandler.sendMessage(e, embed.build());
                } else {
                    loadAndPlay(manager, e, trackId);
                }
            }
        }

    }

    /**
     * Loads a track from a given url and plays it if possible, else adds it to the queue.
     *
     * @param manager GuildAudioManager.
     * @param e MessageEvent.
     * @param url TrackUrl.
     */
    private void loadAndPlay(GuildAudioManager manager, MessageEvent e, String url) {
        final String trackUrl;

        if(url.startsWith("<") && url.endsWith(">")) {
            trackUrl = url.substring(1, url.length() - 1);
        } else {
            trackUrl = url;
        }

        AudioManagerManager.getPlayerManager().loadItemOrdered(manager, trackUrl, new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack track) {
                try {
                    track.setUserData(e);
                    manager.scheduler.queue(track);

                    String[] uri = track.getInfo().uri.split("=");
                    String imageUrl = (uri.length > 1) ? "https://img.youtube.com/vi/" + uri[1] + "/1.jpg" : "https://i.imgur.com/bCNQlm6.jpg";

                    EmbedBuilder embed = new EmbedBuilder()
                            .setAuthor(e.getMember().getEffectiveName() + " added to the queue!", null, e.getAuthor().getAvatarUrl())
                            .setTitle(track.getInfo().title, trackUrl)
                            .setThumbnail(imageUrl)
                            .addField("Duration", TextUtilities.getTimestamp(track.getDuration()), true)
                            .addField("Channel", track.getInfo().author, true)
                            .addField("Position in queue", manager.scheduler.queue.size() + "", false)
                            .setFooter(Configuration.STANDARD_STRINGS[1] + e.getMember().getEffectiveName(), e.getAuthor().getEffectiveAvatarUrl());
                    MessageHandler.sendMessage(e, embed.build());
                } catch(Exception ex) {
                    log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
                }
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                try {
                    EmbedBuilder embed = new EmbedBuilder().setTitle("Adding **" + playlist.getTracks().size() + "** tracks to queue from playlist: **" + playlist.getName() + "**");
                    MessageHandler.sendMessage(e, embed.build());

                    List<AudioTrack> tracks = playlist.getTracks();
                    for(AudioTrack track: tracks) {
                        track.setUserData(e);
                        manager.scheduler.queue(track);
                    }

                    new CurrentCommand().onCommand(e);

                } catch(Exception ex) {
                    log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
                }
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
