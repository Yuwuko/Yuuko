package com.yuuko.modules.interaction.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.modules.Command;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;
import java.util.List;

public class SleepCommand extends Command {
    private static final List<String> interactionImage = Arrays.asList(
            "https://i.imgur.com/W5SEYT6.gif",
            "https://i.imgur.com/7AOboGB.gif",
            "https://i.imgur.com/u559Fgp.gif",
            "https://i.imgur.com/qWm5FfT.gif",
            "https://i.imgur.com/1wDyaRE.gif"
    );

    public SleepCommand() {
        super("sleep", Arrays.asList("-sleep"));
    }

    @Override
    public void onCommand(MessageEvent context) {
        EmbedBuilder embed = new EmbedBuilder()
                .setDescription(context.i18n( "self").formatted(context.getMember().getEffectiveName()))
                .setImage(interactionImage.get(getRandom(interactionImage.size())));
        MessageDispatcher.sendMessage(context, embed.build());
    }
}
