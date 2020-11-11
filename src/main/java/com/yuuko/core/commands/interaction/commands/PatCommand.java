package com.yuuko.core.commands.interaction.commands;

import com.yuuko.core.Configuration;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.interaction.InteractionCommand;
import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.utilities.MessageUtilities;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

import java.util.Arrays;
import java.util.List;

public class PatCommand extends InteractionCommand {
    private static final List<String> interactionImage = Arrays.asList(
            "https://i.imgur.com/2lacG7l.gif",
            "https://i.imgur.com/UWbKpx8.gif",
            "https://i.imgur.com/4ssddEQ.gif",
            "https://i.imgur.com/2k0MFIr.gif",
            "https://i.imgur.com/NNOz81F.gif"
    );

    public PatCommand() {
        super("pat", Configuration.MODULES.get("interaction"), 1, -1L, Arrays.asList("-pat @user"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        Member target = MessageUtilities.getMentionedMember(e, true);
        if(target != null) {
            EmbedBuilder embed = new EmbedBuilder().setDescription("**" + e.getMember().getEffectiveName() + "** pats **" + target.getEffectiveName() + "**.").setImage(interactionImage.get(getRandom(interactionImage.size())));
            MessageHandler.sendMessage(e, embed.build());
        }
    }
}
