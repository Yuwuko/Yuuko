package com.yuuko.core.commands.interaction.commands;

import com.yuuko.core.Configuration;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.interaction.InteractionCommand;
import com.yuuko.core.events.entity.MessageEvent;
import net.dv8tion.jda.core.EmbedBuilder;

import java.util.Arrays;
import java.util.List;

public class SleepCommand extends InteractionCommand {

    private static final List<String> interactionImage = Arrays.asList(
            "https://i.imgur.com/W5SEYT6.gif",
            "https://i.imgur.com/7AOboGB.gif",
            "https://i.imgur.com/u559Fgp.gif",
            "https://i.imgur.com/qWm5FfT.gif",
            "https://i.imgur.com/1wDyaRE.gif"
    );

    public SleepCommand() {
        super("sleep", Configuration.MODULES.get("interaction"), 0, Arrays.asList("-sleep"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        EmbedBuilder embed = new EmbedBuilder().setDescription("**" + e.getMember().getEffectiveName() + "** goes to sleep.").setImage(interactionImage.get(random(interactionImage.size())));
        MessageHandler.sendMessage(e, embed.build());
    }

}
