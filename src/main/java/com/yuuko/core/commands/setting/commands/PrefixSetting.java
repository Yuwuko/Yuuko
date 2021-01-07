package com.yuuko.core.commands.setting.commands;

import com.yuuko.core.Config;
import com.yuuko.core.MessageDispatcher;
import com.yuuko.core.commands.Command;
import com.yuuko.core.database.function.GuildFunctions;
import com.yuuko.core.events.entity.MessageEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;

import java.util.Arrays;

public class PrefixSetting extends Command {

    public PrefixSetting() {
        super("prefix", Config.MODULES.get("setting"), 1, -1L, Arrays.asList("-prefix <prefix>"), false, Arrays.asList(Permission.MANAGE_SERVER));
    }

    public void onCommand(MessageEvent e) throws Exception {
        if(e.getParameters().length() < 1 || e.getParameters().length() > 5) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Parameter").setDescription("Input must have a minimum length of `1` and a maximum length of `5` characters.");
            MessageDispatcher.reply(e, embed.build());
            return;
        }

        if(GuildFunctions.setGuildSettings("prefix", e.getParameters(), e.getGuild().getId())) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Prefix Changed").setDescription("The prefix used for this guild has been set to `" + e.getParameters() + "`");
            MessageDispatcher.reply(e, embed.build());
        }
    }
}
