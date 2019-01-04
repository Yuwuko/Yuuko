package com.yuuko.core.modules.interaction.commands;

import com.yuuko.core.modules.Command;
import com.yuuko.core.modules.interaction.InteractionModule;
import com.yuuko.core.utilities.MessageHandler;
import com.yuuko.core.utilities.MessageUtility;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Random;

public class PokeCommand extends Command {

    private static final String[] interactionImage = new String[]{
            "https://i.imgur.com/7l5duGX.gif",
            "https://i.imgur.com/CYxJyxQ.gif",
            "https://i.imgur.com/iy019QA.gif",
            "https://i.imgur.com/VhwMdAW.gif",
            "https://i.imgur.com/fZmUTgw.gif"
    };

    public PokeCommand() {
        super("poke", InteractionModule.class, 1, new String[]{"-poke @user"}, null);
    }

    @Override
    public void executeCommand(MessageReceivedEvent e, String[] command) {
        if(MessageUtility.getFirstMentionedMember(e) != null) {
            EmbedBuilder embed = new EmbedBuilder().setDescription("**" + e.getMember().getEffectiveName() + "** pokes **" + MessageUtility.getFirstMentionedMember(e).getEffectiveName() + "**.").setImage(interactionImage[new Random().nextInt(interactionImage.length -1)]);
            MessageHandler.sendMessage(e, embed.build());
        }
    }

}
