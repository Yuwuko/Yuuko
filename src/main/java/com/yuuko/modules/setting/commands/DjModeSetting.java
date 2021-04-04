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
import java.util.List;

public class DjModeSetting extends Command {
    private static final List<String> booleanValues = Arrays.asList(
            "true",
            "yes",
            "false",
            "no"
    );

    public DjModeSetting() {
        super("djmode", 0, -1L, Arrays.asList("-djmode", "-djmode <value>"), false, Arrays.asList(Permission.MANAGE_SERVER));
    }

    public void onCommand(MessageEvent context) throws Exception {
        if(!context.hasParameters()) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(context.i18n("title"))
                    .setDescription(context.i18n("desc"))
                    .addField(context.i18n("state"), context.i18n("state_desc").formatted(GuildFunctions.getGuildSettingBoolean("djmode", context.getGuild().getId())), true)
                    .addField(context.i18n("help"), context.i18n("help_desc").formatted(context.getPrefix(), context.getCommand().getName()), false);
            MessageDispatcher.reply(context, embed.build());
            return;
        }

        String intValue = (Sanitiser.isBooleanTrue(context.getParameters())) ? "1" : "0";
        if(GuildFunctions.setGuildSettings("djmode", intValue, context.getGuild().getId())) {
            if(Boolean.parseBoolean(context.getParameters().toUpperCase())) {
                EmbedBuilder embed = new EmbedBuilder().setColor(Color.GREEN).setTitle("`djMode` => `true`.");
                MessageDispatcher.reply(context, embed.build());
            } else {
                EmbedBuilder embed = new EmbedBuilder().setColor(Color.RED).setTitle("`djMode` => `false`.");
                MessageDispatcher.reply(context, embed.build());
            }
        }
    }
}
