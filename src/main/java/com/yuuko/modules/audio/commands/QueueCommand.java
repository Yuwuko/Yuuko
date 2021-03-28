package com.yuuko.modules.audio.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.Yuuko;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.i18n.I18n;
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
        super("queue", 0, -1L, Arrays.asList("-queue"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) throws Exception {
        GuildAudioManager manager = AudioManager.getGuildAudioManager(e.getGuild());
        synchronized(manager.getScheduler().queue) {
            if(manager.getScheduler().queue.size() < 1) {
                EmbedBuilder embed = new EmbedBuilder().setTitle(I18n.getText(e, "empty_title")).setDescription(I18n.getText(e, "desc"));
                MessageDispatcher.reply(e, embed.build());
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
                    .setTitle(I18n.getText(e, "full_title").formatted(count.get()))
                    .setDescription(queue.toString())
                    .addField(I18n.getText(e, "queue_length"), manager.getScheduler().queue.size() + "", true)
                    .addField(I18n.getText(e, "next_duration").formatted(count.get()), TextUtilities.getTimestamp(nextDuration.get()), true)
                    .addField(I18n.getText(e, "total_duration"), TextUtilities.getTimestamp(totalDuration.get()), true)
                    .setFooter(Yuuko.STANDARD_STRINGS.get(1) + e.getAuthor().getAsTag(), e.getAuthor().getEffectiveAvatarUrl());
            MessageDispatcher.reply(e, embed.build());
        }
    }

}
