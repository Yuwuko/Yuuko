package com.yuuko.core.commands.setting.commands;

import com.yuuko.core.Configuration;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.database.function.GuildFunctions;
import com.yuuko.core.events.entity.MessageEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;

import java.util.Arrays;
import java.util.List;

public class NowPlayingSetting extends Command {
    private static final List<String> booleanValues = Arrays.asList(
            "true",
            "yes",
            "false",
            "no"
    );

    public NowPlayingSetting() {
        super("nowplaying", Configuration.MODULES.get("setting"), 0, Arrays.asList("-nowplaying", "-nowplaying <value>"), false, Arrays.asList(Permission.MANAGE_SERVER));
    }

    public void onCommand(MessageEvent e) {
        try {
            if(!e.hasParameters()) {
                String status = "The `NowPlaying` setting determines whether tracks are announced when they start.";

                EmbedBuilder embed = new EmbedBuilder().setTitle("Now Playing").setDescription(status)
                        .addField("State", "Audio track `NowPlaying` is currently set to " + GuildFunctions.getGuildSetting("nowplaying", e.getGuild().getId()) + "", true)
                        .addField("Help", "Use `" + e.getPrefix() + "help " + e.getCommand().getName() + "` to get information on how to use this command.", true);
                MessageHandler.sendMessage(e, embed.build());
                return;
            }

            if(!booleanValues.contains(e.getParameters())) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("_" + e.getParameters().toUpperCase() + "_ is not valid. (Valid: TRUE, FALSE)");
                MessageHandler.sendMessage(e, embed.build());
                return;
            }

            String intValue = (e.getParameters().equals("true") || e.getParameters().equals("yes")) ? "1" : "0";

            if(GuildFunctions.setGuildSettings("nowplaying", intValue, e.getGuild().getId())) {
                if(Boolean.parseBoolean(e.getParameters().toUpperCase())) {
                    EmbedBuilder embed = new EmbedBuilder().setTitle("NowPlaying set to TRUE.");
                    MessageHandler.sendMessage(e, embed.build());
                } else {
                    EmbedBuilder embed = new EmbedBuilder().setTitle("NowPlaying set to FALSE.");
                    MessageHandler.sendMessage(e, embed.build());
                }
            }
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
        }
    }
}
