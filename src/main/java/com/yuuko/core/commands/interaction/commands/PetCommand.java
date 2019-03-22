package com.yuuko.core.commands.interaction.commands;

import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.interaction.InteractionModule;
import com.yuuko.core.utilities.MessageUtilities;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Random;

public class PetCommand extends Command {

    private static final String[] interactionImage = new String[]{
            "https://i.imgur.com/4ssddEQ.gif",
            "https://i.imgur.com/UWbKpx8.gif",
            "https://i.imgur.com/2k0MFIr.gif",
            "https://i.imgur.com/6xfbS1q.gif",
            "https://i.imgur.com/KRsZdho.gif"
    };

    public PetCommand() {
        super("pet", InteractionModule.class, 1, new String[]{"-pet @user"}, false, null);
    }

    @Override
    public void onCommand(MessageReceivedEvent e, String[] command) {
        Member target = MessageUtilities.getMentionedMember(e, null, true);
        if(target != null) {
            EmbedBuilder embed = new EmbedBuilder().setDescription("**" + e.getMember().getEffectiveName() + "** pets **" + target.getEffectiveName() + "**.").setImage(interactionImage[new Random().nextInt(interactionImage.length -1)]);
            MessageHandler.sendMessage(e, embed.build());
        }
    }
}
