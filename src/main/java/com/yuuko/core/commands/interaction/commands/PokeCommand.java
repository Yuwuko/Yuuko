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

public class PokeCommand extends Command {

    private static final List<String> interactionImage = Arrays.asList(
            "https://i.imgur.com/7l5duGX.gif",
            "https://i.imgur.com/CYxJyxQ.gif",
            "https://i.imgur.com/iy019QA.gif",
            "https://i.imgur.com/VhwMdAW.gif",
            "https://i.imgur.com/fZmUTgw.gif"
    );

    public PokeCommand() {
        super("poke", InteractionModule.class, 1, Arrays.asList("-poke @user"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        Member target = MessageUtilities.getMentionedMember(e, true);
        if(target != null) {
            EmbedBuilder embed = new EmbedBuilder().setDescription("**" + e.getMember().getEffectiveName() + "** pokes **" + target.getEffectiveName() + "**.").setImage(interactionImage.get(new Random().nextInt(interactionImage.size() -1)));
            MessageHandler.sendMessage(e, embed.build());
        }
    }

}
