package com.yuuko.core.commands.core.commands;

import com.yuuko.core.Configuration;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.database.function.GuildFunctions;
import com.yuuko.core.events.entity.MessageEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;

import java.util.Arrays;

public class AdvertiseCommand extends Command {

    public AdvertiseCommand() {
        super("advertise", Configuration.MODULES.get("core"), 0, Arrays.asList("-advertise", "-advertise stop"), false, Arrays.asList(Permission.ADMINISTRATOR, Permission.CREATE_INSTANT_INVITE));
    }

    @Override
    public void onCommand(MessageEvent e) {
        if(!e.hasParameters()) {
            e.getMessage().getTextChannel().createInvite().setMaxAge(0).setMaxUses(0).setUnique(false)
                    .queue(s -> {
                        GuildFunctions.setGuildInvite(s.getURL(), e.getGuild().getId());

                        EmbedBuilder embed = new EmbedBuilder().setTitle("Advertise")
                                .setDescription("Server is now being advertised on `www.yuuko.info` using invite code `" + s.getCode() + "`");
                        MessageHandler.sendMessage(e, embed.build());
                        });
            return;
        }

        if(e.getParameters().equals("stop")) {
            GuildFunctions.setGuildInvite(null, e.getGuild().getId());

            EmbedBuilder embed = new EmbedBuilder().setTitle("Advertise")
                    .setDescription("Invite link has been successfully removed.");
            MessageHandler.sendMessage(e, embed.build());
        }
    }
}
