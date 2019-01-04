package com.yuuko.core.modules.audio.commands;

import com.yuuko.core.modules.Command;
import com.yuuko.core.modules.audio.AudioModule;
import com.yuuko.core.modules.audio.handlers.AudioManagerManager;
import com.yuuko.core.modules.audio.handlers.GuildAudioManager;
import com.yuuko.core.utilities.MessageHandler;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class RepeatCommand extends Command {

    public RepeatCommand() {
        super("repeat", AudioModule.class, 0, new String[]{"-repeat"}, null);
    }

    @Override
    public void executeCommand(MessageReceivedEvent e, String[] command) {
        GuildAudioManager manager = AudioManagerManager.getGuildAudioManager(e.getGuild().getId());

        manager.scheduler.setRepeating(!manager.scheduler.isRepeating());
        MessageHandler.sendMessage(e, e.getAuthor().getAsMention() + " toggled repeat to: " + manager.scheduler.isRepeating());
    }

}
