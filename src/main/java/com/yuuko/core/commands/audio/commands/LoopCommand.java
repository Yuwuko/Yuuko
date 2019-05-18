package com.yuuko.core.commands.audio.commands;

import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.audio.AudioModule;
import com.yuuko.core.commands.audio.handlers.AudioManagerController;
import com.yuuko.core.commands.audio.handlers.GuildAudioManager;
import com.yuuko.core.events.extensions.MessageEvent;
import net.dv8tion.jda.core.EmbedBuilder;

import java.util.Arrays;

public class LoopCommand extends Command {

    public LoopCommand() {
        super("loop", AudioModule.class, 0, Arrays.asList("-loop"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        GuildAudioManager manager = AudioManagerController.getGuildAudioManager(e.getGuild().getId());

        manager.scheduler.setLooping(!manager.scheduler.isLooping());

        EmbedBuilder embed = new EmbedBuilder().setTitle("Loop").setDescription("Looping for queue set to **" + manager.scheduler.isLooping() + "**");
        MessageHandler.sendMessage(e, embed.build());
    }

}
