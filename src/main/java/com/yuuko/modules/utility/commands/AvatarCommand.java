package com.yuuko.modules.utility.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.Yuuko;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.modules.Command;
import com.yuuko.utilities.MessageUtilities;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

import java.util.Arrays;

public class AvatarCommand extends Command {

    public AvatarCommand() {
        super("avatar", Arrays.asList("-avatar @user", "-avatar <userId>"), 1);
    }

    @Override
    public void onCommand(MessageEvent context) {
        Member target = MessageUtilities.getMentionedMember(context, true);

        if(target == null) {
            return;
        }

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle(context.i18n("title").formatted(target.getUser().getAsTag()))
                .setImage(target.getUser().getEffectiveAvatarUrl() + "?size=256&.gif")
                .setFooter(Yuuko.STANDARD_STRINGS.get(1) + context.getAuthor().getAsTag(), context.getAuthor().getEffectiveAvatarUrl());
        MessageDispatcher.reply(context, embed.build());
    }
}
