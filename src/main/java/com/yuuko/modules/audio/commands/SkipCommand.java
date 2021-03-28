package com.yuuko.modules.audio.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.i18n.I18n;
import com.yuuko.modules.Command;
import com.yuuko.modules.audio.handlers.AudioManager;
import com.yuuko.modules.audio.handlers.GuildAudioManager;
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
            EmbedBuilder embed = new EmbedBuilder().setTitle(I18n.getError(e, "no_track"));
            MessageDispatcher.reply(e, embed.build());
            return;
        }

        EmbedBuilder embed = new EmbedBuilder().setTitle(I18n.getText(e, "title")).setDescription(manager.getPlayer().getPlayingTrack().getInfo().title);
        MessageDispatcher.reply(e, embed.build());
        manager.getScheduler().nextTrack();
    }

}
