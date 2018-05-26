package basketbandit.core.modules.audio.commands;

import basketbandit.core.modules.Command;
import basketbandit.core.modules.audio.ModuleAudio;
import basketbandit.core.modules.audio.handlers.GuildAudioManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandShuffle extends Command {

    public CommandShuffle() {
        super("shuffle", "basketbandit.core.modules.handlers.ModuleAudio", null);
    }

    public CommandShuffle(MessageReceivedEvent e) {
        super("shuffle", "basketbandit.core.modules.handlers.ModuleAudio", null);
        executeCommand(e);
    }

    /**
     * Executes command using MessageReceivedEvent e.
     *
     * @param e; MessageReceivedEvent.
     * @return boolean; if the command executed correctly.
     */
    protected boolean executeCommand(MessageReceivedEvent e) {
        GuildAudioManager manager = ModuleAudio.getMusicManager(e.getGuild().getId());

        e.getTextChannel().sendMessage(e.getAuthor().getAsMention() + " shuffled the queue.").queue();
        manager.scheduler.shuffle();
        return true;

    }

}
