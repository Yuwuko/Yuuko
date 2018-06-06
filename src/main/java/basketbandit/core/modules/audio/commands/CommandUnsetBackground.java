package basketbandit.core.modules.audio.commands;

import basketbandit.core.modules.Command;
import basketbandit.core.modules.audio.handlers.AudioManagerHandler;
import basketbandit.core.modules.audio.handlers.GuildAudioManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandUnsetBackground extends Command {

    public CommandUnsetBackground() {
        super("unsetbackground", "basketbandit.core.modules.audio.ModuleAudio", null);
    }

    public CommandUnsetBackground(MessageReceivedEvent e, String[] command) {
        super("unsetbackground", "basketbandit.core.modules.audio.ModuleAudio", null);
        executeCommand(e, command);
    }

    /**
     * Executes command using MessageReceivedEvent e.
     * @param e; MessageReceivedEvent.
     */
    protected void executeCommand(MessageReceivedEvent e, String[] command) {
        GuildAudioManager manager = AudioManagerHandler.getGuildAudioManager(e.getGuild().getId());

        manager.scheduler.setBackground(null);
        e.getTextChannel().sendMessage("Background track removed.").queue();
    }

}
