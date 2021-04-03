package com.yuuko.modules.interaction.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.i18n.I18n;
import com.yuuko.modules.Command;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;
import java.util.List;

public class BlushCommand extends Command {
    private static final List<String> interactionImage = Arrays.asList(
            "https://i.imgur.com/I3apoUB.gif",
            "https://i.imgur.com/0lfLa2Z.gif",
            "https://i.imgur.com/wH4j7ns.gif",
            "https://i.imgur.com/8OKsNcT.gif",
            "https://i.imgur.com/CyjUV4W.gif"
    );

    public BlushCommand() {
        super("blush", 0, -1L, Arrays.asList("-blush"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) throws Exception {
        EmbedBuilder embed = new EmbedBuilder().setDescription(I18n.getText(e, "self").formatted(e.getMember().getEffectiveName())).setImage(interactionImage.get(getRandom(interactionImage.size())));
        MessageDispatcher.sendMessage(e, embed.build());
    }
}
