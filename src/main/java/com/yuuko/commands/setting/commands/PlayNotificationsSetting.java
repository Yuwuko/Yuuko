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

public class PlayNotificationsSetting extends Command {

    public PlayNotificationsSetting() {
        super("playnotifications", Yuuko.MODULES.get("setting"), 0, -1L, Arrays.asList("-playnotifications", "-playnotifications <boolean>"), false, Arrays.asList(Permission.MANAGE_SERVER));
    }

    public void onCommand(MessageEvent e) throws Exception {
        if(!e.hasParameters()) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Play Notifications").setDescription("The `playnotifications` setting determines whether tracks are announced when they start.")
                    .addField("State", "`playnotifications` is currently set to `" + (GuildFunctions.getGuildSetting("playnotifications", e.getGuild().getId()).equals("1") ? "true" : "false") + "`", false)
                    .addField("Help", "Use `" + e.getPrefix() + "help " + e.getCommand().getName() + "` to get information on how to use this command.", false);
            MessageDispatcher.reply(e, embed.build());
            return;
        }

        String boolIntValue = Sanitiser.isBooleanTrue(e.getParameters()) ? "1" : "0";
        if(GuildFunctions.setGuildSettings("playnotifications", boolIntValue, e.getGuild().getId())) {
            if(Sanitiser.isBooleanTrue(e.getParameters())) {
                EmbedBuilder embed = new EmbedBuilder().setColor(Color.GREEN).setTitle("`playnotifications` set to `true`.");
                MessageDispatcher.reply(e, embed.build());
            } else {
                EmbedBuilder embed = new EmbedBuilder().setColor(Color.RED).setTitle("`playnotifications` set to `false`.");
                MessageDispatcher.reply(e, embed.build());
            }
        }
    }
}
