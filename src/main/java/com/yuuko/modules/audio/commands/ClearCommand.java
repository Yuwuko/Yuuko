package com.yuuko.modules.audio.commands;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.yuuko.MessageDispatcher;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.i18n.I18n;
import com.yuuko.modules.Command;
import com.yuuko.modules.audio.handlers.AudioManager;
import com.yuuko.modules.audio.handlers.GuildAudioManager;
import com.yuuko.utilities.Sanitiser;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;
import java.util.LinkedList;

public class ClearCommand extends Command {

    public ClearCommand() {
        super("clear", 0, -1L, Arrays.asList("-clear", "-clear <position>"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) throws Exception {
        // Clear entire queue
        if(!e.hasParameters()) {
            EmbedBuilder embed = new EmbedBuilder().setTitle(I18n.getText(e, "title")).setDescription(I18n.getText(e, "desc"));
            MessageDispatcher.reply(e, embed.build());
            AudioManager.getGuildAudioManager(e.getGuild()).getScheduler().queue.clear();
            return;
        }

        // Clear specific track
        if(Sanitiser.isNumeric(e.getParameters())) {
            GuildAudioManager manager = AudioManager.getGuildAudioManager(e.getGuild());
            LinkedList<AudioTrack> temp = new LinkedList<>();
            int clear = Integer.parseInt(e.getParameters()) - 1;
            int i = 0;
            for(AudioTrack track: manager.getScheduler().queue) {
                if(clear == i++) {
                    EmbedBuilder embed = new EmbedBuilder().setTitle(I18n.getText(e, "title")).setDescription(I18n.getText(e, "desc_formatted").formatted(track.getInfo().title));
                    MessageDispatcher.reply(e, embed.build());
                    continue; // if we find the track we want to skip - continue loop to skip adding to temp list
                }
                temp.addLast(track);
            }

            manager.getScheduler().queue.clear();
            manager.getScheduler().queue.addAll(temp);
        }
    }

}
