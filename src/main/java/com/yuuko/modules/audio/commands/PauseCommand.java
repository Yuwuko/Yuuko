package com.yuuko.modules.audio.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.modules.Command;
import com.yuuko.modules.audio.handlers.AudioManager;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;

public class PauseCommand extends Command {

    public PauseCommand() {
        super("pause", Arrays.asList("-pause"));
    }

    @Override
    public void onCommand(MessageEvent context) {
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle(context.i18n( "title"))
                .setDescription(context.i18n( "desc"));
        MessageDispatcher.reply(context, embed.build());
        AudioManager.getGuildAudioManager(context.getGuild()).getPlayer().setPaused(true);
    }

}
