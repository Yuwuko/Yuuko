package basketbandit.core.modules.audio.commands;

import basketbandit.core.Configuration;
import basketbandit.core.modules.Command;
import basketbandit.core.modules.audio.ModuleAudio;
import basketbandit.core.modules.audio.handlers.AudioManagerHandler;
import basketbandit.core.modules.audio.handlers.GuildAudioManager;
import basketbandit.core.modules.audio.handlers.YouTubeSearchHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.List;

public class CommandPlay extends Command {

    public CommandPlay() {
        super("play", "basketbandit.core.modules.audio.ModuleAudio", null);
    }

    public CommandPlay(MessageReceivedEvent e) {
        super("play", "basketbandit.core.modules.audio.ModuleAudio", null);
        executeCommand(e);


    }

    public CommandPlay(MessageReceivedEvent e, String trackUrl) {
        super("play", "basketbandit.core.modules.audio.ModuleAudio", null);
        executeCommandAux(e, trackUrl);
    }

    /**
     * Executes command using MessageReceivedEvent e.
     * @param e; MessageReceivedEvent.
     * @return boolean; if the command executed correctly.
     */
    protected void executeCommand(MessageReceivedEvent e) {
        String[] commandArray = e.getMessage().getContentRaw().split("\\s+", 2);
        GuildAudioManager manager = AudioManagerHandler.getGuildAudioManager(e.getGuild().getId());

        if(commandArray.length == 1) {
            e.getGuild().getAudioManager().setSendingHandler(manager.sendHandler);
            e.getGuild().getAudioManager().openAudioConnection(e.getMember().getVoiceState().getChannel());

            if(manager.player.isPaused()) {
                manager.player.setPaused(false);
                e.getTextChannel().sendMessage("Player unpaused.").queue();
                new CommandCurrentTrack();
            }

        } else {
            e.getGuild().getAudioManager().setSendingHandler(manager.sendHandler);
            e.getGuild().getAudioManager().openAudioConnection(e.getMember().getVoiceState().getChannel());
            manager.player.setPaused(false);

            if(!commandArray[1].startsWith("https://www.youtube.com/watch?v=") || !commandArray[1].startsWith("https://youtu.be/")) {
                String trackUrl = YouTubeSearchHandler.search(commandArray[1]);

                if(trackUrl == null || trackUrl.equals("")) {
                    e.getTextChannel().sendMessage("Sorry " + e.getAuthor().getAsMention() + ", those search parameters failed to return a result, please check them and try again.").queue();
                } else {
                    loadAndPlay(manager, e.getChannel(), trackUrl, e);
                }

            } else {
                loadAndPlay(manager, e.getChannel(), commandArray[1], e);

            }
        }

    }

    private void executeCommandAux(MessageReceivedEvent e, String trackId) {
        GuildAudioManager manager = AudioManagerHandler.getGuildAudioManager(e.getGuild().getId());

        e.getGuild().getAudioManager().setSendingHandler(manager.sendHandler);
        e.getGuild().getAudioManager().openAudioConnection(e.getMember().getVoiceState().getChannel());
        manager.player.setPaused(false);

        loadAndPlay(manager, e.getChannel(), "https://www.youtube.com/watch?v=" + trackId, e);
    }

    /**
     * Loads a track from a given url and plays it if possible, else adds it to the queue.
     * @param manager ; GuildAudioManager.
     * @param channel ; MessageChannel.
     * @param url ; TrackUrl.
     */
    private void loadAndPlay(GuildAudioManager manager, final MessageChannel channel, String url, MessageReceivedEvent e) {
        final String trackUrl;

        if(url.startsWith("<") && url.endsWith(">")) {
            trackUrl = url.substring(1, url.length() - 1);
        } else {
            trackUrl = url;
        }

        AudioManagerHandler.getPlayerManager().loadItemOrdered(manager, trackUrl, new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack track) {

                if(!manager.scheduler.queue(track)) {
                    e.getTextChannel().sendMessage("Sorry " + e.getAuthor().getAsMention() + ", unable to queue track.").queue();
                    return;
                }

                String[] uri = track.getInfo().uri.split("=");

                EmbedBuilder queuedTrack = new EmbedBuilder()
                        .setColor(Color.RED)
                        .setAuthor(e.getMember().getEffectiveName() + " added to the queue!",null, e.getAuthor().getAvatarUrl())
                        .setTitle(track.getInfo().title, trackUrl)
                        .setThumbnail("https://img.youtube.com/vi/" + uri[1] + "/1.jpg")
                        .addField("Duration", ModuleAudio.getTimestamp(track.getDuration()), true)
                        .addField("Channel", track.getInfo().author, true)
                        .addField("Position in queue", manager.scheduler.queue.size()+"", false)
                        .setFooter("Version: " + Configuration.VERSION, e.getGuild().getMemberById(Configuration.BOT_ID).getUser().getAvatarUrl());

                channel.sendMessage(queuedTrack.build()).queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                List<AudioTrack> tracks = playlist.getTracks();

                channel.sendMessage("Adding **" + playlist.getTracks().size() +"** tracks to queue from playlist: " + playlist.getName()).queue();
                tracks.forEach(manager.scheduler::queue);
            }

            @Override
            public void noMatches() {
                channel.sendMessage(trackUrl).queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                channel.sendMessage("Could not play: " + exception.getMessage()).queue();
            }
        });
    }
}
