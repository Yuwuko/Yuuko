package basketbandit.core.modules.audio.commands;

import basketbandit.core.Utils;
import basketbandit.core.modules.Command;
import basketbandit.core.modules.audio.handlers.AudioManagerHandler;
import basketbandit.core.modules.audio.handlers.GuildAudioManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandPause extends Command {

    public CommandPause() {
        super("pause", "basketbandit.core.modules.audio.ModuleAudio", new String[]{"-pause"}, null);
    }

    public CommandPause(MessageReceivedEvent e, String[] command) {
        executeCommand(e, command);
    }

    @Override
    protected void executeCommand(MessageReceivedEvent e, String[] command) {
        GuildAudioManager manager = AudioManagerHandler.getGuildAudioManager(e.getGuild().getId());

        manager.player.setPaused(true);
        Utils.sendMessage(e, e.getAuthor().getAsMention() + " paused playback.");
    }

}
