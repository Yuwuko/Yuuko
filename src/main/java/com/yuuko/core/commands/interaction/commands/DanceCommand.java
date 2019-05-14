package com.yuuko.core.commands.interaction.commands;

import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.interaction.InteractionModule;
import com.yuuko.core.events.extensions.MessageEvent;
import com.yuuko.core.utilities.MessageUtilities;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;

import java.util.Random;

public class DanceCommand extends Command {

    private static final String[] interactionImage = new String[]{
            "https://stop-talking-to.me/yuuko/dance/1.gif",
            "https://stop-talking-to.me/yuuko/dance/2.gif",
            "https://stop-talking-to.me/yuuko/dance/3.gif",
            "https://stop-talking-to.me/yuuko/dance/4.gif",
            "https://stop-talking-to.me/yuuko/dance/5.gif"
    };

    public DanceCommand() {
        super("dance", InteractionModule.class, 0, new String[]{"-dance", "-dance @user"}, false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        if(MessageUtilities.checkIfUserMentioned(e)) {
            Member target = MessageUtilities.getMentionedMember(e, true);
            if(target != null) {
                EmbedBuilder embed = new EmbedBuilder().setDescription("**" + e.getMember().getEffectiveName() + "** dances with **" + target.getEffectiveName() + "**.").setImage(interactionImage[new Random().nextInt(interactionImage.length -1)]);
                MessageHandler.sendMessage(e, embed.build());
            }
        } else {
            EmbedBuilder embed = new EmbedBuilder().setDescription("**" + e.getMember().getEffectiveName() + "** dances.").setImage(interactionImage[new Random().nextInt(interactionImage.length -1)]);
            MessageHandler.sendMessage(e, embed.build());
        }
    }
}
