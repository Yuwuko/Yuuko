package com.yuuko.modules.interaction.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.modules.Command;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;
import java.util.List;

public class LaughCommand extends Command {
    private static final List<String> interactionImage = Arrays.asList(
            "https://i.imgur.com/SGboaP0.gif",
            "https://i.imgur.com/S0m2mfm.gif",
            "https://i.imgur.com/12T0WK1.gif",
            "https://i.imgur.com/1i53Pu5.gif",
            "https://i.imgur.com/EgOdPmj.gif"
    );

    public LaughCommand() {
        super("laugh", Arrays.asList("-laugh", "-laugh @user"));
    }

    @Override
    public void onCommand(MessageEvent context) {
        if(!context.getMessage().getMentionedMembers().isEmpty()) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setDescription(context.i18n( "target").formatted(context.getMember().getEffectiveName(), context.getMessage().getMentionedMembers().get(0).getEffectiveName()))
                    .setImage(interactionImage.get(getRandom(interactionImage.size())));
            MessageDispatcher.sendMessage(context, embed.build());
            return;
        }
        EmbedBuilder embed = new EmbedBuilder().setDescription(context.i18n( "self").formatted(context.getMember().getEffectiveName())).setImage(interactionImage.get(getRandom(interactionImage.size())));
        MessageDispatcher.sendMessage(context, embed.build());
    }
}
