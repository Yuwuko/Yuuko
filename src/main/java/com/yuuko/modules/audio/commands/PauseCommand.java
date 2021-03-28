package com.yuuko.modules.audio.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.i18n.I18n;
import com.yuuko.modules.Command;
import com.yuuko.modules.audio.handlers.AudioManager;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;

public class PauseCommand extends Command {

    public PauseCommand() {
        super("pause", 0, -1L, Arrays.asList("-pause"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) throws Exception {
        EmbedBuilder embed = new EmbedBuilder().setTitle(I18n.getText(e, "title")).setDescription(I18n.getText(e, "desc"));
        MessageDispatcher.reply(e, embed.build());
        AudioManager.getGuildAudioManager(e.getGuild()).getPlayer().setPaused(true);
    }

}
