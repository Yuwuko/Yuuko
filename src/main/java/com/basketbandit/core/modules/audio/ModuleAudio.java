package com.basketbandit.core.modules.audio;

import com.basketbandit.core.modules.C;
import com.basketbandit.core.modules.Module;
import com.basketbandit.core.modules.audio.commands.*;
import com.basketbandit.core.utils.Utils;
import com.google.api.services.youtube.model.SearchResult;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.HashMap;
import java.util.List;

public class ModuleAudio extends Module {

    public static HashMap<Long, List<SearchResult>> searchUsers = new HashMap<>();

    public ModuleAudio() {
        super("ModuleAudio", "moduleAudio");
    }

    public ModuleAudio(MessageReceivedEvent e, String[] command) {
        super("ModuleAudio", "moduleAudio");

        if(checkModuleSettings(e)) {
            return;
        }

        executeCommand(e, command);
    }

    public ModuleAudio(MessageReceivedEvent e, String url, boolean unused) {
        super("ModuleAudio", "moduleAudio");

        if(checkModuleSettings(e)) {
            return;
        }

        if(url != null) {
            new CommandPlay(e, url);
            searchUsers.remove(e.getAuthor().getIdLong());
        }
    }

    @Override
    protected void executeCommand(MessageReceivedEvent e, String[] command) {
        if(!e.getMember().getVoiceState().inVoiceChannel()) {
            Utils.sendMessage(e, "Sorry " + e.getAuthor().getAsMention() + ", you need to be in a voice channel to use that command! <:basketbandithehe:443069427832061953>");
            return;
        }

        if(command[0].equals(C.PLAY.getCommandName())) {
            new CommandPlay(e, command);
            return;
        }

        if(command[0].equals(C.PAUSE.getCommandName())) {
            new CommandPause(e, command);
            return;
        }

        if(command[0].equals(C.STOP.getCommandName())) {
            new CommandStop(e, command);
            return;
        }

        if(command[0].equals(C.SKIP.getCommandName())) {
            new CommandSkip(e, command);
            return;
        }

        if(command[0].equals(C.SET_BACKGROUND.getCommandName())) {
            new CommandSetBackground(e, command);
            return;
        }

        if(command[0].equals(C.UNSET_BACKGROUND.getCommandName())) {
            new CommandUnsetBackground(e, command);
            return;
        }

        if(command[0].equals(C.TRACK.getCommandName())) {
            new CommandCurrentTrack(e, command);
            return;
        }

        if(command[0].equals(C.SHUFFLE.getCommandName())) {
            new CommandShuffle(e, command);
            return;
        }

        if(command[0].equals(C.QUEUE.getCommandName())) {
            new CommandQueue(e, command);
            return;
        }

        if(command[0].equals(C.LAST_TRACK.getCommandName())) {
            new CommandLastTrack(e, command);
            return;
        }

        if(command[0].equals(C.TOGGLE_REPEAT.getCommandName())) {
            new CommandToggleRepeat(e, command);
            return;
        }

        if(command[0].equals(C.SEARCH.getCommandName())) {
            new CommandSearch(e, command);
            return;
        }

        Utils.sendMessage(e, "Sorry " + e.getAuthor().getAsMention() + ", you lack the required permissions to use that command.");
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
