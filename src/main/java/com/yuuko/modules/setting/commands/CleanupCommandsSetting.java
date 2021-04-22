package com.yuuko.modules.setting.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.database.function.GuildFunctions;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.modules.Command;
import com.yuuko.utilities.Sanitiser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;

import java.awt.*;
import java.util.Arrays;

public class CleanupCommandsSetting extends Command {

    public CleanupCommandsSetting() {
        super("cleanupcommands", Arrays.asList("-cleanupcommands", "-cleanupcommands <value>"), Arrays.asList(Permission.MANAGE_SERVER));
    }

    public void onCommand(MessageEvent context) throws Exception {
        if(!context.hasParameters()) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(context.i18n("title"))
                    .setDescription(context.i18n("desc"))
                    .addField(context.i18n("state"), context.i18n("state_desc").formatted(GuildFunctions.getGuildSettingBoolean("cleanupcommands", context.getGuild().getId())), false)
                    .addField(context.i18n("help"), context.i18n("help_desc").formatted(context.getPrefix(), context.getCommand().getName()), false);
            MessageDispatcher.reply(context, embed.build());
            return;
        }

        String boolIntValue = (Sanitiser.isBooleanTrue(context.getParameters())) ? "1" : "0";
        if(GuildFunctions.setGuildSettings("cleanupcommands", boolIntValue, context.getGuild().getId())) {
            if(Sanitiser.isBooleanTrue(context.getParameters())) {
                EmbedBuilder embed = new EmbedBuilder()
                        .setColor(Color.GREEN)
                        .setTitle("`cleanupcommands` => `true`");
                MessageDispatcher.reply(context, embed.build());
            } else {
                EmbedBuilder embed = new EmbedBuilder()
                        .setColor(Color.RED)
                        .setTitle("`cleanupcommands` => `false`");
                MessageDispatcher.reply(context, embed.build());
            }
        }
    }
}
