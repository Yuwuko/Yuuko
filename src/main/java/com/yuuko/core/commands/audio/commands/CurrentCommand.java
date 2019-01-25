package com.yuuko.core.commands.audio.commands;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.yuuko.core.Cache;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.audio.AudioModule;
import com.yuuko.core.commands.audio.handlers.AudioManagerManager;
import com.yuuko.core.commands.audio.handlers.GuildAudioManager;
import com.yuuko.core.utilities.MessageHandler;
import com.yuuko.core.utilities.TextUtility;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CurrentCommand extends Command {

    public CurrentCommand() {
        super("current", AudioModule.class, 0, new String[]{"-current"}, false, null);
    }

    @Override
    public void executeCommand(MessageReceivedEvent e, String[] command) {
        GuildAudioManager manager = AudioManagerManager.getGuildAudioManager(e.getGuild().getId());
        AudioTrack track = manager.player.getPlayingTrack();

        if(track != null) {
            String[] uri = track.getInfo().uri.split("=");
            String imageUrl = (uri.length > 1) ? "https://img.youtube.com/vi/" + uri[1] + "/1.jpg" : "https://i.imgur.com/bCNQlm6.jpg";

            EmbedBuilder queuedTrack = new EmbedBuilder()
                    .setAuthor("Now Playing")
                    .setTitle(track.getInfo().title, track.getInfo().uri)
                    .setThumbnail(imageUrl)
                    .addField("Duration", TextUtility.getTimestamp(track.getPosition()) + "/" + TextUtility.getTimestamp(track.getDuration()), true)
                    .addField("Channel", track.getInfo().author, true)
                    .setFooter(Cache.STANDARD_STRINGS[1] + e.getMember().getEffectiveName(), Cache.BOT.getAvatarUrl());
            MessageHandler.sendMessage(e, queuedTrack.build());
        } else {
            EmbedBuilder embed = new EmbedBuilder().setTitle("There isn't a track currently playing.");
            MessageHandler.sendMessage(e, embed.build());
        }
    }

}
