package basketbandit.core.commands;

import basketbandit.core.Configuration;
import basketbandit.core.modules.ModuleMusic;
import basketbandit.core.music.GuildMusicManager;
import basketbandit.core.music.MusicManagerHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;

public class CommandSetBackground extends Command {

    CommandSetBackground() {
        super("setbackground", "music", null);
    }

    public CommandSetBackground(MessageReceivedEvent e) {
        super("setbackground", "music", null);
        executeCommand(e);
    }

    /**
     * Executes command using MessageReceivedEvent e.
     * @param e; MessageReceivedEvent.
     * @return boolean; if the command executed correctly.
     */
    protected boolean executeCommand(MessageReceivedEvent e) {
        String[] commandArray = e.getMessage().getContentRaw().split("\\s+", 2);
        GuildMusicManager manager = ModuleMusic.getMusicManager(e.getGuild().getId());

        e.getGuild().getAudioManager().setSendingHandler(manager.sendHandler);
        e.getGuild().getAudioManager().openAudioConnection(e.getMember().getVoiceState().getChannel());
        manager.player.setPaused(false);

        if(!commandArray[1].startsWith("https://www.youtube.com/watch?v=") || !commandArray[1].startsWith("https://youtu.be/")) {
            loadAndPlay(manager, e.getChannel(), ModuleMusic.searchYouTube(e), e, true);
        } else {
            loadAndPlay(manager, e.getChannel(), commandArray[1], e, true);
        }

        return true;

    }

    /**
     * Loads a track from a given url and plays it if possible, else adds it to the queue.
     * @param manager; GuildMusicManager.
     * @param channel; MessageChannel.
     * @param url; TrackUrl.
     */
    private void loadAndPlay(GuildMusicManager manager, final MessageChannel channel, String url, MessageReceivedEvent e, final boolean addPlaylist) {
        final String trackUrl;

        if(url.startsWith("<") && url.endsWith(">")) {
            trackUrl = url.substring(1, url.length() - 1);
        } else {
            trackUrl = url;
        }

        MusicManagerHandler.getPlayerManager().loadItemOrdered(manager, trackUrl, new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack track) {
                if(manager.scheduler.queue.size() == 0) {
                    manager.scheduler.queue(track);
                }

                manager.scheduler.setBackground(track);

                EmbedBuilder queuedTrack = new EmbedBuilder()
                        .setColor(Color.RED)
                        .setAuthor(e.getMember().getEffectiveName() + " set the background music!",null, e.getAuthor().getAvatarUrl())
                        .setTitle(track.getInfo().title, trackUrl)
                        .setDescription("Channel: " + track.getInfo().author + ", Duration: " + ModuleMusic.getTimestamp(track.getDuration()))
                        .setFooter("Version: " + Configuration.VERSION, e.getGuild().getMemberById(Configuration.BOT_ID).getUser().getAvatarUrl());

                channel.sendMessage(queuedTrack.build()).queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                // Playlist as background not currently supported but method needed implementation.
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
