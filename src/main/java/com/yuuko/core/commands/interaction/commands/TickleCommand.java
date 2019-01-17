package com.yuuko.core.commands.interaction.commands;

import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.interaction.InteractionModule;
import com.yuuko.core.utilities.MessageHandler;
import com.yuuko.core.utilities.MessageUtility;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Random;

public class TickleCommand extends Command {
    private static final String[] interactionImage = new String[]{
            "https://i.imgur.com/lapBsXk.gif",
            "https://i.imgur.com/LNk70yS.gif",
            "https://i.imgur.com/5icCYBp.gif",
            "https://i.imgur.com/TgN4DFW.gif",
            "https://i.imgur.com/s4SgOQX.gif"
    };

    public TickleCommand() {
        super("tickle", InteractionModule.class, 1, new String[]{"-tickle @user"}, null);
    }

    @Override
    public void executeCommand(MessageReceivedEvent e, String[] command) {
        Member target = MessageUtility.getFirstMentionedMember(e);
        if(target != null) {
            EmbedBuilder embed = new EmbedBuilder().setDescription("**" + e.getMember().getEffectiveName() + "** tickles **" + target.getEffectiveName() + "**.").setImage(interactionImage[new Random().nextInt(interactionImage.length -1)]);
            MessageHandler.sendMessage(e, embed.build());
        }
    }

}
