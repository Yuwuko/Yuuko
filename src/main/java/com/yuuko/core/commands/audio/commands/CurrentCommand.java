package com.yuuko.core.commands.audio.commands;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.yuuko.core.Configuration;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.audio.handlers.AudioManagerController;
import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.utilities.LavalinkUtilities;
import com.yuuko.core.utilities.TextUtilities;
import net.dv8tion.jda.core.EmbedBuilder;

import java.util.Arrays;

public class CurrentCommand extends Command {

    public CurrentCommand() {
        super("current", Configuration.MODULES.get("audio"), 0, Arrays.asList("-current"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        AudioTrack track = AudioManagerController.getGuildAudioManager(e.getGuild()).getPlayer().getPlayingTrack();

        if(track != null) {
            String[] uri = track.getInfo().uri.split("=");
            String imageUrl = (uri.length > 1) ? "https://img.youtube.com/vi/" + uri[1] + "/1.jpg" : "https://i.imgur.com/bCNQlm6.jpg";

            EmbedBuilder queuedTrack = new EmbedBuilder()
                    .setAuthor("Now Playing")
                    .setTitle(track.getInfo().title, track.getInfo().uri)
                    .setThumbnail(imageUrl)
                    .addField("Duration", TextUtilities.getTimestamp(LavalinkUtilities.getTrackPosition(e.getGuild())) + "/" + TextUtilities.getTimestamp(track.getDuration()), true)
                    .addField("Channel", track.getInfo().author, true)
                    .setFooter(Configuration.STANDARD_STRINGS.get(1) + e.getMember().getEffectiveName(), e.getAuthor().getEffectiveAvatarUrl());
            MessageHandler.sendMessage(e, queuedTrack.build());
        } else {
            EmbedBuilder embed = new EmbedBuilder().setTitle("There isn't a track currently playing.");
            MessageHandler.sendMessage(e, embed.build());
        }
    }

}
