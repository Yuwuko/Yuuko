package basketbandit.core.modules.audio.commands;

import basketbandit.core.modules.Command;
import basketbandit.core.modules.audio.handlers.AudioManagerHandler;
import basketbandit.core.modules.audio.handlers.GuildAudioManager;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandStop extends Command {

    public CommandStop() {
        super("stop", "basketbandit.core.modules.audio.ModuleAudio", null);
    }

    public CommandStop(MessageReceivedEvent e) {
        super("stop", "basketbandit.core.modules.audio.ModuleAudio", null);
        executeCommand(e);
    }

    public CommandStop(GuildVoiceLeaveEvent e) {
        super("stop", "basketbandit.core.modules.audio.ModuleAudio", null);
        executeCommandAux(e);
    }

    /**
     * Executes command using MessageReceivedEvent e.
     * @param e; MessageReceivedEvent.
     */
    protected void executeCommand(MessageReceivedEvent e) {
        GuildAudioManager manager = AudioManagerHandler.getGuildAudioManager(e.getGuild().getId());

        manager.scheduler.queue.clear();
        manager.player.stopTrack();
        manager.player.setPaused(false);
        e.getGuild().getAudioManager().setSendingHandler(null);
        e.getGuild().getAudioManager().closeAudioConnection();
        e.getTextChannel().sendMessage(e.getAuthor().getAsMention() + " stopped playback.").queue();
    }

    /**
     * Executes command when everyone leaves the channel the bot is in.
     * @param e; GuildVoiceLeaveEvent
     */
    private void executeCommandAux(GuildVoiceLeaveEvent e) {
        GuildAudioManager manager = AudioManagerHandler.getGuildAudioManager(e.getGuild().getId());

        manager.scheduler.queue.clear();
        manager.player.stopTrack();
        manager.player.setPaused(false);
        e.getGuild().getAudioManager().setSendingHandler(null);
        e.getGuild().getAudioManager().closeAudioConnection();
    }

}
