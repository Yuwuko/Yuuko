package basketbandit.core.modules.audio.commands;

import basketbandit.core.Utils;
import basketbandit.core.modules.Command;
import basketbandit.core.modules.audio.handlers.AudioManagerHandler;
import basketbandit.core.modules.audio.handlers.GuildAudioManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandToggleRepeat extends Command {

    public CommandToggleRepeat() {
        super("togglerepeat", "basketbandit.core.modules.audio.ModuleAudio", new String[]{"-togglerepeat"}, null);
    }

    public CommandToggleRepeat(MessageReceivedEvent e, String[] command) {
        executeCommand(e, command);
    }

    @Override
    protected void executeCommand(MessageReceivedEvent e, String[] command) {
        GuildAudioManager manager = AudioManagerHandler.getGuildAudioManager(e.getGuild().getId());

        manager.scheduler.setRepeating(!manager.scheduler.isRepeating());
        Utils.sendMessage(e, e.getAuthor().getAsMention() + " toggled repeat to: " + manager.scheduler.isRepeating());
    }

}
