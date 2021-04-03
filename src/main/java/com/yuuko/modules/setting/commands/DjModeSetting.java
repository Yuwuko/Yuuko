package com.yuuko.modules.setting.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.database.function.GuildFunctions;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.modules.Command;
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
            EmbedBuilder embed = new EmbedBuilder().setTitle("DJ Mode").setDescription("The `djMode` setting determines whether audio commands are locked to people who posses the `DJ` role.")
                    .addField("State", "DJ Mode is currently set to `" + (GuildFunctions.getGuildSetting("djmode", context.getGuild().getId()).equals("1") ? "TRUE" : "FALSE") + "`", true)
                    .addField("Help", "Use `" + context.getPrefix() + "help " + context.getCommand().getName() + "` to get information on how to use this command.", true);
            MessageDispatcher.reply(context, embed.build());
            return;
        }

        if(!booleanValues.contains(context.getParameters())) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("_" + context.getParameters().toUpperCase() + "_ is not valid. (Valid: TRUE, FALSE)");
            MessageDispatcher.reply(context, embed.build());
            return;
        }

        String intValue = (context.getParameters().equals("true") || context.getParameters().equals("yes")) ? "1" : "0";
        if(GuildFunctions.setGuildSettings("djmode", intValue, context.getGuild().getId())) {
            if(Boolean.parseBoolean(context.getParameters().toUpperCase())) {
                EmbedBuilder embed = new EmbedBuilder().setColor(Color.GREEN).setTitle("`djMode` set to `TRUE`.");
                MessageDispatcher.reply(context, embed.build());
            } else {
                EmbedBuilder embed = new EmbedBuilder().setColor(Color.RED).setTitle("`djMode` set to `FALSE`.");
                MessageDispatcher.reply(context, embed.build());
            }
        }
    }
}
