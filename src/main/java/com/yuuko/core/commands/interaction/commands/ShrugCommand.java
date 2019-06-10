package com.yuuko.core.commands.interaction.commands;

import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.interaction.InteractionModule;
import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.utilities.MessageUtilities;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ShrugCommand extends Command {

    private static final List<String> interactionImage = Arrays.asList(
            "https://i.imgur.com/ghlye0C.gif",
            "https://i.imgur.com/nUacE87.gif",
            "https://i.imgur.com/0ttnPkG.gif",
            "https://i.imgur.com/1Pfi4Qp.gif",
            "https://i.imgur.com/EaAgfes.gif"
    );

    public ShrugCommand() {
        super("shrug", InteractionModule.class, 0, Arrays.asList("-shrug", "-shrug @user"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        if(MessageUtilities.checkIfUserMentioned(e)) {
            Member target = MessageUtilities.getMentionedMember(e, true);
            if(target != null) {
                EmbedBuilder embed = new EmbedBuilder().setDescription("**" + e.getMember().getEffectiveName() + "** shrugs at **" + target.getEffectiveName() + "**.").setImage(interactionImage.get(new Random().nextInt(interactionImage.size() - 1)));
                MessageHandler.sendMessage(e, embed.build());
            }
        } else {
            EmbedBuilder embed = new EmbedBuilder().setDescription("**" + e.getMember().getEffectiveName() + "** shrugs.").setImage(interactionImage.get(new Random().nextInt(interactionImage.size() -1)));
            MessageHandler.sendMessage(e, embed.build());
        }
    }

}
