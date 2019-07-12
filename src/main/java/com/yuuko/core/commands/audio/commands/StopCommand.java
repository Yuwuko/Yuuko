package com.yuuko.core.commands.audio.commands;

import com.yuuko.core.Configuration;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.audio.AudioModule;
import com.yuuko.core.commands.audio.handlers.AudioManagerController;
import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.utilities.LavalinkUtilities;
import lavalink.client.io.Link;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;

import java.util.Arrays;

public class StopCommand extends Command {

    public StopCommand() {
        super("stop", AudioModule.class, 0, Arrays.asList("-stop"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        if(!LavalinkUtilities.isState(e.getGuild(), Link.State.NOT_CONNECTED)) {
            Configuration.LAVALINK.resetPlayer(e.getGuild());
            Configuration.LAVALINK.closeConnection(e.getGuild());
            AudioManagerController.removeGuildAudioManager(e.getGuild());

            if(e.getCommand() != null) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Stopping").setDescription("Audio connection closed.");
                MessageHandler.sendMessage(e, embed.build());
            }
        }
    }

    /**
     * Executes command just by feeding the method a guild object.
     *
     * @param guild Guild
     */
    public void onCommand(Guild guild) {
        Configuration.LAVALINK.resetPlayer(guild);
        Configuration.LAVALINK.closeConnection(guild);
        AudioManagerController.removeGuildAudioManager(guild);
    }
}
