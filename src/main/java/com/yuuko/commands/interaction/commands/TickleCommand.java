package com.yuuko.commands.interaction.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.commands.Command;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.utilities.MessageUtilities;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

import java.util.Arrays;
import java.util.List;

public class TickleCommand extends Command {
    private static final List<String> interactionImage = Arrays.asList(
            "https://i.imgur.com/lapBsXk.gif",
            "https://i.imgur.com/LNk70yS.gif",
            "https://i.imgur.com/5icCYBp.gif",
            "https://i.imgur.com/TgN4DFW.gif",
            "https://i.imgur.com/s4SgOQX.gif"
    );

    public TickleCommand() {
        super("tickle", 1, -1L, Arrays.asList("-tickle @user"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) throws Exception {
        Member target = MessageUtilities.getMentionedMember(e, true);
        if(target != null) {
            EmbedBuilder embed = new EmbedBuilder().setDescription("**" + e.getMember().getEffectiveName() + "** tickles **" + target.getEffectiveName() + "**.").setImage(interactionImage.get(getRandom(interactionImage.size())));
            MessageDispatcher.sendMessage(e, embed.build());
        }
    }
}
