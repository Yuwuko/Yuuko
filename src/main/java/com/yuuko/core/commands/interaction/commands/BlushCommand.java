package com.yuuko.core.commands.interaction.commands;

import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.interaction.InteractionModule;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Random;

public class BlushCommand extends Command {

    private static final String[] interactionImage = new String[]{
            "https://i.imgur.com/I3apoUB.gif",
            "https://i.imgur.com/0lfLa2Z.gif",
            "https://i.imgur.com/wH4j7ns.gif",
            "https://i.imgur.com/8OKsNcT.gif",
            "https://i.imgur.com/CyjUV4W.gif"
    };

    public BlushCommand() {
        super("blush", InteractionModule.class, 0, new String[]{"-blush"}, false, null);
    }

    @Override
    public void onCommand(MessageReceivedEvent e, String[] command) {
        EmbedBuilder embed = new EmbedBuilder().setDescription("**" + e.getMember().getEffectiveName() + "** blushes.").setImage(interactionImage[new Random().nextInt(interactionImage.length -1)]);
        MessageHandler.sendMessage(e, embed.build());
    }
}
