package com.yuuko.modules.core.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.modules.Command;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;

public class PermissionsCommand extends Command {

    public PermissionsCommand() {
        super("permissions", 0, -1L, Arrays.asList("-permissions"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) throws Exception {
        String permissions = e.getGuild().getSelfMember().getPermissions().toString().replace("[", "").replace("]", "").replace(",", "\n");
        EmbedBuilder about = new EmbedBuilder().setTitle("Permissions")
                .setDescription("One of the most common reasons for commands not to work is lack of required permissions. Below are all of the permissions that I current have.")
                .addField("Granted", permissions, true);
        MessageDispatcher.reply(e, about.build());
    }
}
