package com.yuuko.core.commands.interaction.commands;

import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.interaction.InteractionModule;
import com.yuuko.core.utilities.MessageHandler;
import com.yuuko.core.utilities.MessageUtility;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Random;

public class HugCommand extends Command {
    private static final String[] interactionImage = new String[]{
            "https://i.imgur.com/wOmoeF8.gif",
            "https://i.imgur.com/ntqYLGl.gif",
            "https://i.imgur.com/v47M1S4.gif",
            "https://i.imgur.com/cZWWATV.gif",
            "https://i.imgur.com/CxmswPU.gif"
    };

    public HugCommand() {
        super("hug", InteractionModule.class, 1, new String[]{"-hug @user"}, null);
    }

    @Override
    public void executeCommand(MessageReceivedEvent e, String[] command) {
        Member target = MessageUtility.getFirstMentionedMember(e);
        if(target != null) {
            EmbedBuilder embed = new EmbedBuilder().setDescription("**" + e.getMember().getEffectiveName() + "** hugs **" + target.getEffectiveName() + "**.").setImage(interactionImage[new Random().nextInt(interactionImage.length -1)]);
            MessageHandler.sendMessage(e, embed.build());
        }
    }

}
