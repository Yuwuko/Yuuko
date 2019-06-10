package com.yuuko.core.commands.interaction.commands;

import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.interaction.InteractionModule;
import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.utilities.MessageUtilities;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class KillCommand extends Command {

    private static final List<String> interactionImage = Arrays.asList(
            "https://stop-talking-to.me/yuuko/kill/1.gif",
            "https://stop-talking-to.me/yuuko/kill/2.gif",
            "https://stop-talking-to.me/yuuko/kill/3.gif",
            "https://stop-talking-to.me/yuuko/kill/4.gif",
            "https://stop-talking-to.me/yuuko/kill/5.gif"
    );

    public KillCommand() {
        super("kill", InteractionModule.class, 1, Arrays.asList("-kill @user"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        Member target = MessageUtilities.getMentionedMember(e, true);
        if(target != null) {
            EmbedBuilder embed = new EmbedBuilder().setDescription("**" + e.getMember().getEffectiveName() + "** kills **" + target.getEffectiveName() + "**.").setImage(interactionImage.get(new Random().nextInt(interactionImage.size() -1)));
            MessageHandler.sendMessage(e, embed.build());
        }
    }
}
