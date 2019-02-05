package com.yuuko.core.commands.core.settings;

import com.yuuko.core.database.DatabaseFunctions;
import com.yuuko.core.utilities.MessageHandler;
import com.yuuko.core.utilities.MessageUtility;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.guild.GuildBanEvent;
import net.dv8tion.jda.core.events.guild.GuildUnbanEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.time.Instant;

public class ModerationLogSetting {

    public ModerationLogSetting(MessageReceivedEvent e, String value) {
        executeCommand(e, value);
    }

    private void executeCommand(MessageReceivedEvent e, String value) {
        if(value.equalsIgnoreCase("setup")) {
            if(e.getGuild().getSelfMember().hasPermission(Permission.MANAGE_CHANNEL, Permission.MANAGE_PERMISSIONS)) {
                setupModerationLog(e);
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

    private void setupModerationLog(MessageReceivedEvent e) {
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
     * Executes the logging feature of the bot.
     * @param e GuildBanEvent
     */
    public static void executeLogging(GuildBanEvent e) {
        TextChannel log = e.getGuild().getTextChannelById(DatabaseFunctions.getGuildSetting("modLog", e.getGuild().getId()));
        if(log != null) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("Ban")
                    .addField("User", e.getUser().getName() + "#" + e.getUser().getDiscriminator() + "(" + e.getUser().getAsTag() + ")" , true)
                    .setTimestamp(Instant.now());
            MessageHandler.sendMessage(log, embed.build());
        }
    }

    /**
     * Executes the logging feature of the bot.
     * @param e GuildUnbanEvent
     */
    public static void executeLogging(GuildUnbanEvent e) {
        TextChannel log = e.getGuild().getTextChannelById(DatabaseFunctions.getGuildSetting("modLog", e.getGuild().getId()));
        if(log != null) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("Unban")
                    .addField("User", e.getUser().getName() + "#" + e.getUser().getDiscriminator() + "(" + e.getUser().getAsTag() + ")" , true)
                    .setTimestamp(Instant.now());
            MessageHandler.sendMessage(log, embed.build());
        }
    }
}
