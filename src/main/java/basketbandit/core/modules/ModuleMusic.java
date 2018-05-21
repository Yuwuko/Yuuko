// Program: BasketBandit (Discord Bot)
// Programmer: Joshua Mark Hunt
// Version: 20/05/2018 - JDK 10.0.1

package basketbandit.core.modules;

import basketbandit.core.Configuration;
import basketbandit.core.commands.*;
import basketbandit.core.music.GuildMusicManager;
import basketbandit.core.music.MusicManagerHandler;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.io.IOException;
import java.util.List;

public class ModuleMusic {

    public ModuleMusic(MessageReceivedEvent e) {
        String[] commandArray = e.getMessage().getContentRaw().toLowerCase().split("\\s+", 2);
        String command = commandArray[0];

        if(!e.getMember().getVoiceState().inVoiceChannel()) {
            e.getTextChannel().sendMessage("Sorry " + e.getAuthor().getAsMention() + ", you need to be in a voice channel to use that command! <:hehe:445200711438041090>").queue();
            return;
        }

        if(command.equals(C.PLAY.getEffectiveName())) {
            new CommandPlay(e);
            return;
        }

        if(command.equals(C.PAUSE.getEffectiveName())) {
            new CommandPause(e);
            return;
        }

        if(command.equals(C.STOP.getEffectiveName())) {
            new CommandStop(e);
            return;
        }

        if(command.equals(C.SKIP.getEffectiveName())) {
            new CommandSkip(e);
            return;
        }

        if(command.equals(C.SET_BACKGROUND.getEffectiveName())) {
            new CommandSetBackground(e);
            return;
        }

        if(command.equals(C.UNSET_BACKGROUND.getEffectiveName())) {
            new CommandUnsetBackground(e);
            return;
        }

        if(command.equals(C.TRACK.getEffectiveName())) {
            new CommandTrack(e);
            return;
        }

        if(command.equals(C.SHUFFLE.getEffectiveName())) {
            new CommandShuffle(e);
            return;
        }

        if(command.equals(C.QUEUE.getEffectiveName())) {
            new CommandQueue(e);
            return;
        }

        if(command.equals(C.LAST_TRACK.getEffectiveName())) {
            new CommandLastTrack(e);
            return;
        }

        if(command.equals(C.TOGGLE_REPEAT.getEffectiveName())) {
            new CommandToggleRepeat(e);
            return;
        }

        System.out.println("[WARNING] End of constructor reached for ModuleMusic.");
    }

    /**
     * Finds and sets the guild's music manager.
     * @return GuildMusicManager.
     */
    public static GuildMusicManager getMusicManager(String id) {
        GuildMusicManager manager;
        if(MusicManagerHandler.getGuildMusicManager(id) == null) {
            synchronized(MusicManagerHandler.getGuildMusicManagers()) {
                manager = MusicManagerHandler.getGuildMusicManager(id);
                if(manager == null) {
                    manager = new GuildMusicManager(MusicManagerHandler.getPlayerManager());
                    manager.player.setVolume(50);
                    MusicManagerHandler.addGuildMusicManager(id, manager);
                }
            }
        } else {
            manager = MusicManagerHandler.getGuildMusicManager(id);
        }
        return manager;
    }

    /**
     * Searches youtube using command[1] and returns the first result.
     * @return youtube video url.
     */
    public static String searchYouTube(MessageReceivedEvent e) {
        String[] commandArray = e.getMessage().getContentRaw().split("\\s+",2);

        try {
            YouTube youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), request -> {
            }).setApplicationName("basketbandit-204012").build();

            YouTube.Search.List search = youtube.search().list("id,snippet");

            search.setKey(Configuration.GOOGLE_API);
            search.setQ(commandArray[1]);
            search.setType("video");
            search.setFields("items(id/videoId)");
            search.setMaxResults(1L);

            SearchListResponse searchResponse = search.execute();
            List<SearchResult> searchResultList = searchResponse.getItems();
            SearchResult result = searchResultList.get(0);

            // Return the URL.
            return "https://www.youtube.com/watch?v=" + result.getId().getVideoId();

        } catch (GoogleJsonResponseException ex) {
            System.err.println("There was a service error: " + ex.getDetails().getCode() + " : " + ex.getDetails().getMessage());
        } catch (IOException cx) {
            System.err.println("There was an IO error: " + cx.getCause() + " : " + e.getMessage());
        } catch (Throwable tx) {
            tx.printStackTrace();
        }

        return "err...";
    }

    /**
     * Gets current songs timeStamp.
     * @param milliseconds; how many milliseconds of the song has played.
     * @return formatted timeStamp.
     */
    public static String getTimestamp(long milliseconds) {
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
