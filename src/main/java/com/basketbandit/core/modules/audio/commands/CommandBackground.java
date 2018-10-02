package com.basketbandit.core.modules.audio.commands;

import com.basketbandit.core.Configuration;
import com.basketbandit.core.modules.Command;
import com.basketbandit.core.modules.audio.ModuleAudio;
import com.basketbandit.core.modules.audio.handlers.AudioManagerHandler;
import com.basketbandit.core.modules.audio.handlers.GuildAudioManager;
import com.basketbandit.core.modules.audio.handlers.YouTubeSearchHandler;
import com.basketbandit.core.utils.Utils;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;

public class CommandBackground extends Command {

    public CommandBackground() {
        super("background", "com.basketbandit.core.modules.audio.ModuleAudio", new String[]{"-background", "-background [url]", "-background [term]"}, null);
    }

    public CommandBackground(MessageReceivedEvent e, String[] command) {
        executeCommand(e, command);
    }

    @Override
    protected void executeCommand(MessageReceivedEvent e, String[] command) {

        GuildAudioManager manager = AudioManagerHandler.getGuildAudioManager(e.getGuild().getId());

        if(command.length > 1) {
            e.getGuild().getAudioManager().setSendingHandler(manager.sendHandler);
            e.getGuild().getAudioManager().openAudioConnection(e.getMember().getVoiceState().getChannel());
            manager.player.setPaused(false);

            if(command[1].startsWith("https://") || command[1].startsWith("http://")) {
                setAndPlay(manager, e.getChannel(), command[1], e);

            } else {
                String trackUrl = YouTubeSearchHandler.search(command[1]);

                if(trackUrl == null) {
                    Utils.sendMessage(e, "Sorry " + e.getAuthor().getAsMention() + ", those search parameters failed to return a result, please check them and try again.");
                } else {
                    setAndPlay(manager, e.getChannel(), trackUrl, e);
                }
            }
        } else {
            // If no parameters are given, unset the background track.
            Utils.sendMessage(e, "Background track removed.");
            manager.scheduler.setBackground(null);
        }
    }

    /**
     * Loads a track from a given url and plays it if possible, else adds it to the queue.
     * @param manager; GuildAudioManager.
     * @param channel; MessageChannel.
     * @param url; TrackUrl.
     */
    private void setAndPlay(GuildAudioManager manager, final MessageChannel channel, String url, MessageReceivedEvent e) {
        final String trackUrl;

        if(url.startsWith("<") && url.endsWith(">")) {
            trackUrl = url.substring(1, url.length() - 1);
        } else {
            trackUrl = url;
        }

        AudioManagerHandler.getPlayerManager().loadItemOrdered(manager, trackUrl, new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack track) {
                if(manager.scheduler.queue.size() == 0) {
                    manager.scheduler.queue(track);
                }

                manager.scheduler.setBackground(track);

                EmbedBuilder queuedTrack = new EmbedBuilder()
                        .setColor(Color.DARK_GRAY)
                        .setAuthor(e.getMember().getEffectiveName() + " set the background music!",null, e.getAuthor().getAvatarUrl())
                        .setTitle(track.getInfo().title, trackUrl)
                        .setDescription("Channel: " + track.getInfo().author + ", Duration: " + ModuleAudio.getTimestamp(track.getDuration()))
                        .setFooter("Version: " + Configuration.VERSION, e.getGuild().getMemberById(Configuration.BOT_ID).getUser().getAvatarUrl());

                channel.sendMessage(queuedTrack.build()).queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                // Playlist as background not currently supported but method needed implementation.
                channel.sendMessage("Sorry, adding playlists to the background is currently unsupported but it will be soon!").queue();
            }

            @Override
            public void noMatches() {
                channel.sendMessage(trackUrl).queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                channel.sendMessage("Could not add track to the background: " + exception.getMessage()).queue();
            }
        });
    }
}
