package com.yuuko.modules.audio.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.Yuuko;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.modules.Command;
import com.yuuko.modules.audio.handlers.AudioManager;
import com.yuuko.modules.audio.handlers.GuildAudioManager;
import com.yuuko.utilities.TextUtilities;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class QueueCommand extends Command {

    public QueueCommand() {
        super("queue", Arrays.asList("-queue"));
    }

    @Override
    public void onCommand(MessageEvent context) throws Exception {
        GuildAudioManager manager = AudioManager.getGuildAudioManager(context.getGuild());
        synchronized(manager.getScheduler().queue) {
            if(manager.getScheduler().queue.size() < 1) {
                EmbedBuilder embed = new EmbedBuilder().setTitle(context.i18n( "empty_title")).setDescription(context.i18n( "desc"));
                MessageDispatcher.reply(context, embed.build());
                return;
            }

            StringBuilder queue = new StringBuilder();
            AtomicLong nextDuration = new AtomicLong();
            AtomicLong totalDuration = new AtomicLong();
            AtomicInteger count = new AtomicInteger();

            manager.getScheduler().queue.stream().limit(10).forEach(audioTrack -> {
                count.getAndIncrement();
                queue.append("`").append(count.get() + 1).append(":` ").append(audioTrack.getInfo().title).append(" · (").append(TextUtilities.getTimestamp(audioTrack.getInfo().length)).append(") \n");
                nextDuration.getAndAdd(audioTrack.getDuration());
                totalDuration.getAndAdd(audioTrack.getDuration());
            });

            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(context.i18n( "full_title").formatted(count.get()))
                    .setDescription(queue.toString())
                    .addField(context.i18n( "queue_length"), manager.getScheduler().queue.size() + "", true)
                    .addField(context.i18n( "next_duration").formatted(count.get()), TextUtilities.getTimestamp(nextDuration.get()), true)
                    .addField(context.i18n( "total_duration"), TextUtilities.getTimestamp(totalDuration.get()), true)
                    .setFooter(Yuuko.STANDARD_STRINGS.get(1) + context.getAuthor().getAsTag(), context.getAuthor().getEffectiveAvatarUrl());
            MessageDispatcher.reply(context, embed.build());
        }
    }

}
