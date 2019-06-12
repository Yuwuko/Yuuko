package com.yuuko.core.commands.interaction.commands;

import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.interaction.InteractionCommand;
import com.yuuko.core.commands.interaction.InteractionModule;
import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.utilities.MessageUtilities;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;

import java.util.Arrays;
import java.util.List;

public class KissCommand extends InteractionCommand {

    private static final List<String> interactionImage = Arrays.asList(
            "https://i.imgur.com/sGVgr74.gif",
            "https://i.imgur.com/TItLfqh.gif",
            "https://i.imgur.com/YbNv10F.gif",
            "https://i.imgur.com/wQjUdnZ.gif",
            "https://i.imgur.com/lmY5soG.gif"
    );

    public KissCommand() {
        super("kiss", InteractionModule.class, 1, Arrays.asList("-kiss @user"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        Member target = MessageUtilities.getMentionedMember(e, true);
        if(target != null) {
            EmbedBuilder embed = new EmbedBuilder().setDescription("**" + e.getMember().getEffectiveName() + "** kisses **" + target.getEffectiveName() + "**.").setImage(interactionImage.get(random(interactionImage.size())));
            MessageHandler.sendMessage(e, embed.build());
        }
    }
}
