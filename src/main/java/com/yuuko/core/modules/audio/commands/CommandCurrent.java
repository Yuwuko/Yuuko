package com.yuuko.core.modules.audio.commands;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.yuuko.core.Cache;
import com.yuuko.core.Configuration;
import com.yuuko.core.modules.Command;
import com.yuuko.core.modules.audio.handlers.AudioManagerManager;
import com.yuuko.core.modules.audio.handlers.GuildAudioManager;
import com.yuuko.core.utils.MessageHandler;
import com.yuuko.core.utils.Utils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandCurrent extends Command {

    public CommandCurrent() {
        super("current", "com.yuuko.core.modules.audio.ModuleAudio", 0, new String[]{"-current"}, null);
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
                    .addField("Duration", Utils.getTimestamp(track.getPosition()) + "/" + Utils.getTimestamp(track.getDuration()), true)
                    .addField("Channel", track.getInfo().author, true)
                    .setFooter(Cache.STANDARD_STRINGS[2] + e.getMember().getEffectiveName(), e.getGuild().getMemberById(Configuration.BOT_ID).getUser().getAvatarUrl());
            MessageHandler.sendMessage(e, queuedTrack.build());
        } else {
            EmbedBuilder embed = new EmbedBuilder().setTitle("There isn't a track currently playing.");
            MessageHandler.sendMessage(e, embed.build());
        }
    }

}