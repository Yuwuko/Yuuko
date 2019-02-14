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
        for(Role role: e.getGuild().getRoleCache()) {
            roles.append(role.getAsMention()).append(" (").append(role.getColor().toString()).append(")").append("\n");
        }
        TextUtility.removeLastOccurrence(roles, "\n");

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle(e.getGuild().getName() + " Roles")
                .setDescription(roles.toString())
                .setFooter(Configuration.STANDARD_STRINGS[1] + e.getMember().getEffectiveName(), e.getAuthor().getEffectiveAvatarUrl());
        MessageHandler.sendMessage(e, embed.build());
    }
}
