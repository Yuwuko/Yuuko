package com.yuuko.modules.setting.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.database.function.GuildFunctions;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.modules.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;

import java.util.Arrays;

public class PrefixSetting extends Command {

    public PrefixSetting() {
        super("prefix", 1, -1L, Arrays.asList("-prefix <prefix>"), false, Arrays.asList(Permission.MANAGE_SERVER));
    }

    public void onCommand(MessageEvent context) throws Exception {
        if(context.getParameters().length() < 1 || context.getParameters().length() > 5) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Parameter").setDescription("Input must have a minimum length of `1` and a maximum length of `5` characters.");
            MessageDispatcher.reply(context, embed.build());
            return;
        }

        if(GuildFunctions.setGuildSettings("prefix", context.getParameters(), context.getGuild().getId())) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Prefix Changed").setDescription("The prefix used for this guild has been set to `" + context.getParameters() + "`");
            MessageDispatcher.reply(context, embed.build());
        }
    }
}
