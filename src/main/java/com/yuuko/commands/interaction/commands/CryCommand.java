package com.yuuko.commands.interaction.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.Yuuko;
import com.yuuko.commands.Command;
import com.yuuko.events.entity.MessageEvent;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;
import java.util.List;

public class CryCommand extends Command {
    private static final List<String> interactionImage = Arrays.asList(
            "https://i.imgur.com/X2AemjJ.gif",
            "https://i.imgur.com/3LAPKgh.gif",
            "https://i.imgur.com/87EBrze.gif",
            "https://i.imgur.com/im5XQUl.gif",
            "https://i.imgur.com/fGaUNGF.gif"
    );

    public CryCommand() {
        super("cry", Yuuko.MODULES.get("interaction"), 0, -1L, Arrays.asList("-cry"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) throws Exception {
        EmbedBuilder embed = new EmbedBuilder().setDescription("**" + e.getMember().getEffectiveName() + "** cries.").setImage(interactionImage.get(getRandom(interactionImage.size())));
        MessageDispatcher.sendMessage(e, embed.build());
    }
}
