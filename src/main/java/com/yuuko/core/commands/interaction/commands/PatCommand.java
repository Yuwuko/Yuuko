package com.yuuko.core.commands.interaction.commands;

import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.interaction.InteractionModule;
import com.yuuko.core.events.extensions.MessageEvent;
import com.yuuko.core.utilities.MessageUtilities;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;

import java.util.Random;

public class PatCommand extends Command {

    private static final String[] interactionImage = new String[]{
            "https://stop-talking-to.me/yuuko/pat/1.gif",
            "https://stop-talking-to.me/yuuko/pat/2.gif",
            "https://stop-talking-to.me/yuuko/pat/3.gif",
            "https://stop-talking-to.me/yuuko/pat/4.gif",
            "https://stop-talking-to.me/yuuko/pat/5.gif"
    };

    public PatCommand() {
        super("pat", InteractionModule.class, 1, new String[]{"-pat @user"}, false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        Member target = MessageUtilities.getMentionedMember(e, true);
        if(target != null) {
            EmbedBuilder embed = new EmbedBuilder().setDescription("**" + e.getMember().getEffectiveName() + "** pats **" + target.getEffectiveName() + "**.").setImage(interactionImage[new Random().nextInt(interactionImage.length -1)]);
            MessageHandler.sendMessage(e, embed.build());
        }
    }
}
