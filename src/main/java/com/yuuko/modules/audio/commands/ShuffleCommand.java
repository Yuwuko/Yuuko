package com.yuuko.modules.audio.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.modules.Command;
import com.yuuko.modules.audio.handlers.AudioManager;
import com.yuuko.modules.audio.handlers.GuildAudioManager;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;

public class ShuffleCommand extends Command {

    public ShuffleCommand() {
        super("shuffle", Arrays.asList("-shuffle"));
    }

    @Override
    public void onCommand(MessageEvent context) throws Exception {
        GuildAudioManager manager = AudioManager.getGuildAudioManager(context.getGuild());
        if(manager.getScheduler().queue.size() < 1) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(context.i18n( "no_track"));
            MessageDispatcher.reply(context, embed.build());
            return;
        }

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle(context.i18n( "title"))
                .setDescription(context.i18n( "desc"));
        MessageDispatcher.reply(context, embed.build());
        manager.getScheduler().shuffle();
    }

}
