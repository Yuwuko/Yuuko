package com.yuuko.core.commands.audio.commands;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.audio.AudioModule;
import com.yuuko.core.commands.audio.handlers.AudioManagerManager;
import com.yuuko.core.commands.audio.handlers.GuildAudioManager;
import com.yuuko.core.events.extensions.MessageEvent;
import com.yuuko.core.utilities.Sanitiser;
import net.dv8tion.jda.core.EmbedBuilder;

import java.util.LinkedList;
import java.util.Queue;

public class ClearCommand extends Command {

    public ClearCommand() {
        super("clear", AudioModule.class, 0, new String[]{"-clear", "-clear <position>"}, false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        try {
            GuildAudioManager manager = AudioManagerManager.getGuildAudioManager(e.getGuild().getId());

            if(e.getCommand().length > 1) {
                final int clearPos;

                if(Sanitiser.isNumber(e.getCommandParameter())) {
                    clearPos = Integer.parseInt(e.getCommandParameter());
                } else {
                    return;
                }

                LinkedList<AudioTrack> temp = new LinkedList<>();
                Queue<AudioTrack> clone = new LinkedList<>(manager.scheduler.queue);

                int i = 1;
                for(int x = 0; x < manager.scheduler.queue.size(); x++) {
                    if(i == clearPos) {
                        EmbedBuilder embed = new EmbedBuilder().setTitle("Clearing").setDescription("**" + clone.remove().getInfo().title + "** has been cleared from the queue.");
                        MessageHandler.sendMessage(e, embed.build());
                        i++;
                    } else {
                        temp.addLast(clone.remove());
                        i++;
                    }
                }
                manager.scheduler.queue.clear();
                manager.scheduler.queue.addAll(temp);

            } else {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Clearing").setDescription("The queue has been cleared.");
                MessageHandler.sendMessage(e, embed.build());
                manager.scheduler.queue.clear();
            }
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
        }
    }

}
