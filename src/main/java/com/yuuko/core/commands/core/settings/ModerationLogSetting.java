package com.yuuko.core.commands.core.settings;

import com.yuuko.core.Configuration;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.database.function.GuildFunctions;
import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.utilities.MessageUtilities;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.guild.GuildUnbanEvent;

import java.time.Instant;

public class ModerationLogSetting extends Setting {

    public ModerationLogSetting(MessageEvent e) {
        onCommand(e);
    }

    protected void onCommand(MessageEvent e) {
        String[] parameters = e.getCommand().get(1).split("\\s+", 2);

        if(parameters[1].equalsIgnoreCase("setup")) {
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
            if(GuildFunctions.setGuildSettings("modlog", channel.getId(), e.getGuild().getId())) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Moderation Log").setDescription("The moderation log has been set to " + channel.getAsMention() + ".");
                MessageHandler.sendMessage(e, embed.build());
            }
        } else {
            if(GuildFunctions.setGuildSettings("modlog", null, e.getGuild().getId())) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Moderation Log").setDescription("The moderation log has been unset, deactivating the log.");
                MessageHandler.sendMessage(e, embed.build());
            }
        }
    }

    /**
     * Sets up the mod-log channel.
     *
     * @param e MessageEvent
     */
    private void setup(MessageEvent e) {
        try {
            e.getGuild().getController().createTextChannel("moderation-log").queue(channel -> {
                TextChannel textChannel = (TextChannel)channel;
                channel.createPermissionOverride(e.getGuild().getSelfMember()).setAllow(Permission.MESSAGE_WRITE, Permission.MESSAGE_EMBED_LINKS).queue();
                if(GuildFunctions.setGuildSettings("modlog", channel.getId(), e.getGuild().getId())) {
                    EmbedBuilder embed = new EmbedBuilder().setTitle("Moderation Log").setDescription("The " + textChannel.getAsMention() + " channel has been setup correctly.");
                    MessageHandler.sendMessage(e, embed.build());
                }
            });
        } catch(Exception ex) {
            //
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
                    .addField("User", e.getUser().getName(), true)
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
                    .addField("User", target.getName() + "#" + target.getDiscriminator(), true)
                    .addField("Moderator", e.getMessage().getMember().getEffectiveName(), true)
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
                    .addField("Moderator", e.getAuthor().getName() + "#" + e.getAuthor().getDiscriminator(), true)
                    .addField("Channel", e.getChannel().getAsMention(), true)
                    .addField("Count", messagesDeleted + "", false)
                    .setTimestamp(Instant.now())
                    .setFooter(Configuration.STANDARD_STRINGS.get(0), Configuration.BOT.getAvatarUrl());
            MessageHandler.sendMessage(e, log, embed.build());
        }
    }
}
