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
        super("prefix", Arrays.asList("-prefix <prefix>"), Arrays.asList(Permission.MANAGE_SERVER), 1);
    }

    public void onCommand(MessageEvent context) throws Exception {
        if(context.getParameters().length() < 1 || context.getParameters().length() > 5) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(context.i18n("invalid_param"))
                    .setDescription("invalid_param_desc");
            MessageDispatcher.reply(context, embed.build());
            return;
        }

        if(GuildFunctions.setGuildSettings("prefix", context.getParameters(), context.getGuild().getId())) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(context.i18n("prefix"))
                    .setDescription(context.i18n("prefix_desc").formatted(context.getParameters()));
            MessageDispatcher.reply(context, embed.build());
        }
    }
}
