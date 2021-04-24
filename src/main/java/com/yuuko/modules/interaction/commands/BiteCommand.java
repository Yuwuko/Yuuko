package com.yuuko.modules.interaction.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.modules.Command;
import net.dv8tion.jda.api.EmbedBuilder;

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
        super("bite", Arrays.asList("-bite @user"), 1);
    }

    @Override
    public void onCommand(MessageEvent context) throws Exception {
        if(!context.getMessage().getMentionedMembers().isEmpty()) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setDescription(context.i18n( "target").formatted(context.getMember().getEffectiveName(), context.getMessage().getMentionedMembers().get(0).getEffectiveName()))
                    .setImage(interactionImage.get(getRandom(interactionImage.size())));
            MessageDispatcher.sendMessage(context, embed.build());
        }
    }
}
