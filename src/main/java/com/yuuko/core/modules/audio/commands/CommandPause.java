package com.yuuko.core.modules.audio.commands;

import com.yuuko.core.modules.Command;
import com.yuuko.core.modules.audio.handlers.AudioManagerManager;
import com.yuuko.core.modules.audio.handlers.GuildAudioManager;
import com.yuuko.core.utils.MessageHandler;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandPause extends Command {

    public CommandPause() {
        super("pause", "com.yuuko.core.modules.audio.ModuleAudio", 0, new String[]{"-pause"}, null);
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
