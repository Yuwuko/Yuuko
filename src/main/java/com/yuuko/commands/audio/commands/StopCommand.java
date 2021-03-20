package com.yuuko.commands.audio.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.commands.Command;
import com.yuuko.commands.audio.handlers.AudioManager;
import com.yuuko.events.entity.MessageEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;

import java.util.Arrays;

public class StopCommand extends Command {

    public StopCommand() {
        super("stop", 0, -1L, Arrays.asList("-stop"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) throws Exception {
        AudioManager.destroyGuildAudioManager(e.getGuild());
        if(e.getCommand() != null) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Stopping").setDescription("Audio connection closed.");
            MessageDispatcher.reply(e, embed.build());
        }
    }

    /**
     * Executes command just by feeding the method a guild object.
     *
     * @param guild {@link Guild}
     */
    public void onCommand(Guild guild) {
        AudioManager.destroyGuildAudioManager(guild);
    }

}
