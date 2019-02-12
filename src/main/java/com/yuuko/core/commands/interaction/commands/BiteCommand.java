package com.yuuko.core.commands.interaction.commands;

import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.interaction.InteractionModule;
import com.yuuko.core.utilities.MessageHandler;
import com.yuuko.core.utilities.MessageUtilities;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Random;

public class BiteCommand extends Command {
    private static final String[] interactionImage = new String[]{
            "https://i.imgur.com/8jGdNWU.gif",
            "https://i.imgur.com/wcBbh3R.gif",
            "https://i.imgur.com/wb14mqC.gif",
            "https://i.imgur.com/wXFwpHo.gif",
            "https://i.imgur.com/UTdoVpQ.gif"
    };

    public BiteCommand() {
        super("bite", InteractionModule.class, 1, new String[]{"-bite @user"}, false, null);
    }

    @Override
    public void onCommand(MessageReceivedEvent e, String[] command) {
        Member target = MessageUtilities.getMentionedMember(e, null, true);
        if(target != null) {
            EmbedBuilder embed = new EmbedBuilder().setDescription("**" + e.getMember().getEffectiveName() + "** bites **" + target.getEffectiveName() + "**.").setImage(interactionImage[new Random().nextInt(interactionImage.length -1)]);
            MessageHandler.sendMessage(e, embed.build());
        }
    }

}
