package com.yuuko.commands.interaction.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.Yuuko;
import com.yuuko.commands.Command;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.utilities.MessageUtilities;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

import java.util.Arrays;
import java.util.List;

public class KillCommand extends Command {
    private static final List<String> interactionImage = Arrays.asList(
            "https://i.imgur.com/YGTBd1F.gif",
            "https://i.imgur.com/3L8lclp.gif",
            "https://i.imgur.com/3767VAs.gif",
            "https://i.imgur.com/8tPxaFx.gif",
            "https://i.imgur.com/Cin043Y.gif"
    );

    public KillCommand() {
        super("kill", Yuuko.MODULES.get("interaction"), 1, 1L, Arrays.asList("-kill @user"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) throws Exception {
        Member target = MessageUtilities.getMentionedMember(e, true);
        if(target != null) {
            EmbedBuilder embed = new EmbedBuilder().setDescription("**" + e.getMember().getEffectiveName() + "** kills **" + target.getEffectiveName() + "**.").setImage(interactionImage.get(getRandom(interactionImage.size())));
            MessageDispatcher.sendMessage(e, embed.build());
        }
    }
}
