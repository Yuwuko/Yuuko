package com.basketbandit.core.modules.audio.commands;

import com.basketbandit.core.modules.Command;
import com.basketbandit.core.modules.audio.handlers.AudioManagerHandler;
import com.basketbandit.core.modules.audio.handlers.GuildAudioManager;
import com.basketbandit.core.utils.Utils;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandShuffle extends Command {

    public CommandShuffle() {
        super("shuffle", "com.basketbandit.core.modules.audio.ModuleAudio", new String[]{"-shuffle"}, null);
    }

    public CommandShuffle(MessageReceivedEvent e, String[] command) {
        executeCommand(e, command);
    }

    @Override
    protected void executeCommand(MessageReceivedEvent e, String[] command) {
        GuildAudioManager manager = AudioManagerHandler.getGuildAudioManager(e.getGuild().getId());

        try {
            manager.scheduler.shuffle();
            Utils.sendMessage(e, e.getAuthor().getAsMention() + " shuffled the queue.");
        } catch(Exception ex) {
            Utils.sendException(ex.getMessage());
        }
    }

}
