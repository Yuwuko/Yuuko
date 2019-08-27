package com.yuuko.core.events.controllers;

import com.yuuko.core.Configuration;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.audio.handlers.AudioManagerController;
import com.yuuko.core.database.function.GuildFunctions;
import com.yuuko.core.metrics.MetricsManager;
import com.yuuko.core.metrics.pathway.EventMetrics;
import com.yuuko.core.utilities.TextUtilities;
import com.yuuko.core.utilities.Utilities;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.GenericGuildEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateIconEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateNameEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateRegionEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateSplashEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericGuildController {
    private static final Logger log = LoggerFactory.getLogger(GenericGuildController.class);
    private static final EventMetrics metrics = MetricsManager.getEventMetrics();

    public GenericGuildController(GenericGuildEvent e) {
        if(e instanceof GuildMemberJoinEvent) {
            guildMemberJoinEvent((GuildMemberJoinEvent)e);
            metrics.GUILD_MEMBER_JOIN_EVENT.getAndIncrement();
            return;
        }

        if(e instanceof GuildMemberLeaveEvent) {
            guildMemberLeaveEvent((GuildMemberLeaveEvent)e);
            metrics.GUILD_MEMBER_LEAVE_EVENT.getAndIncrement();
            return;
        }

        if(e instanceof GuildJoinEvent) {
            guildJoinEvent((GuildJoinEvent) e);
            metrics.GUILD_JOIN_EVENT.getAndIncrement();
            return;
        }

        if(e instanceof GuildLeaveEvent) {
            guildLeaveEvent((GuildLeaveEvent)e);
            metrics.GUILD_LEAVE_EVENT.getAndIncrement();
            return;
        }

        if(e instanceof GuildUpdateNameEvent) {
            guildUpdateNameEvent((GuildUpdateNameEvent) e);
            metrics.GUILD_UPDATE_NAME_EVENT.getAndIncrement();
            return;
        }

        if(e instanceof GuildUpdateRegionEvent) {
            guildUpdateRegionEvent((GuildUpdateRegionEvent) e);
            metrics.GUILD_UPDATE_REGION_EVENT.getAndIncrement();
            return;
        }

        if(e instanceof GuildUpdateIconEvent) {
            guildUpdateIconEvent((GuildUpdateIconEvent) e);
            metrics.GUILD_UPDATE_ICON_EVENT.getAndIncrement();
            return;
        }

        if(e instanceof GuildUpdateSplashEvent) {
            guildUpdateSplashEvent((GuildUpdateSplashEvent) e);
            metrics.GUILD_UPDATE_SPLASH_EVENT.getAndIncrement();
        }
    }

    private void guildJoinEvent(GuildJoinEvent e) {
        GuildFunctions.addGuild(e.getGuild());
        MetricsManager.getDiscordMetrics().update();
        Utilities.updateDiscordBotList();

        try {
            e.getGuild().getTextChannels().stream().filter(textChannel -> textChannel.getName().toLowerCase().contains("general")).findFirst().ifPresent(textChannel -> {
                Member bot = e.getGuild().getSelfMember();

                Permission[] messagePermissions = new Permission[]{Permission.MESSAGE_READ, Permission.MESSAGE_WRITE, Permission.MESSAGE_EMBED_LINKS};
                if(bot.hasPermission(messagePermissions) && bot.hasPermission(textChannel, messagePermissions)) {
                    EmbedBuilder about = new EmbedBuilder()
                            .setAuthor(Configuration.BOT.getAsTag(), null, Configuration.BOT.getAvatarUrl())
                            .setDescription("Automatic setup successful, use `-help` to see a full list of commands, or `=about` to get some general information about me.");
                    MessageHandler.sendMessage(e, textChannel, about.build());
                }
            });
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
        }
    }

    private void guildLeaveEvent(GuildLeaveEvent e) {
        AudioManagerController.getGuildAudioManager(e.getGuild()).destroy();
        GuildFunctions.cleanup(e.getGuild().getId());
        MetricsManager.getDiscordMetrics().update();
        Utilities.updateDiscordBotList();
    }

    private void guildUpdateNameEvent(GuildUpdateNameEvent e) {
        GuildFunctions.updateGuildName(e.getGuild().getId(), e.getNewName());
    }

    private void guildUpdateRegionEvent(GuildUpdateRegionEvent e) {
        GuildFunctions.updateGuildRegion(e.getGuild().getId(), e.getNewRegion().getName());
    }

    private void guildMemberJoinEvent(GuildMemberJoinEvent e) {
        GuildFunctions.updateGuildMembers(e.getGuild().getId(), e.getGuild().getMemberCache().size());

        final String channelId = GuildFunctions.getGuildSetting("newMember", e.getGuild().getId());
        if(channelId == null) {
            return;
        }

        String message = GuildFunctions.getGuildSetting("newMemberMessage", e.getGuild().getId());
        if(message == null) {
            message = "Welcome to **%guild%**, %user%!";
        }

        TextChannel channel = e.getGuild().getTextChannelById(channelId);
        EmbedBuilder member = new EmbedBuilder().setTitle("New Member").setDescription(TextUtilities.untokenizeString(e, message));
        if(GuildFunctions.getGuildSettingBoolean("newMemberRemoveMessage", e.getGuild().getId())) {
            MessageHandler.sendTempMessage(e, channel, member.build());
        } else {
            MessageHandler.sendMessage(e, channel, member.build());
        }
    }

    private void guildMemberLeaveEvent(GuildMemberLeaveEvent e) {
        GuildFunctions.updateGuildMembers(e.getGuild().getId(), e.getGuild().getMemberCache().size());
    }

    private void guildUpdateIconEvent(GuildUpdateIconEvent e) {
        GuildFunctions.updateGuildIcon(e.getGuild().getId(), e.getNewIconUrl());
    }

    private void guildUpdateSplashEvent(GuildUpdateSplashEvent e) {
        GuildFunctions.updateGuildSplash(e.getGuild().getId(), e.getNewSplashUrl());
    }
}
