package com.yuuko.core.commands.audio.commands;

import com.yuuko.core.MessageDispatcher;
import com.yuuko.core.Yuuko;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.audio.handlers.AudioManager;
import com.yuuko.core.events.entity.MessageEvent;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;

public class PauseCommand extends Command {

    public PauseCommand() {
        super("pause", Yuuko.MODULES.get("audio"), 0, -1L, Arrays.asList("-pause"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) throws Exception {
        EmbedBuilder embed = new EmbedBuilder().setTitle("Pausing").setDescription("The player has been paused.");
        MessageDispatcher.reply(e, embed.build());
        AudioManager.getGuildAudioManager(e.getGuild()).getPlayer().setPaused(true);
    }

}
