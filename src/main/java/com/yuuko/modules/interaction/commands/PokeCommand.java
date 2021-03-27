package com.yuuko.modules.interaction.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.modules.Command;
import com.yuuko.utilities.MessageUtilities;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

import java.util.Arrays;
import java.util.List;

public class PokeCommand extends Command {
    private static final List<String> interactionImage = Arrays.asList(
            "https://i.imgur.com/7l5duGX.gif",
            "https://i.imgur.com/CYxJyxQ.gif",
            "https://i.imgur.com/iy019QA.gif",
            "https://i.imgur.com/VhwMdAW.gif",
            "https://i.imgur.com/fZmUTgw.gif"
    );

    public PokeCommand() {
        super("poke", 1, -1L, Arrays.asList("-poke @user"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) throws Exception {
        Member target = MessageUtilities.getMentionedMember(e, true);
        if(target != null) {
            EmbedBuilder embed = new EmbedBuilder().setDescription("**" + e.getMember().getEffectiveName() + "** pokes **" + target.getEffectiveName() + "**.").setImage(interactionImage.get(getRandom(interactionImage.size())));
            MessageDispatcher.sendMessage(e, embed.build());
        }
    }
}
