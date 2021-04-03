package com.yuuko.modules.utility.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.Yuuko;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.modules.Command;
import com.yuuko.utilities.MessageUtilities;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

import java.util.Arrays;

public class AvatarCommand extends Command {

    public AvatarCommand() {
        super("avatar", 1, -1L, Arrays.asList("-avatar @user", "-avatar <userId>"), false, null);
    }

    @Override
    public void onCommand(MessageEvent context) throws Exception {
        Member target = MessageUtilities.getMentionedMember(context, true);

        if(target == null) {
            return;
        }

        User user = target.getUser();
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle(user.getName() + "#" + user.getDiscriminator() + "'s Avatar")
                .setImage(user.getEffectiveAvatarUrl() + "?size=256&.gif")
                .setFooter(Yuuko.STANDARD_STRINGS.get(1) + context.getAuthor().getAsTag(), context.getAuthor().getEffectiveAvatarUrl());
        MessageDispatcher.reply(context, embed.build());
    }
}
