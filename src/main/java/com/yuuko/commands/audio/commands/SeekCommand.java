package com.yuuko.commands.audio.commands;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.yuuko.MessageDispatcher;
import com.yuuko.Yuuko;
import com.yuuko.commands.Command;
import com.yuuko.commands.audio.handlers.AudioManager;
import com.yuuko.commands.audio.handlers.GuildAudioManager;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.utilities.Sanitiser;
import com.yuuko.utilities.TextUtilities;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;

public class SeekCommand extends Command {

    public SeekCommand() {
        super("seek", Yuuko.MODULES.get("audio"), 1, -1L, Arrays.asList("-seek <seconds>", "-seek <timestamp>"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) throws Exception {
        GuildAudioManager manager = AudioManager.getGuildAudioManager(e.getGuild());
        AudioTrack track = manager.getPlayer().getPlayingTrack();

        int seek;
        if(Sanitiser.isNumber(e.getParameters())) {
            seek = Integer.parseInt(e.getParameters())*1000;
        } else {
            String[] timestamp = e.getParameters().split(":", 2);
            if(timestamp.length == 2) {
                boolean nan = false;
                for(String time : timestamp) {
                    if(!Sanitiser.isNumber(time)) {
                        EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Parameter").setDescription("The position you selected was invalid, time value was set to `0`.");
                        MessageDispatcher.reply(e, embed.build());
                        nan = true;
                    }
                }
                seek = (nan) ? 0 : ((Integer.parseInt(timestamp[0])*60) + Integer.parseInt(timestamp[1])) * 1000;
            } else {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Parameter").setDescription("The timestamp you selected was invalid, position value was set to `0`. Correct timestamps are formatted `2:10` for example.");
                MessageDispatcher.reply(e, embed.build());
                seek = 0;
            }
        }

        if(track != null) {
            if(track.isSeekable()) {
                manager.getPlayer().seekTo((seek < track.getInfo().length) ? seek : track.getInfo().length);
                EmbedBuilder embed = new EmbedBuilder().setTitle("Seeking").setDescription("The track position has been set to `" + TextUtilities.getTimestamp(seek) + "`.");
                MessageDispatcher.reply(e, embed.build());
            } else {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Unseekable").setDescription("Sorry, but this track is currently unseekable.");
                MessageDispatcher.reply(e, embed.build());
            }
        } else {
            EmbedBuilder embed = new EmbedBuilder().setTitle("There isn't a track currently playing.");
            MessageDispatcher.reply(e, embed.build());
        }
    }

}
