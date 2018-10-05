package com.basketbandit.core.modules.audio.commands;

import com.basketbandit.core.modules.Command;
import com.basketbandit.core.modules.audio.handlers.AudioManagerHandler;
import com.basketbandit.core.modules.audio.handlers.GuildAudioManager;
import com.basketbandit.core.utils.MessageHandler;
import com.basketbandit.core.utils.Utils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandPause extends Command {

    public CommandPause() {
        super("pause", "com.basketbandit.core.modules.audio.ModuleAudio", 0, new String[]{"-pause"}, null);
    }

    @Override
    public void executeCommand(MessageReceivedEvent e, String[] command) {
        GuildAudioManager manager = AudioManagerHandler.getGuildAudioManager(e.getGuild().getId());

        try {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Pausing").setDescription("**The player has been paused.**");
            MessageHandler.sendMessage(e, embed.build());
            manager.player.setPaused(true);
        } catch(Exception ex) {
            Utils.sendException(ex, e.getMessage().getContentRaw());
        }

    }

}
