package basketbandit.core.modules.audio.commands;

import basketbandit.core.modules.Command;
import basketbandit.core.modules.audio.ModuleAudio;
import basketbandit.core.modules.audio.handlers.GuildAudioManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandToggleRepeat extends Command {

    public CommandToggleRepeat() {
        super("togglerepeat", "basketbandit.core.modules.audio.ModuleAudio", null);
    }

    public CommandToggleRepeat(MessageReceivedEvent e) {
        super("togglerepeat", "basketbandit.core.modules.audio.ModuleAudio", null);
        executeCommand(e);
    }

    /**
     * Executes command using MessageReceivedEvent e.
     * @param e; MessageReceivedEvent.
     * @return boolean; if the command executed correctly.
     */
    protected boolean executeCommand(MessageReceivedEvent e) {
        GuildAudioManager manager = ModuleAudio.getMusicManager(e.getGuild().getId());

        manager.scheduler.setRepeating(!manager.scheduler.isRepeating());
        e.getTextChannel().sendMessage(e.getAuthor().getAsMention() + " toggled repeat to: " + manager.scheduler.isRepeating()).queue();
        return true;
    }
}
