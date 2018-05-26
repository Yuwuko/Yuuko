package basketbandit.core.modules.audio.commands;

import basketbandit.core.modules.Command;
import basketbandit.core.modules.audio.ModuleAudio;
import basketbandit.core.modules.audio.handlers.GuildAudioManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandUnsetBackground extends Command {

    public CommandUnsetBackground() {
        super("unsetbackground", "basketbandit.core.modules.audio.ModuleAudio", null);
    }

    public CommandUnsetBackground(MessageReceivedEvent e) {
        super("unsetbackground", "basketbandit.core.modules.audio.ModuleAudio", null);
        executeCommand(e);
    }

    /**
     * Executes command using MessageReceivedEvent e.
     * @param e; MessageReceivedEvent.
     * @return boolean; if the command executed correctly.
     */
    protected boolean executeCommand(MessageReceivedEvent e) {
        GuildAudioManager manager = ModuleAudio.getMusicManager(e.getGuild().getId());

        manager.scheduler.setBackground(null);
        return true;

    }

}
