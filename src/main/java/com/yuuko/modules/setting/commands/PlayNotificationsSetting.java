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

public class PlayNotificationsSetting extends Command {

    public PlayNotificationsSetting() {
        super("playnotifications", 0, -1L, Arrays.asList("-playnotifications", "-playnotifications <boolean>"), false, Arrays.asList(Permission.MANAGE_SERVER));
    }

    public void onCommand(MessageEvent context) throws Exception {
        if(!context.hasParameters()) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Play Notifications").setDescription("The `playnotifications` setting determines whether tracks are announced when they start.")
                    .addField("State", "`playnotifications` is currently set to `" + (GuildFunctions.getGuildSetting("playnotifications", context.getGuild().getId()).equals("1") ? "true" : "false") + "`", false)
                    .addField("Help", "Use `" + context.getPrefix() + "help " + context.getCommand().getName() + "` to get information on how to use this command.", false);
            MessageDispatcher.reply(context, embed.build());
            return;
        }

        String boolIntValue = Sanitiser.isBooleanTrue(context.getParameters()) ? "1" : "0";
        if(GuildFunctions.setGuildSettings("playnotifications", boolIntValue, context.getGuild().getId())) {
            if(Sanitiser.isBooleanTrue(context.getParameters())) {
                EmbedBuilder embed = new EmbedBuilder().setColor(Color.GREEN).setTitle("`playnotifications` set to `true`.");
                MessageDispatcher.reply(context, embed.build());
            } else {
                EmbedBuilder embed = new EmbedBuilder().setColor(Color.RED).setTitle("`playnotifications` set to `false`.");
                MessageDispatcher.reply(context, embed.build());
            }
        }
    }
}
