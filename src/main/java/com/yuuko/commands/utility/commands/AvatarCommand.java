package com.yuuko.commands.utility.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.Yuuko;
import com.yuuko.commands.Command;
import com.yuuko.events.entity.MessageEvent;
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
    public void onCommand(MessageEvent e) throws Exception {
        Member target = MessageUtilities.getMentionedMember(e, true);

        if(target == null) {
            return;
        }

        User user = target.getUser();
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle(user.getName() + "#" + user.getDiscriminator() + "'s Avatar")
                .setImage(user.getEffectiveAvatarUrl() + "?size=256&.gif")
                .setFooter(Yuuko.STANDARD_STRINGS.get(1) + e.getAuthor().getAsTag(), e.getAuthor().getEffectiveAvatarUrl());
        MessageDispatcher.reply(e, embed.build());
    }
}
