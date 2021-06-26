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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Arrays;

public class CommandLogSetting extends Command {

    public CommandLogSetting() {
        super("commandlog", Arrays.asList("-commandlog", "-commandlog setup", "-commandlog <#channel>", "-commandlog unset"), Arrays.asList(Permission.MANAGE_SERVER));
    }

    public void onCommand(MessageEvent context) {
        if(!context.hasParameters()) {
            String channel = GuildFunctions.getGuildSetting("commandlog", context.getGuild().getId());
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(context.i18n("title"))
                    .setDescription((channel == null) ? context.i18n("not_set") : context.i18n("set").formatted(context.getGuild().getTextChannelById(channel).getAsMention()))
                    .addField(context.i18n("help"), context.i18n("help_desc").formatted(context.getPrefix(), context.getCommand().getName()), false);
            MessageDispatcher.reply(context, embed.build());
            return;
        }

        if(context.getParameters().equalsIgnoreCase("setup")) {
            context.getGuild().createTextChannel("command-log").queue(channel -> {
                channel.createPermissionOverride(context.getGuild().getSelfMember()).setAllow(Permission.MESSAGE_WRITE, Permission.MESSAGE_EMBED_LINKS).queue();
                if(GuildFunctions.setGuildSettings("commandlog", channel.getId(), context.getGuild().getId())) {
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
            if(GuildFunctions.setGuildSettings("commandlog", channel.getId(), context.getGuild().getId())) {
                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle(context.i18n("title"))
                        .setDescription(context.i18n("set_success").formatted(channel.getAsMention()));
                MessageDispatcher.reply(context, embed.build());
            }
            return;
        }

        if(GuildFunctions.setGuildSettings("commandlog", null, context.getGuild().getId())) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(context.i18n("title"))
                    .setDescription(context.i18n("unset_success"));
            MessageDispatcher.reply(context, embed.build());
        }
    }

    /**
     * Executes the command logging function if it is enabled.
     * @param context MessageEvent
     * @param executionTimeMs long
     */
    public static void execute(MessageEvent context, double executionTimeMs) {
        String channelId = GuildFunctions.getGuildSetting("commandlog", context.getGuild().getId());
        if(channelId != null) {
            TextChannel log = context.getGuild().getTextChannelById(channelId);
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(context.i18n("command", "command_log"))
                    .setThumbnail(context.getAuthor().getAvatarUrl())
                    .addField(context.i18n("user", "command_log"), context.getAuthor().getAsTag(), true)
                    .addField(context.i18n("command", "command_log"), context.getMessage().getContentDisplay(), true)
                    .addField(context.i18n("channel", "command_log"), context.getMessage().getTextChannel().getAsMention(), true)
                    .addField(context.i18n("execution", "command_log"), new BigDecimal(executionTimeMs).setScale(2, RoundingMode.HALF_UP) + "ms", true)
                    .setFooter(Yuuko.STANDARD_STRINGS.get(0), Yuuko.BOT.getAvatarUrl())
                    .setTimestamp(Instant.now());
            MessageDispatcher.sendMessage(context, log, embed.build());
        }
    }
}
