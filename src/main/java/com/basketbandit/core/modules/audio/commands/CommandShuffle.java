package com.basketbandit.core.modules.audio.commands;

import com.basketbandit.core.modules.Command;
import com.basketbandit.core.modules.audio.handlers.AudioManagerManager;
import com.basketbandit.core.modules.audio.handlers.GuildAudioManager;
import com.basketbandit.core.utils.MessageHandler;
import com.basketbandit.core.utils.Utils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandShuffle extends Command {

    public CommandShuffle() {
        super("shuffle", "com.basketbandit.core.modules.audio.ModuleAudio", 0, new String[]{"-shuffle"}, null);
    }

    @Override
    public void executeCommand(MessageReceivedEvent e, String[] command) {
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
            Utils.sendException(ex, e.getMessage().getContentRaw());
        }
    }

}
