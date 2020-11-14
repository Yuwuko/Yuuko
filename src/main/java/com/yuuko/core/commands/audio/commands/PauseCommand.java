package com.yuuko.core.commands.audio.commands;

import com.yuuko.core.Config;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.audio.handlers.AudioManagerController;
import com.yuuko.core.events.entity.MessageEvent;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;

public class PauseCommand extends Command {

    public PauseCommand() {
        super("pause", Config.MODULES.get("audio"), 0, -1L, Arrays.asList("-pause"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        try {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Pausing").setDescription("The player has been paused.");
            MessageHandler.sendMessage(e, embed.build());
            AudioManagerController.getGuildAudioManager(e.getGuild()).getPlayer().setPaused(true);
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
        }
    }

}
