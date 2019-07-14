package com.yuuko.core.commands.interaction.commands;

import com.yuuko.core.Configuration;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.interaction.InteractionCommand;
import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.utilities.MessageUtilities;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;

import java.util.Arrays;
import java.util.List;

public class AttackCommand extends InteractionCommand {

    private static final List<String> interactionImage = Arrays.asList(
            "https://i.imgur.com/jRrzvo9.gif",
            "https://i.imgur.com/CVyED1B.gif",
            "https://i.imgur.com/dTS1TPa.gif",
            "https://i.imgur.com/bRWRAcE.gif",
            "https://i.imgur.com/P3AAEPC.gif"
    );

    public AttackCommand() {
        super("attack", Configuration.MODULES.get("interaction"), 1, Arrays.asList("-attack @user"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        Member target = MessageUtilities.getMentionedMember(e, true);
        if(target != null) {
            EmbedBuilder embed = new EmbedBuilder().setDescription("**" + e.getMember().getEffectiveName() + "** attacks **" + target.getEffectiveName() + "**.").setImage(interactionImage.get(random(interactionImage.size())));
            MessageHandler.sendMessage(e, embed.build());
        }
    }

}
