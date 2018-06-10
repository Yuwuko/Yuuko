package com.basketbandit.core.modules.audio.commands;

import com.basketbandit.core.Utils;
import com.basketbandit.core.modules.Command;
import com.basketbandit.core.modules.audio.handlers.AudioManagerHandler;
import com.basketbandit.core.modules.audio.handlers.GuildAudioManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandSkip extends Command {

    public CommandSkip() {
        super("skip", "com.basketbandit.core.modules.audio.ModuleAudio", new String[]{"-skip"}, null);
    }

    public CommandSkip(MessageReceivedEvent e, String[] command) {
        executeCommand(e, command);
    }

    @Override
    protected void executeCommand(MessageReceivedEvent e, String[] command) {
        GuildAudioManager manager = AudioManagerHandler.getGuildAudioManager(e.getGuild().getId());

        Utils.sendMessage(e, e.getAuthor().getAsMention() + " skipped track: " + manager.player.getPlayingTrack().getInfo().title);
        manager.scheduler.nextTrack();
    }

}