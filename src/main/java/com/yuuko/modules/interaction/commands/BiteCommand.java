package com.yuuko.modules.interaction.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.modules.Command;
import com.yuuko.utilities.MessageUtilities;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

import java.util.Arrays;
import java.util.List;

public class BiteCommand extends Command {
    private static final List<String> interactionImage = Arrays.asList(
            "https://i.imgur.com/8jGdNWU.gif",
            "https://i.imgur.com/wcBbh3R.gif",
            "https://i.imgur.com/wb14mqC.gif",
            "https://i.imgur.com/wXFwpHo.gif",
            "https://i.imgur.com/UTdoVpQ.gif"
    );

    public BiteCommand() {
        super("bite", 1, -1L, Arrays.asList("-bite @user"), false, null);
    }

    @Override
    public void onCommand(MessageEvent context) throws Exception {
        Member target = MessageUtilities.getMentionedMember(context, true);
        if(target != null) {
            EmbedBuilder embed = new EmbedBuilder().setDescription(context.i18n( "target").formatted(context.getMember().getEffectiveName(), target.getEffectiveName())).setImage(interactionImage.get(getRandom(interactionImage.size())));
            MessageDispatcher.sendMessage(context, embed.build());
        }
    }
}
