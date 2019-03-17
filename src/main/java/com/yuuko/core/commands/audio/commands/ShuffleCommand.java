package com.yuuko.core.commands.audio.commands;

import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.audio.AudioModule;
import com.yuuko.core.commands.audio.handlers.AudioManagerManager;
import com.yuuko.core.commands.audio.handlers.GuildAudioManager;
import com.yuuko.core.utilities.MessageHandler;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ShuffleCommand extends Command {

    public ShuffleCommand() {
        super("shuffle", AudioModule.class, 0, new String[]{"-shuffle"}, false, null);
    }

    @Override
    public void onCommand(MessageReceivedEvent e, String[] command) {
        try {
            GuildAudioManager manager = AudioManagerManager.getGuildAudioManager(e.getGuild().getId());

            if(manager.scheduler.queue.size() > 1) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Shuffling").setDescription("The queue has been shuffled.");
                MessageHandler.sendMessage(e, embed.build());
                manager.scheduler.shuffle();
            } else {
                EmbedBuilder embed = new EmbedBuilder().setTitle("There aren't any tracks to shuffle.");
                MessageHandler.sendMessage(e, embed.build());
            }
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
        }
    }

}
