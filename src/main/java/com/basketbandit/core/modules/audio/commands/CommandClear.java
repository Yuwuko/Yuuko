package com.basketbandit.core.modules.audio.commands;

import com.basketbandit.core.modules.Command;
import com.basketbandit.core.modules.audio.handlers.AudioManagerManager;
import com.basketbandit.core.modules.audio.handlers.GuildAudioManager;
import com.basketbandit.core.utils.MessageHandler;
import com.basketbandit.core.utils.Utils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.LinkedList;
import java.util.Queue;

public class CommandClear extends Command {

    public CommandClear() {
        super("clear", "com.basketbandit.core.modules.audio.ModuleAudio", 0, new String[]{"-clear", "-clear 3"}, null);
    }

    @Override
    public void executeCommand(MessageReceivedEvent e, String[] command) {
        try {
            GuildAudioManager manager = AudioManagerManager.getGuildAudioManager(e.getGuild().getId());

            if(command.length > 1) {
                int clearPos;

                try {
                    clearPos = Integer.parseInt(command[1]);
                } catch (Exception ex) {
                    return;
                }

                Queue<com.sedmelluq.discord.lavaplayer.track.AudioTrack> temp = new LinkedList<>();
                Queue<com.sedmelluq.discord.lavaplayer.track.AudioTrack> clone = new LinkedList<>();
                clone.addAll(manager.scheduler.queue);

                int i = 1;
                for(int x = 0; x < manager.scheduler.queue.size(); x++) {
                    if(i == clearPos) {
                        EmbedBuilder embed = new EmbedBuilder().setTitle("Clearing").setDescription("**" + clone.remove().getInfo().title + "** has been cleared from the queue.");
                        MessageHandler.sendMessage(e, embed.build());
                        i++;
                    } else {
                        ((LinkedList<com.sedmelluq.discord.lavaplayer.track.AudioTrack>) temp).addLast(clone.remove());
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
            Utils.sendException(ex, e.getMessage().getContentRaw());
        }
    }

}
