package com.yuuko.core.commands.audio.commands;

import com.yuuko.core.MessageDispatcher;
import com.yuuko.core.Yuuko;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.audio.handlers.AudioManager;
import com.yuuko.core.commands.audio.handlers.GuildAudioManager;
import com.yuuko.core.events.entity.MessageEvent;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;

public class SkipCommand extends Command {

    public SkipCommand() {
        super("skip", Yuuko.MODULES.get("audio"), 0, -1L, Arrays.asList("-skip"), false, null);
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
