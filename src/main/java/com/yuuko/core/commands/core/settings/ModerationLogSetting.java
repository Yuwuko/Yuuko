package com.yuuko.core.commands.core.settings;

import com.yuuko.core.Cache;
import com.yuuko.core.database.DatabaseFunctions;
import com.yuuko.core.utilities.MessageHandler;
import com.yuuko.core.utilities.MessageUtilities;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.guild.GuildUnbanEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

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

        TextChannel channel = MessageUtilities.getFirstMentionedChannel(e);
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
            e.getGuild().getController().createTextChannel("mod-log").queue(channel -> {
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
     * Logs ban events from the ban command.
     *
     * @param e MessageReceivedEvent
     * @param target User to be banned
     * @param time length of time for user to be banned
     * @param reason why the user was banned
     */
    public static void execute(MessageReceivedEvent e, User target, int time, String reason) {
        String channelId = DatabaseFunctions.getGuildSetting("modLog", e.getGuild().getId());
        if(channelId != null) {
            TextChannel log = e.getGuild().getTextChannelById(channelId);
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("Ban")
                    .setThumbnail(target.getAvatarUrl())
                    .addField("User", target.getName() + "#" + target.getDiscriminator(), true)
                    .addField("Reason", reason, true)
                    .addField("Time", (time == 0) ? "Permanent" : time + "", false)
                    .setFooter(Cache.STANDARD_STRINGS[0], Cache.BOT.getAvatarUrl())
                    .setTimestamp(Instant.now());
            MessageHandler.sendMessage(log, embed.build());
        }
    }

    /**
     * Executes GuildUnbanEvent logging if the mod log is set.
     *
     * @param e GuildUnbanEvent
     */
    public static void execute(GuildUnbanEvent e) {
        String channelId = DatabaseFunctions.getGuildSetting("modLog", e.getGuild().getId());
        if(channelId != null) {
            TextChannel log = e.getGuild().getTextChannelById(channelId);
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("Unban")
                    .addField("User", e.getUser().getName(), true)
                    .setFooter(Cache.STANDARD_STRINGS[0], Cache.BOT.getAvatarUrl())
                    .setTimestamp(Instant.now());
            MessageHandler.sendMessage(log, embed.build());
        }
    }

    /**
     * Logs mute events from the given action command.
     *
     * @param e MessageReceivedEvent
     * @param action String
     * @param target User
     * @param reason String
     */
    public static void execute(MessageReceivedEvent e, String action, User target, String reason) {
        String channelId = DatabaseFunctions.getGuildSetting("modLog", e.getGuild().getId());
        if(channelId != null) {
            TextChannel log = e.getGuild().getTextChannelById(channelId);
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(action)
                    .setThumbnail(target.getAvatarUrl())
                    .addField("User", target.getName() + "#" + target.getDiscriminator(), true)
                    .addField("Moderator", e.getMessage().getMember().getEffectiveName(), true)
                    .addField("Reason", reason, false)
                    .setFooter(Cache.STANDARD_STRINGS[0], Cache.BOT.getAvatarUrl())
                    .setTimestamp(Instant.now());
            MessageHandler.sendMessage(log, embed.build());
        }
    }

    /**
     * Logs messages deleted using the nuke command.
     *
     * @param e MessageReceivedEvent
     */
    public static void execute(MessageReceivedEvent e, int messagesDeleted) {
        String channelId = DatabaseFunctions.getGuildSetting("modLog", e.getGuild().getId());
        if(channelId != null) {
            TextChannel log = e.getGuild().getTextChannelById(channelId);
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("Message Deleted")
                    .setThumbnail(e.getAuthor().getAvatarUrl())
                    .addField("Moderator", e.getAuthor().getName() + "#" + e.getAuthor().getDiscriminator(), true)
                    .addField("Channel", e.getTextChannel().getAsMention(), true)
                    .addField("Count", messagesDeleted + "", false)
                    .setFooter(Cache.STANDARD_STRINGS[0], Cache.BOT.getAvatarUrl())
                    .setTimestamp(Instant.now());
            MessageHandler.sendMessage(log, embed.build());
        }
    }
}
