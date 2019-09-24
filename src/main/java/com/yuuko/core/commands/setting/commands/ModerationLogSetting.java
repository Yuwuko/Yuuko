package com.yuuko.core.commands.setting.commands;

import com.yuuko.core.Configuration;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.database.function.GuildFunctions;
import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.utilities.DiscordUtilities;
import com.yuuko.core.utilities.MessageUtilities;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.GuildUnbanEvent;

import java.time.Instant;
import java.util.Arrays;

public class ModerationLogSetting extends Command {

    public ModerationLogSetting() {
        super("modlog", Configuration.MODULES.get("setting"), 0, Arrays.asList("-modlog", "-modlog setup", "-modlog <#channel>", "-modlog unset"), false, Arrays.asList(Permission.MANAGE_SERVER));
    }

    public void onCommand(MessageEvent e) {
        if(!e.hasParameters()) {
            String channel = GuildFunctions.getGuildSetting("modlog", e.getGuild().getId());
            String status = (channel == null) ? "There is currently no moderation log set." : "The moderation log is currently set to use " + e.getGuild().getTextChannelById(channel).getAsMention();

            EmbedBuilder embed = new EmbedBuilder().setTitle("Moderation Log").setDescription(status)
                    .addField("Help", "Use `" + e.getPrefix() + "help " + e.getCommand().getName() + "` to get information on how to use this command.", true);
            MessageHandler.sendMessage(e, embed.build());
            return;
        }

        if(e.getParameters().equalsIgnoreCase("setup")) {
            if(e.getGuild().getSelfMember().hasPermission(Permission.MANAGE_CHANNEL, Permission.MANAGE_PERMISSIONS)) {
                e.getGuild().createTextChannel("moderation-log").queue(channel -> {
                    channel.createPermissionOverride(e.getGuild().getSelfMember()).setAllow(Permission.MESSAGE_WRITE, Permission.MESSAGE_EMBED_LINKS).queue();
                    if(GuildFunctions.setGuildSettings("modlog", channel.getId(), e.getGuild().getId())) {
                        EmbedBuilder embed = new EmbedBuilder().setTitle("Moderation Log").setDescription("The " + channel.getAsMention() + " channel has been setup correctly.");
                        MessageHandler.sendMessage(e, embed.build());
                    }
                });
            } else {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Missing Permission").setDescription("I require the **Manage Channel** and **Manage Permissions** permissions to setup the moderation log automatically.");
                MessageHandler.sendMessage(e, embed.build());
            }
            return;
        }

        TextChannel channel = MessageUtilities.getFirstMentionedChannel(e);
        if(channel != null) {
            if(GuildFunctions.setGuildSettings("modlog", channel.getId(), e.getGuild().getId())) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Moderation Log").setDescription("The moderation log has been set to " + channel.getAsMention() + ".");
                MessageHandler.sendMessage(e, embed.build());
            }
            return;

        }

        if(GuildFunctions.setGuildSettings("modlog", null, e.getGuild().getId())) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Moderation Log").setDescription("The moderation log has been unset, deactivating the log.");
            MessageHandler.sendMessage(e, embed.build());
        }
    }

    /**
     * Executes GuildUnbanEvent logging if the mod log is set.
     *
     * @param e GuildUnbanEvent
     */
    public static void execute(GuildUnbanEvent e) {
        String channelId = GuildFunctions.getGuildSetting("modlog", e.getGuild().getId());
        if(channelId != null) {
            TextChannel log = e.getGuild().getTextChannelById(channelId);
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("Unban")
                    .addField("User", DiscordUtilities.getTag(e.getUser()), true)
                    .setFooter(Configuration.STANDARD_STRINGS.get(0), Configuration.BOT.getAvatarUrl())
                    .setTimestamp(Instant.now());
            MessageHandler.sendMessage(e, log, embed.build());
        }
    }

    /**
     * Logs mute events from the given action command.
     *
     * @param e MessageEvent
     * @param action String
     * @param target User
     * @param reason String
     */
    public static void execute(MessageEvent e, String action, User target, String reason) {
        String channelId = GuildFunctions.getGuildSetting("modlog", e.getGuild().getId());
        if(channelId != null) {
            TextChannel log = e.getGuild().getTextChannelById(channelId);
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(action)
                    .setThumbnail(target.getAvatarUrl())
                    .addField("User", DiscordUtilities.getTag(target), true)
                    .addField("Moderator", DiscordUtilities.getTag(e.getMember()), true)
                    .addField("Reason", reason, false)
                    .setTimestamp(Instant.now())
                    .setFooter(Configuration.STANDARD_STRINGS.get(0), Configuration.BOT.getAvatarUrl());
            MessageHandler.sendMessage(e, log, embed.build());
        }
    }

    /**
     * Logs messages deleted using the nuke command.
     *
     * @param e MessageEvent
     */
    public static void execute(MessageEvent e, int messagesDeleted) {
        String channelId = GuildFunctions.getGuildSetting("modlog", e.getGuild().getId());
        if(channelId != null) {
            TextChannel log = e.getGuild().getTextChannelById(channelId);
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("Message Deleted")
                    .setThumbnail(e.getAuthor().getAvatarUrl())
                    .addField("Moderator", DiscordUtilities.getTag(e.getMember()), true)
                    .addField("Channel", e.getChannel().getAsMention(), true)
                    .addField("Count", messagesDeleted + "", false)
                    .setTimestamp(Instant.now())
                    .setFooter(Configuration.STANDARD_STRINGS.get(0), Configuration.BOT.getAvatarUrl());
            MessageHandler.sendMessage(e, log, embed.build());
        }
    }
}
