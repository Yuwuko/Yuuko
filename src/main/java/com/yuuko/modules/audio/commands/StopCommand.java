package com.yuuko.modules.audio.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.modules.Command;
import com.yuuko.modules.audio.handlers.AudioManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;

import java.util.Arrays;

public class StopCommand extends Command {

    public StopCommand() {
        super("stop", 0, -1L, Arrays.asList("-stop"), false, null);
    }

    @Override
    public void onCommand(MessageEvent context) throws Exception {
        AudioManager.destroyGuildAudioManager(context.getGuild());
        if(context.getCommand() != null) {
            EmbedBuilder embed = new EmbedBuilder().setTitle(context.i18n( "title")).setDescription(context.i18n( "desc"));
            MessageDispatcher.reply(context, embed.build());
        }
    }

    /**
     * Executes command just by feeding the method a guild object.
     * @param guild {@link Guild}
     */
    public void onCommand(Guild guild) {
        AudioManager.destroyGuildAudioManager(guild);
    }

}
