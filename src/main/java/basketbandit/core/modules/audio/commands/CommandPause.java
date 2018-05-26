package basketbandit.core.modules.audio.commands;

import basketbandit.core.modules.Command;
import basketbandit.core.modules.audio.ModuleAudio;
import basketbandit.core.modules.audio.handlers.GuildAudioManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandPause extends Command {

    public CommandPause() {
        super("pause", "basketbandit.core.modules.audio.ModuleAudio", null);
    }

    public CommandPause(MessageReceivedEvent e) {
        super("pause", "basketbandit.core.modules.audio.ModuleAudio", null);
        executeCommand(e);
    }

    /**
     * Executes command using MessageReceivedEvent e.
     * @param e; MessageReceivedEvent.
     * @return boolean; if the command executed correctly.
     */
    protected boolean executeCommand(MessageReceivedEvent e) {
        GuildAudioManager manager = ModuleAudio.getMusicManager(e.getGuild().getId());

        manager.player.setPaused(true);
        return true;

    }

}
