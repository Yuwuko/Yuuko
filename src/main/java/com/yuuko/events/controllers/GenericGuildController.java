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

    public GenericGuildController(GenericGuildEvent event) {
        if(event instanceof GuildJoinEvent) {
            guildJoinEvent((GuildJoinEvent) event);
            return;
        }

        if(event instanceof GuildLeaveEvent) {
            guildLeaveEvent((GuildLeaveEvent)event);
            return;
        }

        if(event instanceof GuildUpdateNameEvent) {
            guildUpdateNameEvent((GuildUpdateNameEvent) event);
            return;
        }

        if(event instanceof GuildUpdateRegionEvent) {
            guildUpdateRegionEvent((GuildUpdateRegionEvent) event);
            return;
        }

        if(event instanceof GuildUpdateIconEvent) {
            guildUpdateIconEvent((GuildUpdateIconEvent) event);
            return;
        }

        if(event instanceof GuildUpdateSplashEvent) {
            guildUpdateSplashEvent((GuildUpdateSplashEvent) event);
        }
    }

    private void guildJoinEvent(GuildJoinEvent event) {
        GuildFunctions.addGuild(event.getGuild());
        MetricsManager.getDiscordMetrics(event.getJDA().getShardInfo().getShardId()).update();

        // possible that a guild has no text channels
        if(event.getGuild().getDefaultChannel() != null) {
            Member bot = event.getGuild().getSelfMember();

            String embedPermission = "It appears that I already have the `MESSAGE_EMBED_LINKS` permission... thanks!";
            if(!bot.hasPermission(Permission.MESSAGE_EMBED_LINKS) && !bot.hasPermission(event.getGuild().getDefaultChannel(), Permission.MESSAGE_EMBED_LINKS)) {
                embedPermission = "It appears that I haven't been given the `MESSAGE_EMBED_LINKS` permission globally or in this channel...";
            } else if(bot.hasPermission(Permission.MESSAGE_EMBED_LINKS) && !bot.hasPermission(event.getGuild().getDefaultChannel(), Permission.MESSAGE_EMBED_LINKS)) {
                embedPermission = "It appears that I haven't been given the `MESSAGE_EMBED_LINKS` permission in this channel, it would be great if I could get it...";
            } else if(!bot.hasPermission(Permission.MESSAGE_EMBED_LINKS) && bot.hasPermission(event.getGuild().getDefaultChannel(), Permission.MESSAGE_EMBED_LINKS)) {
                embedPermission = "It appears that although I have the `MESSAGE_EMBED_LINKS` permission in this channel I don't have it globally... it would be great if I could get it!";
            }

            MessageDispatcher.sendMessage(event, event.getGuild().getDefaultChannel(),
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

    private void guildLeaveEvent(GuildLeaveEvent event) {
        AudioManager.destroyGuildAudioManager(event.getGuild());
        GuildFunctions.removeGuild(event.getGuild());
        MetricsManager.getDiscordMetrics(event.getJDA().getShardInfo().getShardId()).update();
    }

    private void guildUpdateNameEvent(GuildUpdateNameEvent event) {
        GuildFunctions.updateGuildName(event.getGuild().getId(), event.getNewName());
    }

    private void guildUpdateRegionEvent(GuildUpdateRegionEvent event) {
        GuildFunctions.updateGuildRegion(event.getGuild().getId(), event.getNewRegion().getName());
    }

    private void guildUpdateIconEvent(GuildUpdateIconEvent event) {
        GuildFunctions.updateGuildIcon(event.getGuild().getId(), event.getNewIconUrl());
    }

    private void guildUpdateSplashEvent(GuildUpdateSplashEvent event) {
        GuildFunctions.updateGuildSplash(event.getGuild().getId(), event.getNewSplashUrl());
    }
}
