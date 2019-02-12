package com.yuuko.core.commands.interaction.commands;

import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.interaction.InteractionModule;
import com.yuuko.core.utilities.MessageHandler;
import com.yuuko.core.utilities.MessageUtilities;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Random;

public class KissCommand extends Command {
    private static final String[] interactionImage = new String[]{
            "https://i.imgur.com/sGVgr74.gif",
            "https://i.imgur.com/TItLfqh.gif",
            "https://i.imgur.com/YbNv10F.gif",
            "https://i.imgur.com/wQjUdnZ.gif",
            "https://i.imgur.com/lmY5soG.gif"
    };

    public KissCommand() {
        super("kiss", InteractionModule.class, 1, new String[]{"-kiss @user"}, false, null);
    }

    @Override
    public void onCommand(MessageReceivedEvent e, String[] command) {
        Member target = MessageUtilities.getMentionedMember(e, null, true);
        if(target != null) {
            EmbedBuilder embed = new EmbedBuilder().setDescription("**" + e.getMember().getEffectiveName() + "** kisses **" + target.getEffectiveName() + "**.").setImage(interactionImage[new Random().nextInt(interactionImage.length -1)]);
            MessageHandler.sendMessage(e, embed.build());
        }
    }
}
