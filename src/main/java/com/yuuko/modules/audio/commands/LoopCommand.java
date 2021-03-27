package com.yuuko.modules.audio.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.modules.Command;
import com.yuuko.modules.audio.handlers.AudioManager;
import com.yuuko.modules.audio.handlers.GuildAudioManager;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;

public class LoopCommand extends Command {

    public LoopCommand() {
        super("loop", 0, -1L, Arrays.asList("-loop"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) throws Exception {
        GuildAudioManager manager = AudioManager.getGuildAudioManager(e.getGuild());
        EmbedBuilder embed = new EmbedBuilder().setTitle("Loop").setDescription("Looping for queue set to `" + !manager.getScheduler().isLooping() + "`");
        MessageDispatcher.reply(e, embed.build());
        manager.getScheduler().setLooping(!manager.getScheduler().isLooping());
    }

}