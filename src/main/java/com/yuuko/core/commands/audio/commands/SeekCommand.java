package com.yuuko.core.commands.audio.commands;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.audio.AudioModule;
import com.yuuko.core.commands.audio.handlers.AudioManagerManager;
import com.yuuko.core.events.extensions.MessageEvent;
import com.yuuko.core.utilities.LavalinkUtilities;
import com.yuuko.core.utilities.Sanitiser;
import com.yuuko.core.utilities.TextUtilities;
import net.dv8tion.jda.core.EmbedBuilder;

public class SeekCommand extends Command {

    public SeekCommand() {
        super("seek", AudioModule.class, 1, new String[]{"-seek <seconds>", "-seek <timestamp>"}, false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        AudioTrack track = AudioManagerManager.getGuildAudioManager(e.getGuild().getId()).player.getPlayingTrack();

        int seek;

        if(Sanitiser.isNumber(e.getCommand()[1])) {
            seek = Integer.parseInt(e.getCommand()[1])*1000;
        } else {

            String[] timestamp = e.getCommand()[1].split(":", 2);
            if(timestamp.length == 2) {
                boolean nan = false;
                for(String time : timestamp) {
                    if(!Sanitiser.isNumber(time)) {
                        EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Parameter").setDescription("The position you selected was invalid, time value was set to `0`.");
                        MessageHandler.sendMessage(e, embed.build());
                        nan = true;
                    }
                }
                seek = (nan) ? 0 : ((Integer.parseInt(timestamp[0])*60) + Integer.parseInt(timestamp[1])) * 1000;
            } else {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Parameter").setDescription("The timestamp you selected was invalid, position value was set to `0`. Correct timestamps are formatted `2:10` for example.");
                MessageHandler.sendMessage(e, embed.build());
                seek = 0;
            }

        }

        if(track != null) {
            if(track.isSeekable()) {
                LavalinkUtilities.getPlayer(e.getGuild()).seekTo((seek < track.getInfo().length) ? seek : track.getInfo().length);
                EmbedBuilder embed = new EmbedBuilder().setTitle("Seeking").setDescription("The track position has been set to `" + TextUtilities.getTimestamp(seek) + "`.");
                MessageHandler.sendMessage(e, embed.build());
            } else {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Unseekable").setDescription("Sorry, but this track is currently unseekable.");
                MessageHandler.sendMessage(e, embed.build());
            }
        } else {
            EmbedBuilder embed = new EmbedBuilder().setTitle("There isn't a track currently playing.");
            MessageHandler.sendMessage(e, embed.build());
        }
    }

}
