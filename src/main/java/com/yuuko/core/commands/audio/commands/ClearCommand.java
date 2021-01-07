package com.yuuko.core.commands.audio.commands;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.yuuko.core.Config;
import com.yuuko.core.MessageDispatcher;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.audio.handlers.AudioManagerController;
import com.yuuko.core.commands.audio.handlers.GuildAudioManager;
import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.utilities.Sanitiser;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;
import java.util.LinkedList;

public class ClearCommand extends Command {

    public ClearCommand() {
        super("clear", Config.MODULES.get("audio"), 0, -1L, Arrays.asList("-clear", "-clear <position>"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        if(!e.hasParameters()) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Clearing").setDescription("The queue has been cleared.");
            MessageDispatcher.reply(e, embed.build());
            AudioManagerController.getGuildAudioManager(e.getGuild()).getScheduler().queue.clear();
            return;
        }

        if(!Sanitiser.isNumber(e.getParameters())) {
            return;
        }

        GuildAudioManager manager = AudioManagerController.getGuildAudioManager(e.getGuild());
        LinkedList<AudioTrack> temp = new LinkedList<>();
        int clearPos = Integer.parseInt(e.getParameters());
        int i = 0;
        for(AudioTrack track: manager.getScheduler().queue) {
            if(clearPos - 1 == i++) {
                // clearPos - 1 so we can clear the 0th item in queue
                EmbedBuilder embed = new EmbedBuilder().setTitle("Clearing").setDescription("`" + track.getInfo().title + "` has been cleared from the queue.");
                MessageDispatcher.reply(e, embed.build());
                continue;
            }
            temp.addLast(track);
        }

        manager.getScheduler().queue.clear();
        manager.getScheduler().queue.addAll(temp);
    }

}
