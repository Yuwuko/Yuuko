package com.yuuko.core.commands.interaction.commands;

import com.yuuko.core.Config;
import com.yuuko.core.MessageDispatcher;
import com.yuuko.core.commands.interaction.InteractionCommand;
import com.yuuko.core.events.entity.MessageEvent;
import net.dv8tion.jda.api.EmbedBuilder;

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
        super("sleep", Config.MODULES.get("interaction"), 0, -1L, Arrays.asList("-sleep"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) throws Exception {
        EmbedBuilder embed = new EmbedBuilder().setDescription("**" + e.getMember().getEffectiveName() + "** goes to sleep.").setImage(interactionImage.get(getRandom(interactionImage.size())));
        MessageDispatcher.sendMessage(e, embed.build());
    }
}
