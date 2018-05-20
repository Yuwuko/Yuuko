package basketbandit.core.commands;

import basketbandit.core.modules.ModuleMusic;
import basketbandit.core.music.GuildMusicManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandShuffle extends Command {

    CommandShuffle() {
        super("shuffle", "music", null);
    }

    public CommandShuffle(MessageReceivedEvent e) {
        super("shuffle", "music", null);
        executeCommand(e);
    }

    /**
     * Executes command using MessageReceivedEvent e.
     *
     * @param e; MessageReceivedEvent.
     * @return boolean; if the command executed correctly.
     */
    protected boolean executeCommand(MessageReceivedEvent e) {
        GuildMusicManager manager = ModuleMusic.getMusicManager(e.getGuild().getId());

        e.getTextChannel().sendMessage(e.getAuthor().getAsMention() + " shuffled the queue.").queue();
        manager.scheduler.shuffle();
        return true;

    }

}
