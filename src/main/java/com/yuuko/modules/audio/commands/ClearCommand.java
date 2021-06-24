package com.yuuko.modules.audio.commands;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.yuuko.MessageDispatcher;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.modules.Command;
import com.yuuko.modules.audio.handlers.AudioManager;
import com.yuuko.modules.audio.handlers.GuildAudioManager;
import com.yuuko.utilities.Sanitiser;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;
import java.util.LinkedList;

public class ClearCommand extends Command {

    public ClearCommand() {
        super("clear", Arrays.asList("-clear", "-clear <position>"));
    }

    @Override
    public void onCommand(MessageEvent context) throws Exception {
        // Clear entire queue
        if(!context.hasParameters()) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(context.i18n( "title"))
                    .setDescription(context.i18n( "desc"));
            MessageDispatcher.reply(context, embed.build());
            AudioManager.getGuildAudioManager(context.getGuild()).getScheduler().queue.clear();
            return;
        }

        // Clear specific track
        if(Sanitiser.isNumeric(context.getParameters())) {
            GuildAudioManager manager = AudioManager.getGuildAudioManager(context.getGuild());
            LinkedList<AudioTrack> temp = new LinkedList<>();
            int clear = Integer.parseInt(context.getParameters()) - 1;
            int i = 0;
            for(AudioTrack track: manager.getScheduler().queue) {
                if(clear == i++) {
                    EmbedBuilder embed = new EmbedBuilder()
                            .setTitle(context.i18n( "title"))
                            .setDescription(context.i18n( "desc_formatted").formatted(track.getInfo().title));
                    MessageDispatcher.reply(context, embed.build());
                    continue; // if we find the track we want to skip - continue loop to skip adding to temp list
                }
                temp.addLast(track);
            }

            manager.getScheduler().queue.clear();
            manager.getScheduler().queue.addAll(temp);
        }
    }

}
