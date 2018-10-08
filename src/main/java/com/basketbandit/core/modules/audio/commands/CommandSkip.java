package com.basketbandit.core.modules.audio.commands;

import com.basketbandit.core.modules.Command;
import com.basketbandit.core.modules.audio.handlers.AudioManagerManager;
import com.basketbandit.core.modules.audio.handlers.GuildAudioManager;
import com.basketbandit.core.utils.MessageHandler;
import com.basketbandit.core.utils.Utils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandSkip extends Command {

    public CommandSkip() {
        super("skip", "com.basketbandit.core.modules.audio.ModuleAudio", 0, new String[]{"-skip"}, null);
    }

    @Override
    public void executeCommand(MessageReceivedEvent e, String[] command) {
        try {
            GuildAudioManager manager = AudioManagerManager.getGuildAudioManager(e.getGuild().getId());

            if(manager.player.getPlayingTrack() != null) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Skipping").setDescription(manager.player.getPlayingTrack().getInfo().title);
                MessageHandler.sendMessage(e, embed.build());

                if(manager.scheduler.hasNextTrack()) {
                    manager.scheduler.nextTrack();
                } else {
                    manager.player.stopTrack();
                }
            } else {
                EmbedBuilder embed = new EmbedBuilder().setTitle("There is no current track to skip.");
                MessageHandler.sendMessage(e, embed.build());
            }

        } catch(Exception ex) {
            Utils.sendException(ex, e.getMessage().getContentRaw());
        }
    }

}
