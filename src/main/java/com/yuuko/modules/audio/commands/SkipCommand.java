package com.yuuko.modules.audio.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.modules.Command;
import com.yuuko.modules.audio.handlers.AudioManager;
import com.yuuko.modules.audio.handlers.GuildAudioManager;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;

public class SkipCommand extends Command {

    public SkipCommand() {
        super("skip", Arrays.asList("-skip"));
    }

    @Override
    public void onCommand(MessageEvent context) throws Exception {
        GuildAudioManager manager = AudioManager.getGuildAudioManager(context.getGuild());
        if(manager.getPlayer().getPlayingTrack() == null) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(context.i18n( "no_track"));
            MessageDispatcher.reply(context, embed.build());
            return;
        }

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle(context.i18n( "title"))
                .setDescription(manager.getPlayer().getPlayingTrack().getInfo().title);
        MessageDispatcher.reply(context, embed.build());
        manager.getScheduler().nextTrack();
    }

}
