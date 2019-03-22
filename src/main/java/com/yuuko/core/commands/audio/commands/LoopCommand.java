package com.yuuko.core.commands.audio.commands;

import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.audio.AudioModule;
import com.yuuko.core.commands.audio.handlers.AudioManagerManager;
import com.yuuko.core.commands.audio.handlers.GuildAudioManager;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class LoopCommand extends Command {

    public LoopCommand() {
        super("loop", AudioModule.class, 0, new String[]{"-loop"}, false, null);
    }

    @Override
    public void onCommand(MessageReceivedEvent e, String[] command) {
        GuildAudioManager manager = AudioManagerManager.getGuildAudioManager(e.getGuild().getId());

        manager.scheduler.setLooping(!manager.scheduler.isLooping());

        EmbedBuilder embed = new EmbedBuilder().setTitle("Loop").setDescription("Looping for queue set to **" + manager.scheduler.isLooping() + "**");
        MessageHandler.sendMessage(e, embed.build());
    }

}
