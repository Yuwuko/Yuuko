package basketbandit.core.commands;

import basketbandit.core.modules.ModuleMusic;
import basketbandit.core.music.GuildMusicManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandPause extends Command {

    CommandPause() {
        super("pause", "basketbandit.core.modules.ModuleMusic", null);
    }

    public CommandPause(MessageReceivedEvent e) {
        super("pause", "basketbandit.core.modules.ModuleMusic", null);
        executeCommand(e);
    }

    /**
     * Executes command using MessageReceivedEvent e.
     * @param e; MessageReceivedEvent.
     * @return boolean; if the command executed correctly.
     */
    protected boolean executeCommand(MessageReceivedEvent e) {
        GuildMusicManager manager = ModuleMusic.getMusicManager(e.getGuild().getId());

        manager.player.setPaused(true);
        return true;

    }

}
