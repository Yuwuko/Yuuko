package com.yuuko.core.commands.core.settings;

import com.yuuko.core.database.DatabaseFunctions;
import com.yuuko.core.utilities.MessageHandler;
import com.yuuko.core.utilities.MessageUtility;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.guild.GuildBanEvent;
import net.dv8tion.jda.core.events.guild.GuildUnbanEvent;
import net.dv8tion.jda.core.events.message.MessageBulkDeleteEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageUpdateEvent;

import java.time.Instant;

public class ModerationLogSetting {

    public ModerationLogSetting(MessageReceivedEvent e, String value) {
        onCommand(e, value);
    }

    private void onCommand(MessageReceivedEvent e, String value) {
        if(value.equalsIgnoreCase("setup")) {
            if(e.getGuild().getSelfMember().hasPermission(Permission.MANAGE_CHANNEL, Permission.MANAGE_PERMISSIONS)) {
                setup(e);
            } else {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Missing Permission").setDescription("I require the **Manage Channel** and **Manage Permissions** permissions to setup the moderation log automatically.");
                MessageHandler.sendMessage(e, embed.build());
            }
            return;
        }

        TextChannel channel = MessageUtility.getFirstMentionedChannel(e);
        if(channel != null) {
            if(DatabaseFunctions.setGuildSettings("modLog", channel.getId(), e.getGuild().getId())) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Moderation Log").setDescription("The moderation log has been set to " + channel.getAsMention() + ".");
                MessageHandler.sendMessage(e, embed.build());
            }
        } else {
            if(DatabaseFunctions.setGuildSettings("modLog", null, e.getGuild().getId())) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Moderation Log").setDescription("The moderation log has been unset, deactivating the log.");
                MessageHandler.sendMessage(e, embed.build());
            }
        }
    }

    private void setup(MessageReceivedEvent e) {
        try {
            e.getGuild().getController().createTextChannel("modlog").queue(channel -> {
                TextChannel textChannel = (TextChannel)channel;
                channel.createPermissionOverride(e.getGuild().getSelfMember()).setAllow(Permission.MESSAGE_WRITE).queue();
                if(DatabaseFunctions.setGuildSettings("modLog", channel.getId(), e.getGuild().getId())) {
                    EmbedBuilder embed = new EmbedBuilder().setTitle("Moderation Log").setDescription("The " + textChannel.getAsMention() + " channel has been setup correctly.");
                    MessageHandler.sendMessage(e, embed.build());
                }
            });
        } catch(Exception ex) {
            //
        }
    }

    /**
     * Executes GuildBanEvent logging if the mod log is set.
     * @param e GuildBanEvent
     */
    public static void execute(GuildBanEvent e) {
        TextChannel log = e.getGuild().getTextChannelById(DatabaseFunctions.getGuildSetting("modLog", e.getGuild().getId()));
        if(log != null) {
            e.getGuild().getBan(e.getUser()).queue(ban -> {
                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle("Ban")
                        .addField("User", ban.getUser().getName() + "#" + ban.getUser().getDiscriminator(), true)
                        .addField("Reason", ban.getReason(), true)
                        .setTimestamp(Instant.now());
                MessageHandler.sendMessage(log, embed.build());
            });
        }
    }

    /**
     * Executes GuildUnbanEvent logging if the mod log is set.
     * @param e GuildUnbanEvent
     */
    public static void execute(GuildUnbanEvent e) {
        TextChannel log = e.getGuild().getTextChannelById(DatabaseFunctions.getGuildSetting("modLog", e.getGuild().getId()));
        if(log != null) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("Unban")
                    .addField("User", e.getUser().getName(), true)
                    .setTimestamp(Instant.now());
            MessageHandler.sendMessage(log, embed.build());
        }
    }

    /**
     * Executes GuildMessageUpdateEvent logging if the mod log is set.
     * @param e GuildMessageUpdateEvent
     */
    public static void execute(GuildMessageUpdateEvent e) {
        TextChannel log = e.getGuild().getTextChannelById(DatabaseFunctions.getGuildSetting("modLog", e.getGuild().getId()));
        if(log != null) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("Message Updated")
                    .addField("User", e.getAuthor().getName() + "#" + e.getAuthor().getDiscriminator(), true)
                    // TODO
                    .setTimestamp(Instant.now());
            MessageHandler.sendMessage(log, embed.build());
        }
    }

    /**
     * Executes GuildMessageDeleteEvent logging if the mod log is set.
     * @param e GuildMessageDeleteEvent
     */
    public static void execute(GuildMessageDeleteEvent e) {
        TextChannel log = e.getGuild().getTextChannelById(DatabaseFunctions.getGuildSetting("modLog", e.getGuild().getId()));
        if(log != null) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("Message Deleted")
                    //TODO Add to database!
                    .setTimestamp(Instant.now());
            MessageHandler.sendMessage(log, embed.build());
        }
    }

    /**
     * Executes MessageBulkDeleteEvent logging if the mod log is set.
     * @param e MessageBulkDeleteEvent
     */
    public static void execute(MessageBulkDeleteEvent e) {
        TextChannel log = e.getGuild().getTextChannelById(DatabaseFunctions.getGuildSetting("modLog", e.getGuild().getId()));
        if(log != null) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("Messages Deleted")
                    .addField("Count", e.getMessageIds().size() + "", true)
                    .setTimestamp(Instant.now());
            MessageHandler.sendMessage(log, embed.build());
        }
    }
}
