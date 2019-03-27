package com.yuuko.core.commands.interaction.commands;

import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.interaction.InteractionModule;
import com.yuuko.core.events.extensions.MessageEvent;
import net.dv8tion.jda.core.EmbedBuilder;

import java.util.Random;

public class CryCommand extends Command {

    private static final String[] interactionImage = new String[]{
            "https://i.imgur.com/X2AemjJ.gif",
            "https://i.imgur.com/3LAPKgh.gif",
            "https://i.imgur.com/87EBrze.gif",
            "https://i.imgur.com/im5XQUl.gif",
            "https://i.imgur.com/fGaUNGF.gif"
    };

    public CryCommand() {
        super("cry", InteractionModule.class, 0, new String[]{"-cry"}, false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        EmbedBuilder embed = new EmbedBuilder().setDescription("**" + e.getMember().getEffectiveName() + "** cries.").setImage(interactionImage[new Random().nextInt(interactionImage.length -1)]);
        MessageHandler.sendMessage(e, embed.build());
    }

}
