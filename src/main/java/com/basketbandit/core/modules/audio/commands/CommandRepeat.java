package com.basketbandit.core.modules.audio.commands;

import com.basketbandit.core.modules.Command;
import com.basketbandit.core.modules.audio.handlers.AudioManagerManager;
import com.basketbandit.core.modules.audio.handlers.GuildAudioManager;
import com.basketbandit.core.utils.MessageHandler;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandRepeat extends Command {

    public CommandRepeat() {
        super("repeat", "com.basketbandit.core.modules.audio.ModuleAudio", 0, new String[]{"-repeat"}, null);
    }

    @Override
    public void executeCommand(MessageReceivedEvent e, String[] command) {
        GuildAudioManager manager = AudioManagerManager.getGuildAudioManager(e.getGuild().getId());

        manager.scheduler.setRepeating(!manager.scheduler.isRepeating());
        MessageHandler.sendMessage(e, e.getAuthor().getAsMention() + " toggled repeat to: " + manager.scheduler.isRepeating());
    }

}
