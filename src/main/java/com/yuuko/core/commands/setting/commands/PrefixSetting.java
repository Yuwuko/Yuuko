package com.yuuko.core.commands.setting.commands;

import com.yuuko.core.Configuration;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.database.function.GuildFunctions;
import com.yuuko.core.events.entity.MessageEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;

import java.util.Arrays;

public class PrefixSetting extends Command {

    public PrefixSetting() {
        super("prefix", Configuration.MODULES.get("setting"), 1, -1L, Arrays.asList("-prefix <prefix>"), false, Arrays.asList(Permission.MANAGE_SERVER));
    }

    public void onCommand(MessageEvent e) {
        if(e.getParameters().length() < 1 || e.getParameters().length() > 5) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Parameter").setDescription("Input must have a minimum length of `1` and a maximum length of `5` characters.");
            MessageHandler.sendMessage(e, embed.build());
            return;
        }

        if(GuildFunctions.setGuildSettings("prefix", e.getParameters(), e.getGuild().getId())) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Prefix Changed").setDescription("The prefix used for this guild has been set to `" + e.getParameters() + "`");
            MessageHandler.sendMessage(e, embed.build());
        }
    }
}
