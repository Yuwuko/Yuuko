package com.yuuko.core.commands.setting.commands;

import com.yuuko.core.MessageDispatcher;
import com.yuuko.core.Yuuko;
import com.yuuko.core.commands.Command;
import com.yuuko.core.database.function.GuildFunctions;
import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.utilities.Sanitiser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;

import java.awt.*;
import java.util.Arrays;

public class DeleteExecutedSetting extends Command {

    public DeleteExecutedSetting() {
        super("delexecuted", Yuuko.MODULES.get("setting"), 0, -1L, Arrays.asList("-deleteexecuted", "-deleteexecuted <value>"), false, Arrays.asList(Permission.MANAGE_SERVER));
    }

    public void onCommand(MessageEvent e) throws Exception {
        if(!e.hasParameters()) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Delete Executed").setDescription("The `deleteExecuted` setting determines whether user input for commands are deleted if the command is successfully executed.")
                    .addField("State", "`deleteexecuted` is currently set to `" + (GuildFunctions.getGuildSetting("deleteexecuted", e.getGuild().getId()).equals("1") ? "true" : "false") + "`", true)
                    .addField("Help", "Use `" + e.getPrefix() + "help " + e.getCommand().getName() + "` to get information on how to use this command.", true);
            MessageDispatcher.reply(e, embed.build());
            return;
        }

        String boolIntValue = (Sanitiser.isBooleanTrue(e.getParameters())) ? "1" : "0";
        if(GuildFunctions.setGuildSettings("deleteexecuted", boolIntValue, e.getGuild().getId())) {
            if(Sanitiser.isBooleanTrue(e.getParameters())) {
                EmbedBuilder embed = new EmbedBuilder().setColor(Color.GREEN).setTitle("`deleteexecuted` set to `true`.");
                MessageDispatcher.reply(e, embed.build());
            } else {
                EmbedBuilder embed = new EmbedBuilder().setColor(Color.RED).setTitle("`deleteexecuted` set to `false`.");
                MessageDispatcher.reply(e, embed.build());
            }
        }
    }
}
