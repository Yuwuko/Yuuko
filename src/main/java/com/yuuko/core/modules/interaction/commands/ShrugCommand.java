package com.yuuko.core.modules.interaction.commands;

import com.yuuko.core.modules.Command;
import com.yuuko.core.modules.interaction.InteractionModule;
import com.yuuko.core.utilities.MessageHandler;
import com.yuuko.core.utilities.MessageUtility;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Random;

public class ShrugCommand extends Command {
    private static final String[] interactionImage = new String[]{
            "https://i.imgur.com/ghlye0C.gif",
            "https://i.imgur.com/nUacE87.gif",
            "https://i.imgur.com/0ttnPkG.gif",
            "https://i.imgur.com/1Pfi4Qp.gif",
            "https://i.imgur.com/EaAgfes.gif"
    };

    public ShrugCommand() {
        super("shrug", InteractionModule.class, 0, new String[]{"-shrug", "-shrug @user"}, null);
    }

    @Override
    public void executeCommand(MessageReceivedEvent e, String[] command) {
        if(MessageUtility.checkIfUserMentioned(e)) {
            Member target = MessageUtility.getFirstMentionedMember(e);
            if(target != null) {
                EmbedBuilder embed = new EmbedBuilder().setDescription("**" + e.getMember().getEffectiveName() + "** shrugs at **" + target.getEffectiveName() + "**.").setImage(interactionImage[new Random().nextInt(interactionImage.length - 1)]);
                MessageHandler.sendMessage(e, embed.build());
            }
        } else {
            EmbedBuilder embed = new EmbedBuilder().setDescription("**" + e.getMember().getEffectiveName() + "** shrugs.").setImage(interactionImage[new Random().nextInt(interactionImage.length -1)]);
            MessageHandler.sendMessage(e, embed.build());
        }
    }

}
