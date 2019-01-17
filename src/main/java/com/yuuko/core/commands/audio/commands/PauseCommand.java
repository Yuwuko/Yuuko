package com.yuuko.core.commands.audio.commands;

import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.audio.AudioModule;
import com.yuuko.core.commands.audio.handlers.AudioManagerManager;
import com.yuuko.core.commands.audio.handlers.GuildAudioManager;
import com.yuuko.core.utilities.MessageHandler;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class PauseCommand extends Command {

    public PauseCommand() {
        super("pause", AudioModule.class, 0, new String[]{"-pause"}, null);
    }

    @Override
    public void executeCommand(MessageReceivedEvent e, String[] command) {
        GuildAudioManager manager = AudioManagerManager.getGuildAudioManager(e.getGuild().getId());

        try {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Pausing").setDescription("The player has been paused.");
            MessageHandler.sendMessage(e, embed.build());
            manager.player.setPaused(true);
        } catch(Exception ex) {
            MessageHandler.sendException(ex, e.getMessage().getContentRaw());
        }

    }

}
