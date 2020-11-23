package com.yuuko.core.commands.utility.commands;

import com.yuuko.core.Config;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.utilities.MessageUtilities;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

import java.util.Arrays;

public class AvatarCommand extends Command {

    public AvatarCommand() {
        super("avatar", Config.MODULES.get("utility"), 1, -1L, Arrays.asList("-avatar @user", "-avatar <userId>"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        Member target = MessageUtilities.getMentionedMember(e, true);

        if(target == null) {
            return;
        }

        User user = target.getUser();
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle(user.getName() + "#" + user.getDiscriminator() + "'s Avatar")
                .setImage(user.getEffectiveAvatarUrl() + "?size=256&.gif")
                .setFooter(Config.STANDARD_STRINGS.get(1) + e.getMember().getEffectiveName(), e.getAuthor().getEffectiveAvatarUrl());
        MessageHandler.reply(e, embed.build());
    }
}
