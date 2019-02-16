package com.yuuko.core.commands.utility.commands;

import com.yuuko.core.Configuration;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.utility.UtilityModule;
import com.yuuko.core.utilities.MessageHandler;
import com.yuuko.core.utilities.TextUtility;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class RolesCommand extends Command {

    public RolesCommand() {
        super("roles", UtilityModule.class, 0, new String[]{"-roles"}, false, null);
    }

    @Override
    public void onCommand(MessageReceivedEvent e, String[] command) {
        StringBuilder roles = new StringBuilder();

        if(!e.getGuild().getRoleCache().isEmpty()) {
            int characterCount = 0;

            for(Role role : e.getGuild().getRoleCache()) {
                if(characterCount + role.getAsMention().length() + 2 < 2048) {
                    roles.append(role.getAsMention()).append("\n");
                    characterCount += role.getAsMention().length() + 1;
                }
            }
            TextUtility.removeLastOccurrence(roles, "\n");
        } else {
            roles.append("None Available");
        }

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle(e.getGuild().getName() + " Roles")
                .setDescription(roles.toString())
                .setFooter(Configuration.STANDARD_STRINGS[1] + e.getMember().getEffectiveName(), e.getAuthor().getEffectiveAvatarUrl());
        MessageHandler.sendMessage(e, embed.build());
    }
}
