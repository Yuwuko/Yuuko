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
        super("cleanupcommands", 0, -1L, Arrays.asList("-cleanupcommands", "-cleanupcommands <value>"), false, Arrays.asList(Permission.MANAGE_SERVER));
    }

    public void onCommand(MessageEvent context) throws Exception {
        if(!context.hasParameters()) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Cleanup Commands").setDescription("The `cleanupcommands` setting determines whether user input for commands are deleted if the command is successfully executed.")
                    .addField("State", "`cleanupcommands` is currently set to `" + (GuildFunctions.getGuildSetting("cleanupcommands", context.getGuild().getId()).equals("1") ? "true" : "false") + "`", false)
                    .addField("Help", "Use `" + context.getPrefix() + "help " + context.getCommand().getName() + "` to get information on how to use this command.", false);
            MessageDispatcher.reply(context, embed.build());
            return;
        }

        String boolIntValue = (Sanitiser.isBooleanTrue(context.getParameters())) ? "1" : "0";
        if(GuildFunctions.setGuildSettings("cleanupcommands", boolIntValue, context.getGuild().getId())) {
            if(Sanitiser.isBooleanTrue(context.getParameters())) {
                EmbedBuilder embed = new EmbedBuilder().setColor(Color.GREEN).setTitle("`cleanupcommands` set to `true`.");
                MessageDispatcher.reply(context, embed.build());
            } else {
                EmbedBuilder embed = new EmbedBuilder().setColor(Color.RED).setTitle("`cleanupcommands` set to `false`.");
                MessageDispatcher.reply(context, embed.build());
            }
        }
    }
}
