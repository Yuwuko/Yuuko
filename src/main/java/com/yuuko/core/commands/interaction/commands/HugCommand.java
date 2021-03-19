package com.yuuko.core.commands.interaction.commands;

import com.yuuko.core.MessageDispatcher;
import com.yuuko.core.Yuuko;
import com.yuuko.core.commands.Command;
import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.utilities.MessageUtilities;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

import java.util.Arrays;
import java.util.List;

public class HugCommand extends Command {
    private static final List<String> interactionImage = Arrays.asList(
            "https://i.imgur.com/wOmoeF8.gif",
            "https://i.imgur.com/ntqYLGl.gif",
            "https://i.imgur.com/v47M1S4.gif",
            "https://i.imgur.com/cZWWATV.gif",
            "https://i.imgur.com/CxmswPU.gif"
    );

    public HugCommand() {
        super("hug", Yuuko.MODULES.get("interaction"), 1, -1L, Arrays.asList("-hug @user"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) throws Exception {
        Member target = MessageUtilities.getMentionedMember(e, true);
        if(target != null) {
            EmbedBuilder embed = new EmbedBuilder().setDescription("**" + e.getMember().getEffectiveName() + "** hugs **" + target.getEffectiveName() + "**.").setImage(interactionImage.get(getRandom(interactionImage.size())));
            MessageDispatcher.sendMessage(e, embed.build());
        }
    }
}
