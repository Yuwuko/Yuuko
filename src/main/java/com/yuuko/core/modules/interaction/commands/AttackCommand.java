package com.yuuko.core.modules.interaction.commands;

import com.yuuko.core.modules.Command;
import com.yuuko.core.modules.interaction.InteractionModule;
import com.yuuko.core.utilities.MessageHandler;
import com.yuuko.core.utilities.MessageUtility;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Random;

public class AttackCommand extends Command {
    private static final String[] interactionImage = new String[]{
            "https://i.imgur.com/jRrzvo9.gif",
            "https://i.imgur.com/CVyED1B.gif",
            "https://i.imgur.com/dTS1TPa.gif",
            "https://i.imgur.com/bRWRAcE.gif",
            "https://i.imgur.com/P3AAEPC.gif"
    };

    public AttackCommand() {
        super("attack", InteractionModule.class, 1, new String[]{"-attack @user"}, null);
    }

    @Override
    public void executeCommand(MessageReceivedEvent e, String[] command) {
        Member target = MessageUtility.getFirstMentionedMember(e);
        if(target != null) {
            EmbedBuilder embed = new EmbedBuilder().setDescription("**" + e.getMember().getEffectiveName() + "** attacks **" + target.getEffectiveName() + "**.").setImage(interactionImage[new Random().nextInt(interactionImage.length -1)]);
            MessageHandler.sendMessage(e, embed.build());
        }
    }

}
