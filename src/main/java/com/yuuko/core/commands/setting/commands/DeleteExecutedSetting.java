package com.yuuko.core.commands.setting.commands;

import com.yuuko.core.Config;
import com.yuuko.core.MessageDispatcher;
import com.yuuko.core.commands.Command;
import com.yuuko.core.database.function.GuildFunctions;
import com.yuuko.core.events.entity.MessageEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class DeleteExecutedSetting extends Command {
    private static final List<String> booleanValues = Arrays.asList(
            "true",
            "yes",
            "false",
            "no"
    );

    public DeleteExecutedSetting() {
        super("delexecuted", Config.MODULES.get("setting"), 0, -1L, Arrays.asList("-deleteexecuted", "-deleteexecuted <value>"), false, Arrays.asList(Permission.MANAGE_SERVER));
    }

    public void onCommand(MessageEvent e) throws Exception {
        if(!e.hasParameters()) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Delete Executed").setDescription("The `deleteExecuted` setting determines whether user input for commands are deleted if the command is successfully executed.")
                    .addField("State", "`deleteExecuted` is currently set to `" + (GuildFunctions.getGuildSetting("deleteexecuted", e.getGuild().getId()).equals("1") ? "TRUE" : "FALSE") + "`", true)
                    .addField("Help", "Use `" + e.getPrefix() + "help " + e.getCommand().getName() + "` to get information on how to use this command.", true);
            MessageDispatcher.reply(e, embed.build());
            return;
        }

        if(!booleanValues.contains(e.getParameters())) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("_" + e.getParameters().toUpperCase() + "_ is not valid. (Valid: TRUE, FALSE)");
            MessageDispatcher.reply(e, embed.build());
            return;
        }

        String intValue = (e.getParameters().equals("true") || e.getParameters().equals("yes")) ? "1" : "0";
        if(GuildFunctions.setGuildSettings("deleteexecuted", intValue, e.getGuild().getId())) {
            if(Boolean.parseBoolean(e.getParameters().toUpperCase())) {
                EmbedBuilder embed = new EmbedBuilder().setColor(Color.GREEN).setTitle("`deleteExecuted` set to `TRUE`.");
                MessageDispatcher.reply(e, embed.build());
            } else {
                EmbedBuilder embed = new EmbedBuilder().setColor(Color.RED).setTitle("`deleteExecuted` set to `FALSE`.");
                MessageDispatcher.reply(e, embed.build());
            }
        }
    }
}
