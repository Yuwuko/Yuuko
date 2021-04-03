package com.yuuko.modules.utility.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.Yuuko;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.modules.Command;
import com.yuuko.utilities.TextUtilities;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;

import java.util.Arrays;

public class RolesCommand extends Command {

    public RolesCommand() {
        super("roles", 0, -1L, Arrays.asList("-roles"), false, null);
    }

    @Override
    public void onCommand(MessageEvent context) throws Exception {
        StringBuilder roles = new StringBuilder();

        if(!context.getGuild().getRoleCache().isEmpty()) {
            int characterCount = 0;

            for(Role role : context.getGuild().getRoleCache()) {
                if(characterCount + role.getAsMention().length() + 2 < 2048) {
                    roles.append(role.getAsMention()).append("\n");
                    characterCount += role.getAsMention().length() + 1;
                }
            }
            TextUtilities.removeLast(roles, "\n");
        } else {
            roles.append("None Available");
        }

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle(context.getGuild().getName() + " Roles")
                .setDescription(roles.toString())
                .setFooter(Yuuko.STANDARD_STRINGS.get(1) + context.getAuthor().getAsTag(), context.getAuthor().getEffectiveAvatarUrl());
        MessageDispatcher.reply(context, embed.build());
    }
}
