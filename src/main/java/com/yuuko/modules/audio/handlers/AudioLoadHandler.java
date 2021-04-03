package com.yuuko.modules.audio.handlers;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.yuuko.MessageDispatcher;
import com.yuuko.Yuuko;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.utilities.TextUtilities;
import com.yuuko.utilities.Utilities;
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
     * @param context {@link MessageEvent}
     */
    public static void loadAndPlay(GuildAudioManager manager, MessageEvent context, Playback type) {
        final String param = context.getParameters();
        final String trackUrl = param.startsWith("<") && param.endsWith(">") ? param.substring(1, param.length() - 1) : param;

        AudioManager.getPlayerManager().loadItemOrdered(manager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                try {
                    track.setUserData(context);

                    EmbedBuilder embed = new EmbedBuilder().setTitle(track.getInfo().title, trackUrl)
                            .setThumbnail(Utilities.getAudioTrackImage(track))
                            .addField(context.i18n("duration", "audio_load"), TextUtilities.getTimestamp(track.getDuration()), true)
                            .addField(context.i18n("channel", "audio_load"), track.getInfo().author, true)
                            .addField(context.i18n("position", "audio_load"), manager.getScheduler().queue.size() + "", false)
                            .setFooter(Yuuko.STANDARD_STRINGS.get(1) + context.getAuthor().getAsTag(), context.getAuthor().getEffectiveAvatarUrl());

                    switch(type) {
                        case PLAY -> embed.setAuthor(context.i18n("play", "audio_load").formatted(context.getAuthor().getAsTag()), null, context.getAuthor().getAvatarUrl());
                        case PLAYNEXT -> embed.setAuthor(context.i18n("playnext","audio_load").formatted(context.getAuthor().getAsTag()), null, context.getAuthor().getAvatarUrl());
                        case BACKGROUND -> embed.setAuthor(context.i18n("background", "audio_load").formatted(context.getAuthor().getAsTag()),null, context.getAuthor().getAvatarUrl());
                        default -> embed.setAuthor(context.i18n("default", "audio_load").formatted(context.getAuthor().getAsTag()),null, context.getAuthor().getAvatarUrl());
                    }
                    MessageDispatcher.reply(context, embed.build());


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
                        EmbedBuilder embed = new EmbedBuilder().setTitle(context.i18n("playlist_load", "audio_load").formatted(playlist.getTracks().size(), playlist.getName()));
                        MessageDispatcher.reply(context, embed.build());

                        List<AudioTrack> tracks = playlist.getTracks();
                        for(AudioTrack track : tracks) {
                            track.setUserData(context);
                            manager.getScheduler().queue(track);
                        }
                        return;
                    }

                    if(type == Playback.PLAYNEXT) {
                        EmbedBuilder embed = new EmbedBuilder().setTitle(context.i18n("playlist_next", "audio_load").formatted(playlist.getTracks().size(), playlist.getName()));
                        MessageDispatcher.reply(context, embed.build());

                        ArrayList<AudioTrack> tempQueue = new ArrayList<>(manager.getScheduler().queue);
                        manager.getScheduler().queue.clear();
                        List<AudioTrack> tracks = playlist.getTracks();
                        for(AudioTrack track: tracks) {
                            track.setUserData(context);
                            manager.getScheduler().queue(track);
                        }
                        manager.getScheduler().queue.addAll(tempQueue);
                        return;
                    }

                    if(type == Playback.BACKGROUND) {
                        EmbedBuilder embed = new EmbedBuilder().setTitle(context.i18n("no_support", "audio_load"));
                        MessageDispatcher.reply(context, embed.build());
                    }

                } catch(Exception ex) {
                    log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
                }
            }

            @Override
            public void noMatches() {
                EmbedBuilder embed = new EmbedBuilder().setTitle(context.i18n("invalid_param", "audio_load"));
                MessageDispatcher.reply(context, embed.build());
            }

            @Override
            public void loadFailed(FriendlyException ex) {
                EmbedBuilder embed = new EmbedBuilder().setTitle(context.i18n("load_fail_title", "audio_load").formatted(ex.getMessage()))
                        .setDescription(context.i18n("load_fail_desc", "audio_load").formatted(Yuuko.AUTHOR));
                MessageDispatcher.reply(context, embed.build());
            }
        });
    }
}
