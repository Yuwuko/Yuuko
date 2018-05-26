package basketbandit.core.modules.audio;

import basketbandit.core.modules.C;
import basketbandit.core.modules.Module;
import basketbandit.core.modules.audio.commands.*;
import com.google.api.services.youtube.model.SearchResult;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.HashMap;
import java.util.List;

public class ModuleAudio extends Module {

    public static HashMap<Long, List<SearchResult>> searchUsers = new HashMap<>();

    public ModuleAudio() {
        super("ModuleAudio", "modMusic");
    }

    public ModuleAudio(MessageReceivedEvent e) {
        super("ModuleAudio", "modMusic");

        if(!checkModuleSettings(e)) {
            return;
        }

        if(!executeCommand(e)) {
            e.getTextChannel().sendMessage("Sorry " + e.getAuthor().getAsMention() + ", that command was unable to execute correctly.").queue();
        }

    }

    public ModuleAudio(MessageReceivedEvent e, String url) {
        super("ModuleAudio", "modMusic");

        if(!checkModuleSettings(e)) {
            return;
        }

        if(url != null) {
            new CommandPlay(e, url);
            searchUsers.remove(e.getAuthor().getIdLong());
        }
    }

    protected boolean executeCommand(MessageReceivedEvent e) {
        String[] commandArray = e.getMessage().getContentRaw().toLowerCase().split("\\s+", 2);
        String command = commandArray[0];

        if(!e.getMember().getVoiceState().inVoiceChannel()) {
            e.getTextChannel().sendMessage("Sorry " + e.getAuthor().getAsMention() + ", you need to be in a voice channel to use that command! <:hehe:445200711438041090>").queue();
            return true;
        }

        if(command.equals(C.PLAY.getEffectiveName())) {
            new CommandPlay(e);
            return true;
        }

        if(command.equals(C.PAUSE.getEffectiveName())) {
            new CommandPause(e);
            return true;
        }

        if(command.equals(C.STOP.getEffectiveName())) {
            new CommandStop(e);
            return true;
        }

        if(command.equals(C.SKIP.getEffectiveName())) {
            new CommandSkip(e);
            return true;
        }

        if(command.equals(C.SET_BACKGROUND.getEffectiveName())) {
            new CommandSetBackground(e);
            return true;
        }

        if(command.equals(C.UNSET_BACKGROUND.getEffectiveName())) {
            new CommandUnsetBackground(e);
            return true;
        }

        if(command.equals(C.TRACK.getEffectiveName())) {
            new CommandCurrentTrack(e);
            return true;
        }

        if(command.equals(C.SHUFFLE.getEffectiveName())) {
            new CommandShuffle(e);
            return true;
        }

        if(command.equals(C.QUEUE.getEffectiveName())) {
            new CommandQueue(e);
            return true;
        }

        if(command.equals(C.LAST_TRACK.getEffectiveName())) {
            new CommandLastTrack(e);
            return true;
        }

        if(command.equals(C.TOGGLE_REPEAT.getEffectiveName())) {
            new CommandToggleRepeat(e);
            return true;
        }

        if(command.equals(C.SEARCH.getEffectiveName())) {
            new CommandSearch(e);
            return true;
        }

        return false;
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
