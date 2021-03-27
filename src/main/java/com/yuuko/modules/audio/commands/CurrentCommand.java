package com.yuuko.modules.audio.commands;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.yuuko.MessageDispatcher;
import com.yuuko.Yuuko;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.modules.Command;
import com.yuuko.modules.audio.handlers.AudioManager;
import com.yuuko.modules.audio.handlers.GuildAudioManager;
import com.yuuko.utilities.TextUtilities;
import com.yuuko.utilities.Utilities;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;

public class CurrentCommand extends Command {

    public CurrentCommand() {
        super("current", 0, -1L, Arrays.asList("-current"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) throws Exception {
        GuildAudioManager manager = AudioManager.getGuildAudioManager(e.getGuild());
        AudioTrack track = manager.getPlayer().getPlayingTrack();

        if(track == null) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("There is no track currently playing.");
            MessageDispatcher.reply(e, embed.build());
            return;
        }

        EmbedBuilder queuedTrack = new EmbedBuilder()
                .setAuthor("Now Playing")
                .setTitle(track.getInfo().title, track.getInfo().uri)
                .setThumbnail(Utilities.getAudioTrackImage(track))
                .addField("Duration", TextUtilities.getTimestamp(manager.getPlayer().getTrackPosition()) + "/" + TextUtilities.getTimestamp(track.getDuration()), true)
                .addField("Channel", track.getInfo().author, true)
                .setFooter(Yuuko.STANDARD_STRINGS.get(1) + e.getAuthor().getAsTag(), e.getAuthor().getEffectiveAvatarUrl());

        if(e.hasParameters() && e.getParameters().equals("no-reply")) {
            MessageDispatcher.sendMessage(e, queuedTrack.build());
            return;
        }
        MessageDispatcher.reply(e, queuedTrack.build());
    }

}