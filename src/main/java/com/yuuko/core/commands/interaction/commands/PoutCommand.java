package com.yuuko.core.commands.interaction.commands;

import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.interaction.InteractionModule;
import com.yuuko.core.utilities.MessageHandler;
import com.yuuko.core.utilities.MessageUtility;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Random;

public class PoutCommand extends Command {
    private static final String[] interactionImage = new String[]{
            "https://i.imgur.com/WdQgoDb.gif",
            "https://i.imgur.com/FgsaLup.gif",
            "https://i.imgur.com/kAarI7f.gif",
            "https://i.imgur.com/uQzTgI7.gif",
            "https://i.imgur.com/12BxZLf.gif"
    };

    public PoutCommand() {
        super("pout", InteractionModule.class, 0, new String[]{"-pout", "-pout @user"}, null);
    }

    @Override
    public void executeCommand(MessageReceivedEvent e, String[] command) {
        if(MessageUtility.checkIfUserMentioned(e)) {
            Member target = MessageUtility.getFirstMentionedMember(e);
            if(target != null) {
                EmbedBuilder embed = new EmbedBuilder().setDescription("**" + e.getMember().getEffectiveName() + "** pouts at **" + target.getEffectiveName() + "**.").setImage(interactionImage[new Random().nextInt(interactionImage.length - 1)]);
                MessageHandler.sendMessage(e, embed.build());
            }
        } else {
            EmbedBuilder embed = new EmbedBuilder().setDescription("**" + e.getMember().getEffectiveName() + "** pouts.").setImage(interactionImage[new Random().nextInt(interactionImage.length -1)]);
            MessageHandler.sendMessage(e, embed.build());
        }
    }

}
