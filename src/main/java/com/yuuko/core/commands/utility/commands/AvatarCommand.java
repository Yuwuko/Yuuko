package com.yuuko.core.commands.utility.commands;

import com.yuuko.core.Configuration;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.utility.UtilityModule;
import com.yuuko.core.utilities.MessageUtilities;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class AvatarCommand extends Command {

    public AvatarCommand() {
        super("avatar", UtilityModule.class, 1, new String[]{"-avatar @user", "-avatar <userId>"}, false, null);
    }

    @Override
    public void onCommand(MessageReceivedEvent e, String[] command) {
        String[] commandParameters = command[1].split("\\s+", 2);
        Member target = MessageUtilities.getMentionedMember(e, commandParameters, true);

        if(target == null) {
            return;
        }

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle(target.getUser().getName() + "#" + target.getUser().getDiscriminator() + "'s Avatar")
                .setImage(target.getUser().getEffectiveAvatarUrl() + "?size=256&.gif")
                .setFooter(Configuration.STANDARD_STRINGS[1] + e.getMember().getEffectiveName(), e.getAuthor().getEffectiveAvatarUrl());
        MessageHandler.sendMessage(e, embed.build());
    }
}
