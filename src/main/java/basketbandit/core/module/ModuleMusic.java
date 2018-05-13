package basketbandit.core.module;

import basketbandit.core.BasketBandit;
import basketbandit.core.Configuration;
import basketbandit.core.music.GuildMusicManager;
import basketbandit.core.music.TrackScheduler;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.bandcamp.BandcampAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.http.HttpAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.local.LocalAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.soundcloud.SoundCloudAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.twitch.TwitchStreamAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.vimeo.VimeoAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class ModuleMusic {

    private MessageReceivedEvent e;
    private String[] command;
    private BasketBandit core;

    private final GuildMusicManager manager;
    private final TrackScheduler scheduler;
    private final AudioPlayer player;
    private final AudioPlayerManager playerManager;

    ModuleMusic(MessageReceivedEvent e, BasketBandit core) {
        this.e = e;
        command = e.getMessage().getContentRaw().split("\\s+", 2);
        this.core = core;

        this.playerManager = new DefaultAudioPlayerManager();
        playerManager.registerSourceManager(new YoutubeAudioSourceManager());
        playerManager.registerSourceManager(new SoundCloudAudioSourceManager());
        playerManager.registerSourceManager(new BandcampAudioSourceManager());
        playerManager.registerSourceManager(new VimeoAudioSourceManager());
        playerManager.registerSourceManager(new TwitchStreamAudioSourceManager());
        playerManager.registerSourceManager(new HttpAudioSourceManager());
        playerManager.registerSourceManager(new LocalAudioSourceManager());

        playerManager.setFrameBufferDuration(1);

        manager = getMusicManager();
        scheduler = manager.scheduler;
        player = manager.player;

        if(!e.getMember().getVoiceState().inVoiceChannel()) {
            e.getTextChannel().sendMessage("Sorry " + e.getAuthor().getAsMention() + ", you need to be in a voice channel to use that command! <:hehe:445200711438041090>").queue();
            return;
        }

        if(command[0].toLowerCase().equals(Configuration.PREFIX + "play")) {
            commandPlay();
        } else if(command[0].toLowerCase().equals(Configuration.PREFIX + "pause")) {
            commandPause();
        } else if(command[0].toLowerCase().equals(Configuration.PREFIX + "stop")) {
            commandStop();
        } else if(command[0].toLowerCase().equals(Configuration.PREFIX + "skip")) {
            commandSkip();
        } else if(command[0].toLowerCase().equals(Configuration.PREFIX + "track")) {
            commandTrack();
        } else if(command[0].toLowerCase().equals(Configuration.PREFIX + "shuffle")) {
            commandShuffle();
        } else if(command[0].toLowerCase().equals(Configuration.PREFIX + "queue")) {
            commandQueue();
        }
    }

    /**
     * Asks the bot to join the channel and play the given URL.
     */
    private void commandPlay() {
        if(command.length == 1) {
            player.setPaused(false);
        } else {
            e.getGuild().getAudioManager().setSendingHandler(manager.sendHandler);
            e.getGuild().getAudioManager().openAudioConnection(e.getMember().getVoiceState().getChannel());
            player.setPaused(false);

            if(!command[1].startsWith("https://www.youtube.com/watch?v=")) {
                loadAndPlay(manager, e.getChannel(), searchYouTube());
            } else {
                loadAndPlay(manager, e.getChannel(), command[1]);
            }
        }
    }

    /**
     * Stops the player and deletes queue.
     */
    private void commandStop() {
        scheduler.queue.clear();
        player.stopTrack();
        player.setPaused(false);
        e.getTextChannel().sendMessage("Playback has been stopped and the queue has been cleared.").queue();
        e.getGuild().getAudioManager().closeAudioConnection();
    }

    /**
     * Pauses the player.
     */
    private void commandPause() {
        player.setPaused(true);
    }

    /**
     * Skips the current song.
     */
    private void commandSkip() {
        e.getTextChannel().sendMessage("Skipping track...").queue();
        scheduler.nextTrack();
    }

    /**
     * Provides track information.
     */
    private void commandTrack() {
        EmbedBuilder queuedTrack = new EmbedBuilder()
                .setColor(Color.WHITE)
                .setTitle(player.getPlayingTrack().getInfo().title, player.getPlayingTrack().getInfo().uri)
                .setDescription("Channel: " + player.getPlayingTrack().getInfo().author + ", Duration: " + getTimestamp(player.getPlayingTrack().getInfo().length) )
                .setFooter("Version: " + Configuration.VERSION + ", Requested by " + e.getMember().getEffectiveName(), e.getGuild().getMemberById(Configuration.BOT_ID).getUser().getAvatarUrl());

        e.getTextChannel().sendMessage(queuedTrack.build()).queue();
    }

    /**
     * Shuffles the queue.
     */
    private void commandShuffle() {
        scheduler.shuffle();
        e.getTextChannel().sendMessage(e.getAuthor().getAsMention() + " shuffled the queue.").queue();
    }

    /**
     * Returns the current queue.
     */
    private void commandQueue() {
        String queue = "";
        int i = 1;

        for(AudioTrack track: scheduler.queue) {
            queue += i + ": " + track.getInfo().title + ", (" + getTimestamp(track.getInfo().length) + ") \n";
            i++;
            if(i > 10) {
                break;
            }
        }
        i--;

        if(i > 1) {
            EmbedBuilder nextTracks = new EmbedBuilder()
                    .setColor(Color.RED)
                    .setAuthor("Hey " + e.getMember().getEffectiveName() + ",", null, e.getAuthor().getAvatarUrl())
                    .setTitle("Here are the next " + i + " tracks in the queue:")
                    .setDescription(queue)
                    .setFooter("Version: " + Configuration.VERSION, e.getGuild().getMemberById(Configuration.BOT_ID).getUser().getAvatarUrl());

            e.getTextChannel().sendMessage(nextTracks.build()).queue();
        } else {
            e.getTextChannel().sendMessage("Sorry " + e.getAuthor().getAsMention() + ", the queue is empty!").queue();
        }
    }

    /**
     * Finds and sets the guild's music manager.
     * @return GuildMusicManager.
     */
    private GuildMusicManager getMusicManager() {
        GuildMusicManager manager;
        if(core.getGuildMusicManager(e.getGuild().getId()) == null) {
            synchronized(core.getGuildMusicManagers()) {
                manager = core.getGuildMusicManager(e.getGuild().getId());
                if(manager == null) {
                    manager = new GuildMusicManager(playerManager);
                    manager.player.setVolume(35);
                    core.addGuildMusicManager(e.getGuild().getId(), manager);
                }
            }
        } else {
            manager = core.getGuildMusicManager(e.getGuild().getId());
        }
        return manager;
    }

    /**
     * Loads a track from a given url and plays it if possible, else adds it to the queue.
     * @param manager; GuildMusicManager.
     * @param channel; MessageChannel.
     * @param url; TrackUrl.
     */
    private void loadAndPlay(GuildMusicManager manager, final MessageChannel channel, String url) {
        final String trackUrl;

        if(url.startsWith("<") && url.endsWith(">")) {
            trackUrl = url.substring(1, url.length() - 1);
        } else {
            trackUrl = url;
        }

        playerManager.loadItemOrdered(manager, trackUrl, new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack track) {
                manager.scheduler.queue(track);

                EmbedBuilder queuedTrack = new EmbedBuilder()
                        .setColor(Color.RED)
                        .setAuthor(e.getMember().getEffectiveName() + " added to the queue!",null, e.getAuthor().getAvatarUrl())
                        .setTitle(track.getInfo().title, trackUrl)
                        .setDescription("Channel: " + track.getInfo().author + ", Duration: " + getTimestamp(track.getDuration()) )
                        .setFooter("Version: " + Configuration.VERSION, e.getGuild().getMemberById(Configuration.BOT_ID).getUser().getAvatarUrl());

                channel.sendMessage(queuedTrack.build()).queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                AudioTrack firstTrack = playlist.getSelectedTrack();
                List<AudioTrack> tracks = playlist.getTracks();

                if(firstTrack == null) {
                    firstTrack = playlist.getTracks().get(0);
                }

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

    /**
     * Searches youtube using command[1] and returns the first result.
     * @return youtube video url.
     */
    private String searchYouTube() {
        try {
            YouTube youtube;
            youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), request -> {
            }).setApplicationName("basketbandit-204012").build();

            YouTube.Search.List search = youtube.search().list("id,snippet");

            search.setKey(Configuration.GOOGLE_API);
            search.setQ(command[1]);
            search.setType("video");
            search.setFields("items(id/videoId)");
            search.setMaxResults(1L);

            SearchListResponse searchResponse = search.execute();
            List<SearchResult> searchResultList = searchResponse.getItems();
            SearchResult result = searchResultList.get(0);

            // Return the URL.
            return "https://www.youtube.com/watch?v=" + result.getId().getVideoId();

        } catch (GoogleJsonResponseException e) {
            System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
                    + e.getDetails().getMessage());
        } catch (IOException e) {
            System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }

        return "err...";
    }

    /**
     * Gets current songs timeStamp.
     * @param milliseconds; how many milliseconds of the song has played.
     * @return formatted timeStamp.
     */
    private static String getTimestamp(long milliseconds) {
        int seconds = (int) (milliseconds / 1000) % 60 ;
        int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
        int hours   = (int) ((milliseconds / (1000 * 60 * 60)) % 24);

        if(hours > 0) {
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format("%02d:%02d", minutes, seconds);
        }
    }

}
