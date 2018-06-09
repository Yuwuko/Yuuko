package basketbandit.core.modules.audio.commands;

import basketbandit.core.Utils;
import basketbandit.core.modules.Command;
import basketbandit.core.modules.audio.handlers.AudioManagerHandler;
import basketbandit.core.modules.audio.handlers.GuildAudioManager;
import net.dv8tion.jda.core.events.guild.GenericGuildEvent;
import net.dv8tion.jda.core.events.guild.voice.GenericGuildVoiceEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandStop extends Command {

    public CommandStop() {
        super("stop", "basketbandit.core.modules.audio.ModuleAudio", new String[]{"-stop"}, null);
    }

    public CommandStop(MessageReceivedEvent e, String[] command) {
        executeCommand(e, command);
    }

    public CommandStop(GenericGuildVoiceEvent e) {
        executeCommandEdge(e);
    }

    @Override
    protected void executeCommand(MessageReceivedEvent e, String[] command) {
        GuildAudioManager manager = AudioManagerHandler.getGuildAudioManager(e.getGuild().getId());

        manager.scheduler.queue.clear();
        manager.scheduler.setBackground(null);
        manager.player.stopTrack();
        manager.player.setPaused(false);
        e.getGuild().getAudioManager().setSendingHandler(null);
        e.getGuild().getAudioManager().closeAudioConnection();
        Utils.sendMessage(e, e.getAuthor().getAsMention() + " stopped playback.");
    }

    /**
     * Executes command when everyone leaves the channel the bot is in.
     * @param e; GenericGuildEvent
     */
    private void executeCommandEdge(GenericGuildEvent e) {
        GuildAudioManager manager = AudioManagerHandler.getGuildAudioManager(e.getGuild().getId());

        manager.scheduler.queue.clear();
        manager.scheduler.setBackground(null);
        manager.player.stopTrack();
        manager.player.setPaused(false);
        e.getGuild().getAudioManager().setSendingHandler(null);
        e.getGuild().getAudioManager().closeAudioConnection();
    }
}
