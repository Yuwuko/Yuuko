package com.yuuko.commands.setting.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.Yuuko;
import com.yuuko.commands.Command;
import com.yuuko.database.function.GuildFunctions;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.utilities.Sanitiser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;

import java.awt.*;
import java.util.Arrays;

public class CleanupCommandsSetting extends Command {

    public CleanupCommandsSetting() {
        super("cleanupcommands", Yuuko.MODULES.get("setting"), 0, -1L, Arrays.asList("-cleanupcommands", "-cleanupcommands <value>"), false, Arrays.asList(Permission.MANAGE_SERVER));
    }

    public void onCommand(MessageEvent e) throws Exception {
        if(!e.hasParameters()) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Cleanup Commands").setDescription("The `cleanupcommands` setting determines whether user input for commands are deleted if the command is successfully executed.")
                    .addField("State", "`cleanupcommands` is currently set to `" + (GuildFunctions.getGuildSetting("cleanupcommands", e.getGuild().getId()).equals("1") ? "true" : "false") + "`", false)
                    .addField("Help", "Use `" + e.getPrefix() + "help " + e.getCommand().getName() + "` to get information on how to use this command.", false);
            MessageDispatcher.reply(e, embed.build());
            return;
        }

        String boolIntValue = (Sanitiser.isBooleanTrue(e.getParameters())) ? "1" : "0";
        if(GuildFunctions.setGuildSettings("cleanupcommands", boolIntValue, e.getGuild().getId())) {
            if(Sanitiser.isBooleanTrue(e.getParameters())) {
                EmbedBuilder embed = new EmbedBuilder().setColor(Color.GREEN).setTitle("`cleanupcommands` set to `true`.");
                MessageDispatcher.reply(e, embed.build());
            } else {
                EmbedBuilder embed = new EmbedBuilder().setColor(Color.RED).setTitle("`cleanupcommands` set to `false`.");
                MessageDispatcher.reply(e, embed.build());
            }
        }
    }
}
