package com.yuuko.events.controllers;

import com.yuuko.MessageDispatcher;
import com.yuuko.Yuuko;
import com.yuuko.commands.audio.handlers.AudioManager;
import com.yuuko.database.function.GuildFunctions;
import com.yuuko.metrics.MetricsManager;
import com.yuuko.utilities.Utilities;
import net.dv8tion.jda.api.EmbedBuilder;
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
        Utilities.updateDiscordBotList(e.getJDA().getShardInfo().getShardId());

        try {
            e.getGuild().getTextChannels().stream().filter(textChannel -> textChannel.getName().toLowerCase().contains("general")).findFirst().ifPresent(textChannel -> {
                Member bot = e.getGuild().getSelfMember();

                Permission[] messagePermissions = new Permission[]{Permission.MESSAGE_READ, Permission.MESSAGE_WRITE, Permission.MESSAGE_EMBED_LINKS};
                if(bot.hasPermission(messagePermissions) && bot.hasPermission(textChannel, messagePermissions)) {
                    EmbedBuilder about = new EmbedBuilder()
                            .setAuthor(Yuuko.BOT.getAsTag(), null, Yuuko.BOT.getAvatarUrl())
                            .setDescription("Automatic setup successful, use `-help` to see a full list of commands, `-settings` to see available settings or `-about` to get some general information about me.");
                    MessageDispatcher.sendMessage(e, textChannel, about.build());
                }

            });

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
        }
    }

    private void guildLeaveEvent(GuildLeaveEvent e) {
        AudioManager.destroyGuildAudioManager(e.getGuild());
        GuildFunctions.cleanup(e.getGuild().getId());
        MetricsManager.getDiscordMetrics(e.getJDA().getShardInfo().getShardId()).update();
        Utilities.updateDiscordBotList(e.getJDA().getShardInfo().getShardId());
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
