package com.yuuko.core.commands.interaction.commands;

import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.interaction.InteractionCommand;
import com.yuuko.core.commands.interaction.InteractionModule;
import com.yuuko.core.events.entity.MessageEvent;
import net.dv8tion.jda.core.EmbedBuilder;

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
        super("blush", InteractionModule.class, 0, Arrays.asList("-blush"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        EmbedBuilder embed = new EmbedBuilder().setDescription("**" + e.getMember().getEffectiveName() + "** blushes.").setImage(interactionImage.get(random(interactionImage.size())));
        MessageHandler.sendMessage(e, embed.build());
    }
}
