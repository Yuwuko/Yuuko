package com.basketbandit.core.modules.audio.commands;

import com.basketbandit.core.modules.Command;
import com.basketbandit.core.modules.audio.handlers.AudioManagerHandler;
import com.basketbandit.core.modules.audio.handlers.GuildAudioManager;
import com.basketbandit.core.utils.Utils;
import net.dv8tion.jda.core.events.guild.GenericGuildEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandStop extends Command {

    public CommandStop() {
        super("stop", "com.basketbandit.core.modules.audio.ModuleAudio", 0, new String[]{"-stop"}, null);
    }

    @Override
    public void executeCommand(MessageReceivedEvent e, String[] command) {
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
    public void executeCommandAux(GenericGuildEvent e) {
        GuildAudioManager manager = AudioManagerHandler.getGuildAudioManager(e.getGuild().getId());

        manager.scheduler.queue.clear();
        manager.scheduler.setBackground(null);
        manager.player.stopTrack();
        manager.player.setPaused(false);
        e.getGuild().getAudioManager().setSendingHandler(null);
        e.getGuild().getAudioManager().closeAudioConnection();
    }
}
