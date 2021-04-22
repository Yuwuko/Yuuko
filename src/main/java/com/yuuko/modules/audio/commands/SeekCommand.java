package com.yuuko.modules.audio.commands;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.yuuko.MessageDispatcher;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.modules.Command;
import com.yuuko.modules.audio.handlers.AudioManager;
import com.yuuko.modules.audio.handlers.GuildAudioManager;
import com.yuuko.utilities.Sanitiser;
import com.yuuko.utilities.TextUtilities;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;

public class SeekCommand extends Command {

    public SeekCommand() {
        super("seek", Arrays.asList("-seek <seconds>", "-seek <timestamp>"), 1);
    }

    @Override
    public void onCommand(MessageEvent context) throws Exception {
        GuildAudioManager manager = AudioManager.getGuildAudioManager(context.getGuild());
        AudioTrack track = manager.getPlayer().getPlayingTrack();

        int seek;
        if(Sanitiser.isNumeric(context.getParameters())) {
            seek = Integer.parseInt(context.getParameters())*1000;
        } else {
            String[] timestamp = context.getParameters().split(":", 2);
            if(timestamp.length == 2) {
                boolean nan = false;
                for(String time : timestamp) {
                    if(!Sanitiser.isNumeric(time)) {
                        EmbedBuilder embed = new EmbedBuilder().setTitle(context.i18n( "invalid_param")).setDescription(context.i18n( "invalid_position"));
                        MessageDispatcher.reply(context, embed.build());
                        nan = true;
                    }
                }
                seek = (nan) ? 0 : ((Integer.parseInt(timestamp[0])*60) + Integer.parseInt(timestamp[1])) * 1000;
            } else {
                EmbedBuilder embed = new EmbedBuilder().setTitle(context.i18n( "invalid_param")).setDescription(context.i18n( "invalid_timestamp"));
                MessageDispatcher.reply(context, embed.build());
                seek = 0;
            }
        }

        if(track != null) {
            if(track.isSeekable()) {
                manager.getPlayer().seekTo((seek < track.getInfo().length) ? seek : track.getInfo().length);
                EmbedBuilder embed = new EmbedBuilder().setTitle(context.i18n( "title")).setDescription(context.i18n( "desc").formatted(TextUtilities.getTimestamp(seek)));
                MessageDispatcher.reply(context, embed.build());
            } else {
                EmbedBuilder embed = new EmbedBuilder().setTitle(context.i18n( "unseekable")).setDescription(context.i18n( "unseekable_desc"));
                MessageDispatcher.reply(context, embed.build());
            }
        } else {
            EmbedBuilder embed = new EmbedBuilder().setTitle(context.i18n( "no_track"));
            MessageDispatcher.reply(context, embed.build());
        }
    }

}
