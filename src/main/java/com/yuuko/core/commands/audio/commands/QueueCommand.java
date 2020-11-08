package com.yuuko.core.commands.audio.commands;

import com.yuuko.core.Configuration;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.audio.handlers.AudioManagerController;
import com.yuuko.core.commands.audio.handlers.GuildAudioManager;
import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.utilities.TextUtilities;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class QueueCommand extends Command {

    public QueueCommand() {
        super("queue", Configuration.MODULES.get("audio"), 0, -1L, Arrays.asList("-queue"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        try {
            GuildAudioManager manager = AudioManagerController.getGuildAudioManager(e.getGuild());
            synchronized(manager.getScheduler().queue) {
                if(manager.getScheduler().queue.size() < 1) {
                    EmbedBuilder embed = new EmbedBuilder().setTitle("Queue").setDescription("The queue currently contains `0` tracks.");
                    MessageHandler.sendMessage(e, embed.build());
                    return;
                }

                StringBuilder queue = new StringBuilder();
                AtomicLong nextDuration = new AtomicLong();
                AtomicLong totalDuration = new AtomicLong();
                AtomicInteger count = new AtomicInteger();

                manager.getScheduler().queue.forEach(audioTrack -> {
                    if(count.get() < 10) {
                        count.getAndIncrement();
                        queue.append("`").append(count.get() + 1).append(":` ").append(audioTrack.getInfo().title).append(" · (").append(TextUtilities.getTimestamp(audioTrack.getInfo().length)).append(") \n");
                        nextDuration.getAndAdd(audioTrack.getDuration());
                    }
                    totalDuration.getAndAdd(audioTrack.getDuration());
                });

                EmbedBuilder nextTracks = new EmbedBuilder()
                        .setTitle("Here are the next " + count.get() + " tracks in the queue:")
                        .setDescription(queue.toString())
                        .addField("Queue Length", manager.getScheduler().queue.size() + "", true)
                        .addField("Next " + count.get() + " Duration", TextUtilities.getTimestamp(nextDuration.get()), true)
                        .addField("Total Duration", TextUtilities.getTimestamp(totalDuration.get()), true)
                        .setFooter(Configuration.STANDARD_STRINGS.get(1) + e.getMember().getEffectiveName(), e.getAuthor().getEffectiveAvatarUrl());
                MessageHandler.sendMessage(e, nextTracks.build());
            }
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
        }
    }

}
