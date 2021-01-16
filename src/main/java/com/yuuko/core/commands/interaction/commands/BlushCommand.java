package com.yuuko.core.commands.interaction.commands;

import com.yuuko.core.MessageDispatcher;
import com.yuuko.core.Yuuko;
import com.yuuko.core.commands.interaction.InteractionCommand;
import com.yuuko.core.events.entity.MessageEvent;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;
import java.util.List;

public class BlushCommand extends InteractionCommand {
    private static final List<String> interactionImage = Arrays.asList(
            "https://i.imgur.com/I3apoUB.gif",
            "https://i.imgur.com/0lfLa2Z.gif",
            "https://i.imgur.com/wH4j7ns.gif",
            "https://i.imgur.com/8OKsNcT.gif",
            "https://i.imgur.com/CyjUV4W.gif"
    );

    public BlushCommand() {
        super("blush", Yuuko.MODULES.get("interaction"), 0, -1L, Arrays.asList("-blush"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) throws Exception {
        EmbedBuilder embed = new EmbedBuilder().setDescription("**" + e.getMember().getEffectiveName() + "** blushes.").setImage(interactionImage.get(getRandom(interactionImage.size())));
        MessageDispatcher.sendMessage(e, embed.build());
    }
}
