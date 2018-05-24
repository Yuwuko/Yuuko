package basketbandit.core.commands;

import basketbandit.core.modules.ModuleMusic;
import basketbandit.core.music.GuildMusicManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandStop extends Command {

    CommandStop() {
        super("stop", "basketbandit.core.modules.ModuleMusic", null);
    }

    public CommandStop(MessageReceivedEvent e) {
        super("stop", "basketbandit.core.modules.ModuleMusic", null);
        executeCommand(e);
    }

    /**
     * Executes command using MessageReceivedEvent e.
     * @param e; MessageReceivedEvent.
     * @return boolean; if the command executed correctly.
     */
    protected boolean executeCommand(MessageReceivedEvent e) {
        GuildMusicManager manager = ModuleMusic.getMusicManager(e.getGuild().getId());

        manager.scheduler.queue.clear();
        manager.player.stopTrack();
        manager.player.setPaused(false);
        e.getGuild().getAudioManager().setSendingHandler(null);
        e.getGuild().getAudioManager().closeAudioConnection();
        e.getTextChannel().sendMessage("Playback has been stopped and the queue has been cleared.").queue();
        return true;

    }

}
