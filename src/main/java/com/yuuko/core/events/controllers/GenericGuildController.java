package com.yuuko.core.events.controllers;

import com.yuuko.core.Cache;
import com.yuuko.core.Configuration;
import com.yuuko.core.commands.core.settings.ModerationLogSetting;
import com.yuuko.core.database.DatabaseFunctions;
import com.yuuko.core.metrics.handlers.MetricsManager;
import com.yuuko.core.utilities.MessageHandler;
import com.yuuko.core.utilities.TextUtility;
import com.yuuko.core.utilities.Utils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.guild.*;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.guild.update.GuildUpdateNameEvent;
import net.dv8tion.jda.core.events.guild.update.GuildUpdateRegionEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageUpdateEvent;

public class GenericGuildController {

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

        if(e instanceof GuildMemberJoinEvent) {
            guildMemberJoinEvent((GuildMemberJoinEvent)e);
            return;
        }

        if(e instanceof GuildMemberLeaveEvent) {
            guildMemberLeaveEvent((GuildMemberLeaveEvent)e);
            return;
        }

        if(e instanceof GuildBanEvent) {
            ModerationLogSetting.execute((GuildBanEvent) e);
            return;
        }

        if(e instanceof GuildUnbanEvent) {
            ModerationLogSetting.execute((GuildUnbanEvent) e);
            return;
        }

        if(e instanceof GuildMessageUpdateEvent) {
            ModerationLogSetting.execute((GuildMessageUpdateEvent) e);
            return;
        }

        if(e instanceof GuildMessageDeleteEvent) {
            ModerationLogSetting.execute((GuildMessageDeleteEvent) e);
        }
    }

    private void guildJoinEvent(GuildJoinEvent e) {
        DatabaseFunctions.addNewGuild(e.getGuild().getId(), e.getGuild().getName(), e.getGuild().getRegion().getName());

        try {
            e.getGuild().getTextChannels().stream().filter(textChannel -> textChannel.getName().toLowerCase().contains("general")).findFirst().ifPresent(textChannel -> {
                EmbedBuilder about = new EmbedBuilder()
                        .setAuthor(Cache.BOT.getName() + "#" + Cache.BOT.getDiscriminator(), null, Cache.BOT.getAvatarUrl())
                        .setDescription("Automatic setup was successful! Thanks for inviting me to your guild, below is some information about myself. Commands can be found at https://www.yuuko.info or by using the **-help** command! If you have any problems, suggestions, or general feedback, please join the support guild at " + Configuration.SUPPORT_GUILD + " and drop me a line!")
                        .setThumbnail(Cache.BOT.getAvatarUrl())
                        .addField("Author", "[" + Configuration.AUTHOR + "](" + Configuration.AUTHOR_WEBSITE + ")", true)
                        .addField("Version", Configuration.VERSION, true)
                        .addField("Guilds", MetricsManager.getDiscordMetrics().GUILD_COUNT + "", true)
                        .addField("Commands", Cache.COMMANDS.size() + "", true)
                        .addField("Prefix", Configuration.GLOBAL_PREFIX + ", `" + Utils.getServerPrefix(e.getGuild().getId()) + "`", true)
                        .addField("Uptime", TextUtility.getTimestamp(MetricsManager.getSystemMetrics().UPTIME), true)
                        .addField("Ping", MetricsManager.getDiscordMetrics().PING + "", true);
                MessageHandler.sendMessage(textChannel, about.build());
            });
        } catch(Exception ex) {
            MessageHandler.sendException(ex, "GuildJoinEvent -> Initial Message");
        }

        Utils.updateDiscordBotList();
        MetricsManager.updateDiscordMetrics();
    }

    private void guildLeaveEvent(GuildLeaveEvent e) {
        DatabaseFunctions.cleanup(e.getGuild().getId());
        Utils.updateDiscordBotList();
        MetricsManager.updateDiscordMetrics();
    }

    private void guildUpdateNameEvent(GuildUpdateNameEvent e) {
        DatabaseFunctions.updateGuildName(e.getGuild().getId(), e.getNewName());
    }

    private void guildUpdateRegionEvent(GuildUpdateRegionEvent e) {
        DatabaseFunctions.updateGuildRegion(e.getGuild().getId(), e.getNewRegion().getName());
    }

    private void guildMemberJoinEvent(GuildMemberJoinEvent e) {
        TextChannel textChannel = e.getGuild().getTextChannelById(DatabaseFunctions.getGuildSetting("newMember", e.getGuild().getId()));
        if(textChannel != null) {
            EmbedBuilder member = new EmbedBuilder().setTitle("New Member").setDescription("Welcome to **" + e.getGuild().getName() + "**, " + e.getMember().getAsMention() + "!");
            MessageHandler.sendMessage(textChannel, member.build());
        }

        MetricsManager.getDiscordMetrics().USER_COUNT += 1;
    }

    private void guildMemberLeaveEvent(GuildMemberLeaveEvent e) {
        MetricsManager.getDiscordMetrics().USER_COUNT -= 1;
    }
}
