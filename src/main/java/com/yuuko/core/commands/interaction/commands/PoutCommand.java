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

public class PoutCommand extends InteractionCommand {
    private static final List<String> interactionImage = Arrays.asList(
            "https://i.imgur.com/WdQgoDb.gif",
            "https://i.imgur.com/FgsaLup.gif",
            "https://i.imgur.com/kAarI7f.gif",
            "https://i.imgur.com/uQzTgI7.gif",
            "https://i.imgur.com/12BxZLf.gif"
    );

    public PoutCommand() {
        super("pout", Configuration.MODULES.get("interaction"), 0, -1L, Arrays.asList("-pout", "-pout @user"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        if(MessageUtilities.checkIfUserMentioned(e)) {
            Member target = MessageUtilities.getMentionedMember(e, true);
            if(target != null) {
                EmbedBuilder embed = new EmbedBuilder().setDescription("**" + e.getMember().getEffectiveName() + "** pouts at **" + target.getEffectiveName() + "**.").setImage(interactionImage.get(getRandom(interactionImage.size())));
                MessageHandler.sendMessage(e, embed.build());
            }
        } else {
            EmbedBuilder embed = new EmbedBuilder().setDescription("**" + e.getMember().getEffectiveName() + "** pouts.").setImage(interactionImage.get(getRandom(interactionImage.size())));
            MessageHandler.sendMessage(e, embed.build());
        }
    }

}
