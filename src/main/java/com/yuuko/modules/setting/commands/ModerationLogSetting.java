package com.yuuko.modules.setting.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.Yuuko;
import com.yuuko.database.function.GuildFunctions;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.modules.Command;
import com.yuuko.utilities.MessageUtilities;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.GuildUnbanEvent;

import java.time.Instant;
import java.util.Arrays;

public class ModerationLogSetting extends Command {

    public ModerationLogSetting() {
        super("moderationlog", 0, -1L, Arrays.asList("-moderationlog", "-moderationlog setup", "-moderationlog <#channel>", "-moderationlog unset"), false, Arrays.asList(Permission.MANAGE_SERVER));
    }

    public void onCommand(MessageEvent context) throws Exception {
        if(!context.hasParameters()) {
            String channel = GuildFunctions.getGuildSetting("moderationlog", context.getGuild().getId());
            EmbedBuilder embed = new EmbedBuilder().setTitle("Moderation Log")
                    .setDescription((channel == null) ? "There is currently no moderation log set." : "The moderation log is currently set to use " + context.getGuild().getTextChannelById(channel).getAsMention())
                    .addField("Help", "Use `" + context.getPrefix() + "help " + context.getCommand().getName() + "` to get information on how to use this command.", true);
            MessageDispatcher.reply(context, embed.build());
            return;
        }

        if(context.getParameters().equalsIgnoreCase("setup")) {
            context.getGuild().createTextChannel("moderation-log").queue(channel -> {
                channel.createPermissionOverride(context.getGuild().getSelfMember()).setAllow(Permission.MESSAGE_WRITE, Permission.MESSAGE_EMBED_LINKS).queue();
                if(GuildFunctions.setGuildSettings("moderationlog", channel.getId(), context.getGuild().getId())) {
                    EmbedBuilder embed = new EmbedBuilder().setTitle("Moderation Log").setDescription("The " + channel.getAsMention() + " channel has been setup correctly.");
                    MessageDispatcher.reply(context, embed.build());
                }
            });
            return;
        }

        TextChannel channel = MessageUtilities.getFirstMentionedChannel(context);
        if(channel != null) {
            if(GuildFunctions.setGuildSettings("moderationlog", channel.getId(), context.getGuild().getId())) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Moderation Log").setDescription("The moderation log has been set to " + channel.getAsMention() + ".");
                MessageDispatcher.reply(context, embed.build());
            }
            return;
        }

        if(GuildFunctions.setGuildSettings("moderationlog", null, context.getGuild().getId())) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Moderation Log").setDescription("The moderation log has been unset, deactivating the log.");
            MessageDispatcher.reply(context, embed.build());
        }
    }

    /**
     * Add new entry to mod-log on unban event.
     * @param e {@link GuildUnbanEvent}
     */
    public static void execute(GuildUnbanEvent e) {
        String channelId = GuildFunctions.getGuildSetting("moderationlog", e.getGuild().getId());
        if(channelId != null) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("Unban")
                    .addField("User", e.getUser().getAsTag(), true)
                    .setTimestamp(Instant.now())
                    .setFooter(Yuuko.STANDARD_STRINGS.get(0), Yuuko.BOT.getEffectiveAvatarUrl());
            MessageDispatcher.sendMessage(e, e.getGuild().getTextChannelById(channelId), embed.build());
        }
    }

    /**
     * Add new entry to mod-log on from the given action command.
     * @param e {@link MessageEvent}
     * @param action String
     * @param target {@link User}
     * @param reason String
     */
    public static void execute(MessageEvent e, String action, User target, String reason) {
        String channelId = GuildFunctions.getGuildSetting("moderationlog", e.getGuild().getId());
        if(channelId != null) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(action)
                    .setThumbnail(target.getEffectiveAvatarUrl())
                    .addField("User", target.getAsTag(), true)
                    .addField("Moderator", e.getMember().getUser().getAsTag(), true)
                    .addField("Reason", reason, false)
                    .setTimestamp(Instant.now())
                    .setFooter(Yuuko.STANDARD_STRINGS.get(0), Yuuko.BOT.getEffectiveAvatarUrl());
            MessageDispatcher.sendMessage(e, e.getGuild().getTextChannelById(channelId), embed.build());
        }
    }

    /**
     * Add new entry to mod-log from use of the nuke command.
     * @param e {@link MessageEvent}
     * @param messagesDeleted int
     */
    public static void execute(MessageEvent e, int messagesDeleted) {
        String channelId = GuildFunctions.getGuildSetting("moderationlog", e.getGuild().getId());
        if(channelId != null) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("Message Deleted")
                    .setThumbnail(e.getAuthor().getEffectiveAvatarUrl())
                    .addField("Moderator", e.getMember().getUser().getAsTag(), true)
                    .addField("Channel", e.getChannel().getAsMention(), true)
                    .addField("Count", messagesDeleted + "", false)
                    .setTimestamp(Instant.now())
                    .setFooter(Yuuko.STANDARD_STRINGS.get(0), Yuuko.BOT.getEffectiveAvatarUrl());
            MessageDispatcher.sendMessage(e, e.getGuild().getTextChannelById(channelId), embed.build());
        }
    }
}
