package com.yuuko.core.commands.interaction.commands;

import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.interaction.InteractionModule;
import com.yuuko.core.utilities.MessageHandler;
import com.yuuko.core.utilities.MessageUtilities;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Random;

public class AngryCommand extends Command {
    private static final String[] interactionImage = new String[]{
            "https://i.imgur.com/BLGBP1f.gif",
            "https://i.imgur.com/fWh43XO.gif",
            "https://i.imgur.com/T8vX3hy.gif",
            "https://i.imgur.com/20064tE.gif",
            "https://i.imgur.com/bTJofwO.gif"
    };

    public AngryCommand() {
        super("angry", InteractionModule.class, 0, new String[]{"-angry", "-angry @user"}, false, null);
    }

    @Override
    public void onCommand(MessageReceivedEvent e, String[] command) {
        if(MessageUtilities.checkIfUserMentioned(e)) {
            Member target = MessageUtilities.getFirstMentionedMember(e);
            if(target != null) {
                EmbedBuilder embed = new EmbedBuilder().setDescription("**" + e.getMember().getEffectiveName() + "** is angry at **" + target.getEffectiveName() + "**.").setImage(interactionImage[new Random().nextInt(interactionImage.length - 1)]);
                MessageHandler.sendMessage(e, embed.build());
            }
        } else {
            EmbedBuilder embed = new EmbedBuilder().setDescription("**" + e.getMember().getEffectiveName() + "** is angry.").setImage(interactionImage[new Random().nextInt(interactionImage.length -1)]);
            MessageHandler.sendMessage(e, embed.build());
        }
    }

}
