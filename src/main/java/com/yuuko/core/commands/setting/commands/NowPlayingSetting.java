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

public class NowPlayingSetting extends Command {

    public NowPlayingSetting() {
        super("nowplaying", Yuuko.MODULES.get("setting"), 0, -1L, Arrays.asList("-nowplaying", "-nowplaying <boolean>"), false, Arrays.asList(Permission.MANAGE_SERVER));
    }

    public void onCommand(MessageEvent e) throws Exception {
        if(!e.hasParameters()) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Now Playing").setDescription("The `nowPlaying` setting determines whether tracks are announced when they start.")
                    .addField("State", "`nowplaying` is currently set to `" + (GuildFunctions.getGuildSetting("nowplaying", e.getGuild().getId()).equals("1") ? "true" : "false") + "`", false)
                    .addField("Help", "Use `" + e.getPrefix() + "help " + e.getCommand().getName() + "` to get information on how to use this command.", false);
            MessageDispatcher.reply(e, embed.build());
            return;
        }

        String boolIntValue = Sanitiser.isBooleanTrue(e.getParameters()) ? "1" : "0";
        if(GuildFunctions.setGuildSettings("nowplaying", boolIntValue, e.getGuild().getId())) {
            if(Sanitiser.isBooleanTrue(e.getParameters())) {
                EmbedBuilder embed = new EmbedBuilder().setColor(Color.GREEN).setTitle("`nowplaying` set to `true`.");
                MessageDispatcher.reply(e, embed.build());
            } else {
                EmbedBuilder embed = new EmbedBuilder().setColor(Color.RED).setTitle("`nowplaying` set to `false`.");
                MessageDispatcher.reply(e, embed.build());
            }
        }
    }
}
