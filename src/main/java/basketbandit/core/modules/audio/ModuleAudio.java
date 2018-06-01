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
        super("ModuleAudio", "moduleAudio");
    }

    public ModuleAudio(MessageReceivedEvent e, String prefix) {
        super("ModuleAudio", "moduleAudio");

        if(!checkModuleSettings(e)) {
            return;
        }

        executeCommand(e, prefix);
    }

    public ModuleAudio(MessageReceivedEvent e, String url, boolean unused) {
        super("ModuleAudio", "moduleAudio");

        if(!checkModuleSettings(e)) {
            return;
        }

        if(url != null) {
            new CommandPlay(e, url);
            searchUsers.remove(e.getAuthor().getIdLong());
        }
    }

    protected void executeCommand(MessageReceivedEvent e, String prefix) {
        String[] commandArray = e.getMessage().getContentRaw().toLowerCase().split("\\s+", 2);
        String command = commandArray[0].substring(prefix.length());

        if(!e.getMember().getVoiceState().inVoiceChannel()) {
            e.getTextChannel().sendMessage("Sorry " + e.getAuthor().getAsMention() + ", you need to be in a voice channel to use that command! <:hehe:445200711438041090>").queue();
            return;
        }

        if(command.equals(C.PLAY.getCommandName())) {
            new CommandPlay(e);
            return;
        }

        if(command.equals(C.PAUSE.getCommandName())) {
            new CommandPause(e);
            return;
        }

        if(command.equals(C.STOP.getCommandName())) {
            new CommandStop(e);
            return;
        }

        if(command.equals(C.SKIP.getCommandName())) {
            new CommandSkip(e);
            return;
        }

        if(command.equals(C.SET_BACKGROUND.getCommandName())) {
            new CommandSetBackground(e);
            return;
        }

        if(command.equals(C.UNSET_BACKGROUND.getCommandName())) {
            new CommandUnsetBackground(e);
            return;
        }

        if(command.equals(C.TRACK.getCommandName())) {
            new CommandCurrentTrack(e);
            return;
        }

        if(command.equals(C.SHUFFLE.getCommandName())) {
            new CommandShuffle(e);
            return;
        }

        if(command.equals(C.QUEUE.getCommandName())) {
            new CommandQueue(e);
            return;
        }

        if(command.equals(C.LAST_TRACK.getCommandName())) {
            new CommandLastTrack(e);
            return;
        }

        if(command.equals(C.TOGGLE_REPEAT.getCommandName())) {
            new CommandToggleRepeat(e);
            return;
        }

        if(command.equals(C.SEARCH.getCommandName())) {
            new CommandSearch(e);
            return;
        }

        e.getTextChannel().sendMessage("Sorry " + e.getAuthor().getAsMention() + ", that command was unable to execute correctly.").queue();
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
