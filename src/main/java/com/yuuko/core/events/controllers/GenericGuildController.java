package com.yuuko.core.events.controllers;

import com.yuuko.core.Configuration;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.database.function.GuildFunctions;
import com.yuuko.core.metrics.MetricsManager;
import com.yuuko.core.utilities.TextUtilities;
import com.yuuko.core.utilities.Utilities;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.guild.GenericGuildEvent;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.guild.update.GuildUpdateIconEvent;
import net.dv8tion.jda.core.events.guild.update.GuildUpdateNameEvent;
import net.dv8tion.jda.core.events.guild.update.GuildUpdateRegionEvent;
import net.dv8tion.jda.core.events.guild.update.GuildUpdateSplashEvent;
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
            return;
        }

        if(e instanceof GuildMemberJoinEvent) {
            guildMemberJoinEvent((GuildMemberJoinEvent)e);
            return;
        }

        if(e instanceof GuildMemberLeaveEvent) {
            guildMemberLeaveEvent((GuildMemberLeaveEvent)e);
        }
    }

    private void guildJoinEvent(GuildJoinEvent e) {
        GuildFunctions.addGuild(e.getGuild());

        try {
            if(e.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_READ, Permission.MESSAGE_WRITE, Permission.MESSAGE_EMBED_LINKS)) {
                e.getGuild().getTextChannels().stream().filter(textChannel -> textChannel.getName().toLowerCase().contains("general")).findFirst().ifPresent(textChannel -> {
                    EmbedBuilder about = new EmbedBuilder()
                            .setAuthor(Configuration.BOT.getName() + "#" + Configuration.BOT.getDiscriminator(), null, Configuration.BOT.getAvatarUrl())
                            .setDescription("Automatic setup was successful! Thanks for inviting me to your guild, below is some information about myself. Commands can be found at https://www.yuuko.info or by using the **-help** command! If you have any problems, suggestions, or general feedback, please join the support guild at " + Configuration.SUPPORT_GUILD + " and drop me a line!")
                            .setThumbnail(Configuration.BOT.getAvatarUrl())
                            .addField("Author", "[" + Configuration.AUTHOR + "](" + Configuration.AUTHOR_WEBSITE + ")", true)
                            .addField("Version", Configuration.VERSION, true)
                            .addField("Guilds", MetricsManager.getDiscordMetrics().GUILD_COUNT + "", true)
                            .addField("Commands", Configuration.COMMANDS.size() + "", true)
                            .addField("Prefix", Configuration.GLOBAL_PREFIX + ", `" + Utilities.getServerPrefix(e.getGuild()) + "`", true)
                            .addField("Uptime", TextUtilities.getTimestamp(MetricsManager.getSystemMetrics().UPTIME), true)
                            .addField("Ping", MetricsManager.getDiscordMetrics().PING + "", true);
                    MessageHandler.sendMessage(e, textChannel, about.build());
                });
            }
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
        }

        MetricsManager.getDiscordMetrics().update();
        Utilities.updateDiscordBotList();
    }

    private void guildLeaveEvent(GuildLeaveEvent e) {
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
        String channelId = GuildFunctions.getGuildSetting("newMember", e.getGuild().getId());
        if(channelId != null) {
            String message = GuildFunctions.getGuildSetting("newMemberMessage", e.getGuild().getId());
            if(message == null) {
                TextChannel channel = e.getGuild().getTextChannelById(channelId);
                EmbedBuilder member = new EmbedBuilder().setTitle("New Member").setDescription(TextUtilities.untokenizeString(e, "Welcome to **%guild%**, %user%!"));
                MessageHandler.sendMessage(e, channel, member.build());
            } else {
                TextChannel channel = e.getGuild().getTextChannelById(channelId);
                EmbedBuilder member = new EmbedBuilder().setTitle("New Member").setDescription(TextUtilities.untokenizeString(e, message));
                MessageHandler.sendMessage(e, channel, member.build());
            }
        }

        GuildFunctions.updateMemberCount(e.getGuild().getId(), e.getGuild().getMemberCache().size());
    }

    private void guildMemberLeaveEvent(GuildMemberLeaveEvent e) {
        GuildFunctions.updateMemberCount(e.getGuild().getId(), e.getGuild().getMemberCache().size());
    }

    private void guildUpdateIconEvent(GuildUpdateIconEvent e) {
        GuildFunctions.updateGuildIcon(e.getGuild().getId(), e.getNewIconUrl());
    }

    private void guildUpdateSplashEvent(GuildUpdateSplashEvent e) {
        GuildFunctions.updateGuildSplash(e.getGuild().getId(), e.getNewSplashUrl());
    }
}
