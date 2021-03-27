package com.yuuko.events.controllers;

import com.yuuko.MessageDispatcher;
import com.yuuko.Yuuko;
import com.yuuko.database.function.GuildFunctions;
import com.yuuko.metrics.MetricsManager;
import com.yuuko.modules.audio.handlers.AudioManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.GenericGuildEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateIconEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateNameEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateRegionEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateSplashEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericGuildController {
    private static final Logger log = LoggerFactory.getLogger(GenericGuildController.class);

    public GenericGuildController(GenericGuildEvent e) {
        if(e instanceof GuildJoinEvent) {
            guildJoinEvent((GuildJoinEvent) e);
            return;
        }

        if(e instanceof GuildLeaveEvent) {
            guildLeaveEvent((GuildLeaveEvent)e);
            return;
        }

        if(e instanceof GuildUpdateNameEvent) {
            guildUpdateNameEvent((GuildUpdateNameEvent) e);
            return;
        }

        if(e instanceof GuildUpdateRegionEvent) {
            guildUpdateRegionEvent((GuildUpdateRegionEvent) e);
            return;
        }

        if(e instanceof GuildUpdateIconEvent) {
            guildUpdateIconEvent((GuildUpdateIconEvent) e);
            return;
        }

        if(e instanceof GuildUpdateSplashEvent) {
            guildUpdateSplashEvent((GuildUpdateSplashEvent) e);
        }
    }

    private void guildJoinEvent(GuildJoinEvent e) {
        GuildFunctions.addOrUpdateGuild(e.getGuild());
        MetricsManager.getDiscordMetrics(e.getJDA().getShardInfo().getShardId()).update();

        // possible that a guild has no text channels
        if(e.getGuild().getDefaultChannel() != null) {
            Member bot = e.getGuild().getSelfMember();

            String embedPermission = "It appears that I already have the `MESSAGE_EMEBED_LINKS` permission... thanks!";
            if(!bot.hasPermission(Permission.MESSAGE_EMBED_LINKS) && !bot.hasPermission(e.getGuild().getDefaultChannel(), Permission.MESSAGE_EMBED_LINKS)) {
                embedPermission = "It appears that I haven't been given the `MESSAGE_EMEBED_LINKS` permission globally or in this channel...";
            } else if(bot.hasPermission(Permission.MESSAGE_EMBED_LINKS) && !bot.hasPermission(e.getGuild().getDefaultChannel(), Permission.MESSAGE_EMBED_LINKS)) {
                embedPermission = "It appears that I haven't been given the `MESSAGE_EMEBED_LINKS` permission in this channel, it would be great if I could get it...";
            } else if(!bot.hasPermission(Permission.MESSAGE_EMBED_LINKS) && bot.hasPermission(e.getGuild().getDefaultChannel(), Permission.MESSAGE_EMBED_LINKS)) {
                embedPermission = "It appears that although I have the `MESSAGE_EMEBED_LINKS` permission in this channel I don't have it globally... it would be great if I could get it!";
            }

            MessageDispatcher.sendMessage(e, e.getGuild().getDefaultChannel(),
                    """
                            **Automatic setup complete!**\s
                            _A few things:_\s
                            \t * %s Lots of my commands depend on it! :)\s
                            \t * For information about me use `-about`, for a list of commands use `-help`, and for a list of settings use `-settings`.\s
                            \t * If you need any other assistance, please do not hesitate to join the support server: %s
                            """.formatted(embedPermission, Yuuko.SUPPORT_GUILD)
            );
        }
    }

    private void guildLeaveEvent(GuildLeaveEvent e) {
        AudioManager.destroyGuildAudioManager(e.getGuild());
        GuildFunctions.cleanup(e.getGuild().getId());
        MetricsManager.getDiscordMetrics(e.getJDA().getShardInfo().getShardId()).update();
    }

    private void guildUpdateNameEvent(GuildUpdateNameEvent e) {
        GuildFunctions.updateGuildName(e.getGuild().getId(), e.getNewName());
    }

    private void guildUpdateRegionEvent(GuildUpdateRegionEvent e) {
        GuildFunctions.updateGuildRegion(e.getGuild().getId(), e.getNewRegion().getName());
    }

    private void guildUpdateIconEvent(GuildUpdateIconEvent e) {
        GuildFunctions.updateGuildIcon(e.getGuild().getId(), e.getNewIconUrl());
    }

    private void guildUpdateSplashEvent(GuildUpdateSplashEvent e) {
        GuildFunctions.updateGuildSplash(e.getGuild().getId(), e.getNewSplashUrl());
    }
}
