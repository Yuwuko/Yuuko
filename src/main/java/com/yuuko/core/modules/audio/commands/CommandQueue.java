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

public class CommandQueue extends Command {

    public CommandQueue() {
        super("queue", "com.yuuko.core.modules.audio.ModuleAudio", 0, new String[]{"-queue"}, null);
    }

    @Override
    public void executeCommand(MessageReceivedEvent e, String[] command) {
        GuildAudioManager manager = AudioManagerManager.getGuildAudioManager(e.getGuild().getId());

        try {
            synchronized(manager.scheduler.queue) {
                StringBuilder queue = new StringBuilder();
                int i = 1;

                for(AudioTrack track : manager.scheduler.queue) {
                    queue.append("`").append(i).append(":` ").append(track.getInfo().title).append(" · (").append(Utils.getTimestamp(track.getInfo().length)).append(") \n");
                    i++;
                    if(i > 10) {
                        break;
                    }
                }
                i--;

                if(i > 0) {
                    EmbedBuilder nextTracks = new EmbedBuilder()
                            .setTitle("Here are the next " + i + " tracks in the queue:")
                            .setDescription(queue.toString())
                            .setFooter(Cache.STANDARD_STRINGS[1] + e.getMember().getEffectiveName(), e.getGuild().getMemberById(Configuration.BOT_ID).getUser().getAvatarUrl());
                    MessageHandler.sendMessage(e, nextTracks.build());

                } else {
                    EmbedBuilder embed = new EmbedBuilder().setTitle("The queue is currently empty.");
                    MessageHandler.sendMessage(e, embed.build());
                }
            }
        } catch(Exception ex) {
            MessageHandler.sendException(ex, e.getMessage().getContentRaw());
        }
    }

}
