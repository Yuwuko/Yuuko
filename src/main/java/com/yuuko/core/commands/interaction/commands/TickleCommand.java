package com.yuuko.core.commands.interaction.commands;

import com.yuuko.core.Config;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.interaction.InteractionCommand;
import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.utilities.MessageUtilities;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

import java.util.Arrays;
import java.util.List;

public class TickleCommand extends InteractionCommand {
    private static final List<String> interactionImage = Arrays.asList(
            "https://i.imgur.com/lapBsXk.gif",
            "https://i.imgur.com/LNk70yS.gif",
            "https://i.imgur.com/5icCYBp.gif",
            "https://i.imgur.com/TgN4DFW.gif",
            "https://i.imgur.com/s4SgOQX.gif"
    );

    public TickleCommand() {
        super("tickle", Config.MODULES.get("interaction"), 1, -1L, Arrays.asList("-tickle @user"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        Member target = MessageUtilities.getMentionedMember(e, true);
        if(target != null) {
            EmbedBuilder embed = new EmbedBuilder().setDescription("**" + e.getMember().getEffectiveName() + "** tickles **" + target.getEffectiveName() + "**.").setImage(interactionImage.get(getRandom(interactionImage.size())));
            MessageHandler.sendMessage(e, embed.build());
        }
    }
}
