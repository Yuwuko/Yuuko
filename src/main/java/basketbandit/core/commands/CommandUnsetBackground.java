package basketbandit.core.commands;

import basketbandit.core.modules.ModuleMusic;
import basketbandit.core.music.GuildMusicManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandUnsetBackground extends Command {

    CommandUnsetBackground() {
        super("unsetbackground", "basketbandit.core.modules.ModuleMusic", null);
    }

    public CommandUnsetBackground(MessageReceivedEvent e) {
        super("unsetbackground", "basketbandit.core.modules.ModuleMusic", null);
        executeCommand(e);
    }

    /**
     * Executes command using MessageReceivedEvent e.
     * @param e; MessageReceivedEvent.
     * @return boolean; if the command executed correctly.
     */
    protected boolean executeCommand(MessageReceivedEvent e) {
        GuildMusicManager manager = ModuleMusic.getMusicManager(e.getGuild().getId());

        manager.scheduler.setBackground(null);
        return true;

    }

}
