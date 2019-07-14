package com.yuuko.core.commands.audio.commands;

import com.yuuko.core.Configuration;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.audio.handlers.AudioManagerController;
import com.yuuko.core.commands.audio.handlers.GuildAudioManager;
import com.yuuko.core.events.entity.MessageEvent;
import net.dv8tion.jda.core.EmbedBuilder;

import java.util.Arrays;

public class LoopCommand extends Command {

    public LoopCommand() {
        super("loop", Configuration.MODULES.get("audio"), 0, Arrays.asList("-loop"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        GuildAudioManager manager = AudioManagerController.getGuildAudioManager(e.getGuild());

        manager.getScheduler().setLooping(!manager.getScheduler().isLooping());

        EmbedBuilder embed = new EmbedBuilder().setTitle("Loop").setDescription("Looping for queue set to `" + manager.getScheduler().isLooping() + "`");
        MessageHandler.sendMessage(e, embed.build());
    }

}
