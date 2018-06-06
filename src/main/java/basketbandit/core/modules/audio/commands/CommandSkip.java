package basketbandit.core.modules.audio.commands;

import basketbandit.core.modules.Command;
import basketbandit.core.modules.audio.handlers.AudioManagerHandler;
import basketbandit.core.modules.audio.handlers.GuildAudioManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandSkip extends Command {

    public CommandSkip() {
        super("skip", "basketbandit.core.modules.audio.ModuleAudio", null);
    }

    public CommandSkip(MessageReceivedEvent e, String[] command) {
        super("skip", "basketbandit.core.modules.audio.ModuleAudio", null);
        executeCommand(e, command);
    }

    /**
     * Executes command using MessageReceivedEvent e.
     * @param e; MessageReceivedEvent.
     * @return boolean; if the command executed correctly.
     */
    protected void executeCommand(MessageReceivedEvent e, String[] command) {
        GuildAudioManager manager = AudioManagerHandler.getGuildAudioManager(e.getGuild().getId());

        e.getTextChannel().sendMessage(e.getAuthor().getAsMention() + " skipped track: " + manager.player.getPlayingTrack().getInfo().title).queue();
        manager.scheduler.nextTrack();
    }

}
