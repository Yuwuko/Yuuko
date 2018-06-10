package com.basketbandit.core.modules.audio.commands;

import com.basketbandit.core.Configuration;
import com.basketbandit.core.Utils;
import com.basketbandit.core.modules.Command;
import com.basketbandit.core.modules.audio.ModuleAudio;
import com.basketbandit.core.modules.audio.handlers.AudioManagerHandler;
import com.basketbandit.core.modules.audio.handlers.GuildAudioManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;

public class CommandQueue extends Command {

    public CommandQueue() {
        super("queue", "com.basketbandit.core.modules.audio.ModuleAudio", new String[]{"-queue"}, null);
    }

    public CommandQueue(MessageReceivedEvent e, String[] command) {
        executeCommand(e, command);
    }

    @Override
    protected void executeCommand(MessageReceivedEvent e, String[] command) {
        GuildAudioManager manager = AudioManagerHandler.getGuildAudioManager(e.getGuild().getId());

        synchronized(manager.scheduler.queue) {
            StringBuilder queue = new StringBuilder();
            int i = 1;

            for(AudioTrack track : manager.scheduler.queue) {
                queue.append(i).append(": ").append(track.getInfo().title).append(", (").append(ModuleAudio.getTimestamp(track.getInfo().length)).append(") \n");
                i++;
                if(i > 10) {
                    break;
                }
            }
            i--;

            if(i > 0) {
                EmbedBuilder nextTracks = new EmbedBuilder()
                        .setColor(Color.RED)
                        .setAuthor("Hey " + e.getMember().getEffectiveName() + ",", null, e.getAuthor().getAvatarUrl())
                        .setTitle("Here are the next " + i + " tracks in the queue:")
                        .setDescription(queue.toString())
                        .setFooter("Version: " + Configuration.VERSION, e.getGuild().getMemberById(Configuration.BOT_ID).getUser().getAvatarUrl());

                Utils.sendMessage(e, nextTracks.build());
            } else {
                Utils.sendMessage(e, "Sorry " + e.getAuthor().getAsMention() + ", the queue is empty!");
            }
        }
    }

}
