package basketbandit.core.commands;

import basketbandit.core.modules.ModuleMusic;
import basketbandit.core.music.GuildMusicManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandToggleRepeat extends Command {

    CommandToggleRepeat() {
        super("togglerepeat", "music", null);
    }

    public CommandToggleRepeat(MessageReceivedEvent e) {
        super("togglerepeat", "music", null);
        executeCommand(e);
    }

    /**
     * Executes command using MessageReceivedEvent e.
     * @param e; MessageReceivedEvent.
     * @return boolean; if the command executed correctly.
     */
    protected boolean executeCommand(MessageReceivedEvent e) {
        GuildMusicManager manager = ModuleMusic.getMusicManager(e.getGuild().getId());

        manager.scheduler.setRepeating(!manager.scheduler.isRepeating());
        e.getTextChannel().sendMessage(e.getAuthor().getAsMention() + " toggled repeat to: " + manager.scheduler.isRepeating()).queue();
        return true;
    }
}
