package com.yuuko.modules.interaction.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.modules.Command;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;
import java.util.List;

public class ShrugCommand extends Command {
    private static final List<String> interactionImage = Arrays.asList(
            "https://i.imgur.com/ghlye0C.gif",
            "https://i.imgur.com/nUacE87.gif",
            "https://i.imgur.com/0ttnPkG.gif",
            "https://i.imgur.com/1Pfi4Qp.gif",
            "https://i.imgur.com/EaAgfes.gif"
    );

    public ShrugCommand() {
        super("shrug", Arrays.asList("-shrug", "-shrug @user"));
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
