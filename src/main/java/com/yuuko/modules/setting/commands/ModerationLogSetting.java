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
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(context.i18n("title"))
                    .setDescription((channel == null) ? context.i18n("not_set") : context.i18n("set").formatted(context.getGuild().getTextChannelById(channel).getAsMention()))
                    .addField(context.i18n("help"), context.i18n("help_desc").formatted(context.getPrefix(), context.getCommand().getName()), false);
            MessageDispatcher.reply(context, embed.build());
            return;
        }

        if(context.getParameters().equalsIgnoreCase("setup")) {
            context.getGuild().createTextChannel("moderation-log").queue(channel -> {
                channel.createPermissionOverride(context.getGuild().getSelfMember()).setAllow(Permission.MESSAGE_WRITE, Permission.MESSAGE_EMBED_LINKS).queue();
                if(GuildFunctions.setGuildSettings("moderationlog", channel.getId(), context.getGuild().getId())) {
                    EmbedBuilder embed = new EmbedBuilder()
                            .setTitle(context.i18n("title"))
                            .setDescription(context.i18n("setup").formatted(channel.getAsMention()));
                    MessageDispatcher.reply(context, embed.build());
                }
            });
            return;
        }

        TextChannel channel = MessageUtilities.getFirstMentionedChannel(context);
        if(channel != null) {
            if(GuildFunctions.setGuildSettings("moderationlog", channel.getId(), context.getGuild().getId())) {
                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle(context.i18n("title"))
                        .setDescription(context.i18n("set_success").formatted(channel.getAsMention()));
                MessageDispatcher.reply(context, embed.build());
            }
            return;
        }

        if(GuildFunctions.setGuildSettings("moderationlog", null, context.getGuild().getId())) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(context.i18n("title"))
                    .setDescription(context.i18n("unset_success"));
            MessageDispatcher.reply(context, embed.build());
        }
    }

    /**
     * Add new entry to mod-log on unban event.
     * @param context {@link GuildUnbanEvent}
     */
    public static void execute(GuildUnbanEvent context) {
        String channelId = GuildFunctions.getGuildSetting("moderationlog", context.getGuild().getId());
        if(channelId != null) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("Unban")
                    .addField("User", context.getUser().getAsTag(), true)
                    .setTimestamp(Instant.now())
                    .setFooter(Yuuko.STANDARD_STRINGS.get(0), Yuuko.BOT.getEffectiveAvatarUrl());
            MessageDispatcher.sendMessage(context, context.getGuild().getTextChannelById(channelId), embed.build());
        }
    }

    /**
     * Add new entry to mod-log on from the given action command.
     * @param context {@link MessageEvent}
     * @param target {@link User}
     * @param reason String
     */
    public static void execute(MessageEvent context, User target, String reason) {
        String channelId = GuildFunctions.getGuildSetting("moderationlog", context.getGuild().getId());
        if(channelId != null) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(context.i18n("action"))
                    .setThumbnail(target.getEffectiveAvatarUrl())
                    .addField(context.i18n("user"), target.getAsTag(), true)
                    .addField(context.i18n("moderator"), context.getMember().getUser().getAsTag(), true)
                    .addField(context.i18n("reason"), reason, false)
                    .setTimestamp(Instant.now())
                    .setFooter(Yuuko.STANDARD_STRINGS.get(0), Yuuko.BOT.getEffectiveAvatarUrl());
            MessageDispatcher.sendMessage(context, context.getGuild().getTextChannelById(channelId), embed.build());
        }
    }

    /**
     * Add new entry to mod-log from use of the nuke command.
     * @param context {@link MessageEvent}
     * @param messagesDeleted int
     */
    public static void execute(MessageEvent context, int messagesDeleted) {
        String channelId = GuildFunctions.getGuildSetting("moderationlog", context.getGuild().getId());
        if(channelId != null) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(context.i18n("deleted"))
                    .setThumbnail(context.getAuthor().getEffectiveAvatarUrl())
                    .addField(context.i18n("moderator"), context.getMember().getUser().getAsTag(), true)
                    .addField(context.i18n("channel"), context.getChannel().getAsMention(), true)
                    .addField(context.i18n("count"), messagesDeleted + "", false)
                    .setTimestamp(Instant.now())
                    .setFooter(Yuuko.STANDARD_STRINGS.get(0), Yuuko.BOT.getEffectiveAvatarUrl());
            MessageDispatcher.sendMessage(context, context.getGuild().getTextChannelById(channelId), embed.build());
        }
    }
}
