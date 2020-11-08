package com.yuuko.core.commands.audio.commands;

import com.yuuko.core.Configuration;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.audio.handlers.AudioManagerController;
import com.yuuko.core.commands.audio.handlers.GuildAudioManager;
import com.yuuko.core.events.entity.MessageEvent;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;

public class ShuffleCommand extends Command {

    public ShuffleCommand() {
        super("shuffle", Configuration.MODULES.get("audio"), 0, -1L, Arrays.asList("-shuffle"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        try {
            GuildAudioManager manager = AudioManagerController.getGuildAudioManager(e.getGuild());

            if(manager.getScheduler().queue.size() < 1) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("There aren't any tracks to shuffle.");
                MessageHandler.sendMessage(e, embed.build());
                return;
            }

            EmbedBuilder embed = new EmbedBuilder().setTitle("Shuffling").setDescription("The queue has been shuffled.");
            MessageHandler.sendMessage(e, embed.build());
            manager.getScheduler().shuffle();

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
        }
    }

}
