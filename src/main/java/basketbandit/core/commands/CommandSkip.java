package basketbandit.core.commands;

import basketbandit.core.modules.ModuleMusic;
import basketbandit.core.music.GuildMusicManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandSkip extends Command {

    CommandSkip() {
        super("skip", "music", null);
    }

    public CommandSkip(MessageReceivedEvent e) {
        super("skip", "music", null);
        executeCommand(e);
    }

    /**
     * Executes command using MessageReceivedEvent e.
     * @param e; MessageReceivedEvent.
     * @return boolean; if the command executed correctly.
     */
    protected boolean executeCommand(MessageReceivedEvent e) {
        GuildMusicManager manager = ModuleMusic.getMusicManager(e.getGuild().getId());

        e.getTextChannel().sendMessage(e.getAuthor().getAsMention() + " skipped track: " + manager.player.getPlayingTrack().getInfo().title).queue();
        manager.scheduler.nextTrack();
        return true;

    }

}
