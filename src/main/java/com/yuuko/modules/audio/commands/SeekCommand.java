package com.yuuko.modules.audio.commands;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.yuuko.MessageDispatcher;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.i18n.I18n;
import com.yuuko.modules.Command;
import com.yuuko.modules.audio.handlers.AudioManager;
import com.yuuko.modules.audio.handlers.GuildAudioManager;
import com.yuuko.utilities.Sanitiser;
import com.yuuko.utilities.TextUtilities;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;

public class SeekCommand extends Command {

    public SeekCommand() {
        super("seek", 1, -1L, Arrays.asList("-seek <seconds>", "-seek <timestamp>"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) throws Exception {
        GuildAudioManager manager = AudioManager.getGuildAudioManager(e.getGuild());
        AudioTrack track = manager.getPlayer().getPlayingTrack();

        int seek;
        if(Sanitiser.isNumeric(e.getParameters())) {
            seek = Integer.parseInt(e.getParameters())*1000;
        } else {
            String[] timestamp = e.getParameters().split(":", 2);
            if(timestamp.length == 2) {
                boolean nan = false;
                for(String time : timestamp) {
                    if(!Sanitiser.isNumeric(time)) {
                        EmbedBuilder embed = new EmbedBuilder().setTitle(I18n.getError(e, "invalid_param")).setDescription(I18n.getError(e, "invalid_position"));
                        MessageDispatcher.reply(e, embed.build());
                        nan = true;
                    }
                }
                seek = (nan) ? 0 : ((Integer.parseInt(timestamp[0])*60) + Integer.parseInt(timestamp[1])) * 1000;
            } else {
                EmbedBuilder embed = new EmbedBuilder().setTitle(I18n.getError(e, "invalid_param")).setDescription(I18n.getError(e, "invalid_timestamp"));
                MessageDispatcher.reply(e, embed.build());
                seek = 0;
            }
        }

        if(track != null) {
            if(track.isSeekable()) {
                manager.getPlayer().seekTo((seek < track.getInfo().length) ? seek : track.getInfo().length);
                EmbedBuilder embed = new EmbedBuilder().setTitle(I18n.getText(e, "title")).setDescription(I18n.getText(e, "desc").formatted(TextUtilities.getTimestamp(seek)));
                MessageDispatcher.reply(e, embed.build());
            } else {
                EmbedBuilder embed = new EmbedBuilder().setTitle(I18n.getError(e, "unseekable")).setDescription(I18n.getError(e, "unseekable_desc"));
                MessageDispatcher.reply(e, embed.build());
            }
        } else {
            EmbedBuilder embed = new EmbedBuilder().setTitle(I18n.getError(e, "no_track"));
            MessageDispatcher.reply(e, embed.build());
        }
    }

}
