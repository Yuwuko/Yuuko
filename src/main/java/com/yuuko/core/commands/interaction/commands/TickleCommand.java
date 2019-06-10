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

public class TickleCommand extends Command {

    private static final List<String> interactionImage = Arrays.asList(
            "https://i.imgur.com/lapBsXk.gif",
            "https://i.imgur.com/LNk70yS.gif",
            "https://i.imgur.com/5icCYBp.gif",
            "https://i.imgur.com/TgN4DFW.gif",
            "https://i.imgur.com/s4SgOQX.gif"
    );

    public TickleCommand() {
        super("tickle", InteractionModule.class, 1, Arrays.asList("-tickle @user"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        Member target = MessageUtilities.getMentionedMember(e, true);
        if(target != null) {
            EmbedBuilder embed = new EmbedBuilder().setDescription("**" + e.getMember().getEffectiveName() + "** tickles **" + target.getEffectiveName() + "**.").setImage(interactionImage.get(new Random().nextInt(interactionImage.size() -1)));
            MessageHandler.sendMessage(e, embed.build());
        }
    }

}
