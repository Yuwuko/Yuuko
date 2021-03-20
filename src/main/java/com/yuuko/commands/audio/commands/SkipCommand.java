package com.yuuko.commands.audio.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.commands.Command;
import com.yuuko.commands.audio.handlers.AudioManager;
import com.yuuko.commands.audio.handlers.GuildAudioManager;
import com.yuuko.events.entity.MessageEvent;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;

public class SkipCommand extends Command {

    public SkipCommand() {
        super("skip", 0, -1L, Arrays.asList("-skip"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) throws Exception {
        GuildAudioManager manager = AudioManager.getGuildAudioManager(e.getGuild());
        if(manager.getPlayer().getPlayingTrack() == null) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("There is no track to skip.");
            MessageDispatcher.reply(e, embed.build());
            return;
        }

        EmbedBuilder embed = new EmbedBuilder().setTitle("Skipping").setDescription(manager.getPlayer().getPlayingTrack().getInfo().title);
        MessageDispatcher.reply(e, embed.build());
        manager.getScheduler().nextTrack();
    }

}
