package com.yuuko.core.commands.audio.commands;

import com.yuuko.core.MessageDispatcher;
import com.yuuko.core.Yuuko;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.audio.handlers.AudioManager;
import com.yuuko.core.commands.audio.handlers.GuildAudioManager;
import com.yuuko.core.events.entity.MessageEvent;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;

public class LoopCommand extends Command {

    public LoopCommand() {
        super("loop", Yuuko.MODULES.get("audio"), 0, -1L, Arrays.asList("-loop"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) throws Exception {
        GuildAudioManager manager = AudioManager.getGuildAudioManager(e.getGuild());
        EmbedBuilder embed = new EmbedBuilder().setTitle("Loop").setDescription("Looping for queue set to `" + !manager.getScheduler().isLooping() + "`");
        MessageDispatcher.reply(e, embed.build());
        manager.getScheduler().setLooping(!manager.getScheduler().isLooping());
    }

}
