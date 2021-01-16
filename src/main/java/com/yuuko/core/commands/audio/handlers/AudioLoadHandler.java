package com.yuuko.core.commands.audio.handlers;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.yuuko.core.MessageDispatcher;
import com.yuuko.core.Yuuko;
import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.utilities.TextUtilities;
import com.yuuko.core.utilities.Utilities;
import net.dv8tion.jda.api.EmbedBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class AudioLoadHandler {
    private static final Logger log = LoggerFactory.getLogger(AudioLoadHandler.class);

    public enum Playback {
        PLAY,
        PLAYNEXT,
        BACKGROUND
    }

    /**
     * Loads a track from a given url and plays it if possible, else adds it to the queue.
     *
     * @param manager {@link GuildAudioManager}
     * @param e {@link MessageEvent}
     */
    public static void loadAndPlay(GuildAudioManager manager, MessageEvent e, Playback type) {
        final String param = e.getParameters();
        final String trackUrl = param.startsWith("<") && param.endsWith(">") ? param.substring(1, param.length() - 1) : param;

        AudioManager.getPlayerManager().loadItemOrdered(manager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                try {
                    track.setUserData(e);

                    EmbedBuilder embed = new EmbedBuilder().setTitle(track.getInfo().title, trackUrl)
                            .setThumbnail(Utilities.getAudioTrackImage(track))
                            .addField("Duration", TextUtilities.getTimestamp(track.getDuration()), true)
                            .addField("Channel", track.getInfo().author, true)
                            .addField("Position in queue", manager.getScheduler().queue.size() + "", false)
                            .setFooter(Yuuko.STANDARD_STRINGS.get(1) + e.getAuthor().getAsTag(), e.getAuthor().getEffectiveAvatarUrl());

                    switch(type) {
                        case PLAY -> embed.setAuthor(e.getAuthor().getAsTag() + " added to the queue!", null, e.getAuthor().getAvatarUrl());
                        case PLAYNEXT -> embed.setAuthor(e.getAuthor().getAsTag() + " added to the front of the queue!", null, e.getAuthor().getAvatarUrl());
                        case BACKGROUND -> embed.setAuthor(e.getAuthor().getAsTag() + " set the background track!",null, e.getAuthor().getAvatarUrl());
                        default -> embed.setAuthor(e.getAuthor().getAsTag() + " did something!",null, e.getAuthor().getAvatarUrl());
                    }
                    MessageDispatcher.reply(e, embed.build());


                    if(type == Playback.PLAY) {
                        manager.getScheduler().queue(track);
                        return;
                    }

                    if(type == Playback.PLAYNEXT) {
                        ArrayList<AudioTrack> tempQueue = new ArrayList<>(manager.getScheduler().queue);
                        manager.getScheduler().queue.clear();
                        manager.getScheduler().queue.add(track);
                        manager.getScheduler().queue.addAll(tempQueue);
                        return;
                    }

                    if(type == Playback.BACKGROUND) {
                        if(manager.getScheduler().queue.size() == 0) {
                            manager.getScheduler().queue(track);
                        }
                        manager.getScheduler().setBackground(track);
                    }
                } catch(Exception ex) {
                    log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
                }
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                try {
                    if(type == Playback.PLAY) {
                        EmbedBuilder embed = new EmbedBuilder().setTitle("Adding `" + playlist.getTracks().size() + "` tracks to queue from playlist: `" + playlist.getName() + "`");
                        MessageDispatcher.reply(e, embed.build());

                        List<AudioTrack> tracks = playlist.getTracks();
                        for(AudioTrack track : tracks) {
                            track.setUserData(e);
                            manager.getScheduler().queue(track);
                        }
                        return;
                    }

                    if(type == Playback.PLAYNEXT) {
                        EmbedBuilder embed = new EmbedBuilder().setTitle("Adding `" + playlist.getTracks().size() + "` tracks to the front of the queue from playlist: `" + playlist.getName() + "`");
                        MessageDispatcher.reply(e, embed.build());

                        ArrayList<AudioTrack> tempQueue = new ArrayList<>(manager.getScheduler().queue);
                        manager.getScheduler().queue.clear();
                        List<AudioTrack> tracks = playlist.getTracks();
                        for(AudioTrack track: tracks) {
                            track.setUserData(e);
                            manager.getScheduler().queue(track);
                        }
                        manager.getScheduler().queue.addAll(tempQueue);
                        return;
                    }

                    if(type == Playback.BACKGROUND) {
                        EmbedBuilder embed = new EmbedBuilder().setTitle("Playlist as background currently not supported!");
                        MessageDispatcher.reply(e, embed.build());
                    }

                } catch(Exception ex) {
                    log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
                }
            }

            @Override
            public void noMatches() {
                EmbedBuilder embed = new EmbedBuilder().setTitle("No matches found using that parameter.");
                MessageDispatcher.reply(e, embed.build());
            }

            @Override
            public void loadFailed(FriendlyException ex) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Loading failed: " + ex.getMessage())
                        .setDescription("The most common cause for this error is trying to play age-restricted content. If the problem persists, please contact " + Yuuko.AUTHOR + ".");
                MessageDispatcher.reply(e, embed.build());
            }
        });
    }
}
