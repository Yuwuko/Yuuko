package com.basketbandit.core.modules.audio.commands;

import com.basketbandit.core.modules.Command;
import com.basketbandit.core.modules.audio.handlers.AudioManagerHandler;
import com.basketbandit.core.modules.audio.handlers.GuildAudioManager;
import com.basketbandit.core.utils.Utils;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.LinkedList;
import java.util.Queue;

public class CommandClearQueue extends Command {

    public CommandClearQueue() {
        super("clear", "com.basketbandit.core.modules.audio.ModuleAudio", new String[]{"-clear", "-clear 3"}, null);
    }

    public CommandClearQueue(MessageReceivedEvent e, String[] command) {
        executeCommand(e, command);
    }

    @Override
    protected void executeCommand(MessageReceivedEvent e, String[] command) {
        GuildAudioManager manager = AudioManagerHandler.getGuildAudioManager(e.getGuild().getId());

        if(command.length > 1) {
            int clearPos;

            try {
                clearPos = Integer.parseInt(command[1]);
            } catch(Exception ex) {
                return;
            }

            Queue<com.sedmelluq.discord.lavaplayer.track.AudioTrack> temp = new LinkedList<>();
            Queue<com.sedmelluq.discord.lavaplayer.track.AudioTrack> clone = new LinkedList<>();
            clone.addAll(manager.scheduler.queue);

            int i = 1;
            for(int x = 0; x < manager.scheduler.queue.size(); x++) {
                if(i == clearPos) {
                    Utils.sendMessage(e,e.getAuthor().getAsMention() + " has removed '" + clone.remove().getInfo().title + "' from the queue.");
                    i++;
                } else {
                    ((LinkedList<com.sedmelluq.discord.lavaplayer.track.AudioTrack>) temp).addLast(clone.remove());
                    i++;
                }
            }
            manager.scheduler.queue.clear();
            manager.scheduler.queue.addAll(temp);

        } else {
            manager.scheduler.queue.clear();
            Utils.sendMessage(e, e.getAuthor().getAsMention() + " cleared the queue.");
        }
    }

}
