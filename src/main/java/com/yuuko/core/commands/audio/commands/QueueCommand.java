package com.yuuko.core.commands.audio.commands;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.yuuko.core.Configuration;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.audio.AudioModule;
import com.yuuko.core.commands.audio.handlers.AudioManagerManager;
import com.yuuko.core.commands.audio.handlers.GuildAudioManager;
import com.yuuko.core.utilities.MessageHandler;
import com.yuuko.core.utilities.TextUtility;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.concurrent.atomic.AtomicLong;

public class QueueCommand extends Command {

    public QueueCommand() {
        super("queue", AudioModule.class, 0, new String[]{"-queue"}, false, null);
    }

    @Override
    public void onCommand(MessageReceivedEvent e, String[] command) {
        GuildAudioManager manager = AudioManagerManager.getGuildAudioManager(e.getGuild().getId());

        try {
            synchronized(manager.scheduler.queue) {
                StringBuilder queue = new StringBuilder();
                int trackCount = 0;
                for(AudioTrack track : manager.scheduler.queue) {
                    queue.append("`").append(trackCount+1).append(":` ").append(track.getInfo().title).append(" · (").append(TextUtility.getTimestamp(track.getInfo().length)).append(") \n");
                    trackCount++;
                    if(trackCount > 9) {
                        break;
                    }
                }

                final AtomicLong totalDuration = new AtomicLong();
                manager.scheduler.queue.forEach(audioTrack -> totalDuration.getAndAdd(audioTrack.getDuration()));

                if(trackCount > 0) {
                    EmbedBuilder nextTracks = new EmbedBuilder()
                            .setTitle("Here are the next " + trackCount + " tracks in the queue:")
                            .setDescription(queue.toString())
                            .addField("In Queue", manager.scheduler.queue.size() + "", true)
                            .addField("Total Duration", TextUtility.getTimestamp(totalDuration.get()), true)
                            .setFooter(Configuration.STANDARD_STRINGS[1] + e.getMember().getEffectiveName(), e.getAuthor().getEffectiveAvatarUrl());
                    MessageHandler.sendMessage(e, nextTracks.build());
                } else {
                    EmbedBuilder embed = new EmbedBuilder().setTitle("Queue").setDescription("The queue currently contains **0** tracks.");
                    MessageHandler.sendMessage(e, embed.build());
                }
            }
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
        }
    }

}
