package com.basketbandit.core.modules.audio.commands;

import com.basketbandit.core.modules.Command;
import com.basketbandit.core.modules.audio.handlers.AudioManagerHandler;
import com.basketbandit.core.modules.audio.handlers.GuildAudioManager;
import com.basketbandit.core.utils.Utils;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandClearQueue extends Command {

    public CommandClearQueue() {
        super("clearqueue", "com.basketbandit.core.modules.audio.ModuleAudio", new String[]{"-clearqueue"}, null);
    }

    public CommandClearQueue(MessageReceivedEvent e, String[] command) {
        executeCommand(e, command);
    }

    @Override
    protected void executeCommand(MessageReceivedEvent e, String[] command) {
        GuildAudioManager manager = AudioManagerHandler.getGuildAudioManager(e.getGuild().getId());

        manager.scheduler.queue.clear();
        Utils.sendMessage(e, e.getAuthor().getAsMention() + " cleared the queue.");
    }

}
