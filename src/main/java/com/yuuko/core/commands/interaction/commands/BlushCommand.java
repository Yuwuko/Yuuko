package com.yuuko.core.commands.interaction.commands;

import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.interaction.InteractionModule;
import com.yuuko.core.events.entity.MessageEvent;
import net.dv8tion.jda.core.EmbedBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class BlushCommand extends Command {

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
        EmbedBuilder embed = new EmbedBuilder().setDescription("**" + e.getMember().getEffectiveName() + "** blushes.").setImage(interactionImage.get(new Random().nextInt(interactionImage.size() -1)));
        MessageHandler.sendMessage(e, embed.build());
    }
}
