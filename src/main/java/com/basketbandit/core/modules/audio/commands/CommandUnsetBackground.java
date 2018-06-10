package com.basketbandit.core.modules.audio.commands;

import com.basketbandit.core.Utils;
import com.basketbandit.core.modules.Command;
import com.basketbandit.core.modules.audio.handlers.AudioManagerHandler;
import com.basketbandit.core.modules.audio.handlers.GuildAudioManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandUnsetBackground extends Command {

    public CommandUnsetBackground() {
        super("unsetbackground", "com.basketbandit.core.modules.audio.ModuleAudio", new String[]{"-unsetbackground"}, null);
    }

    public CommandUnsetBackground(MessageReceivedEvent e, String[] command) {
        executeCommand(e, command);
    }

    @Override
    protected void executeCommand(MessageReceivedEvent e, String[] command) {
        GuildAudioManager manager = AudioManagerHandler.getGuildAudioManager(e.getGuild().getId());

        manager.scheduler.setBackground(null);
        Utils.sendMessage(e, "Background track removed.");
    }

}
