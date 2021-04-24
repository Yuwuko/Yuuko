package com.yuuko.modules.interaction.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.modules.Command;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;
import java.util.List;

public class PoutCommand extends Command {
    private static final List<String> interactionImage = Arrays.asList(
            "https://i.imgur.com/WdQgoDb.gif",
            "https://i.imgur.com/FgsaLup.gif",
            "https://i.imgur.com/kAarI7f.gif",
            "https://i.imgur.com/uQzTgI7.gif",
            "https://i.imgur.com/12BxZLf.gif"
    );

    public PoutCommand() {
        super("pout", Arrays.asList("-pout", "-pout @user"));
    }

    @Override
    public void onCommand(MessageEvent context) throws Exception {
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
