package com.yuuko.modules.setting.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.Yuuko;
import com.yuuko.database.function.GuildFunctions;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.modules.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.Arrays;
import java.util.HashMap;

public class SettingsCommand extends Command {

    public SettingsCommand() {
        super("settings", Arrays.asList("-settings"));
    }

    @Override
    public void onCommand(MessageEvent context) throws Exception {
        // Embed displaying all of the current settings for the server, giving information about each setting.
        HashMap<String, String> settings = GuildFunctions.getGuildSettings(context.getGuild().getId());

        String starboard = context.i18n("starboard_disabled");
        if(settings.get("starboard") != null) {
            TextChannel textChannel = context.getGuild().getTextChannelById(settings.get("starboard"));
            starboard = textChannel != null ? textChannel.getAsMention() : context.i18n("missing");
        }

        String commandlog = context.i18n("commandlog_disabled");
        if(settings.get("commandlog") != null) {
            TextChannel textChannel = context.getGuild().getTextChannelById(settings.get("commandlog"));
            commandlog = textChannel != null ? textChannel.getAsMention() : context.i18n("missing");
        }

        String moderationlog = context.i18n("moderationlog_disabled");
        if(settings.get("moderationlog") != null) {
            TextChannel textChannel = context.getGuild().getTextChannelById(settings.get("moderationlog"));
            moderationlog = textChannel != null ? textChannel.getAsMention() : context.i18n("missing");
        }

        String eventchannel = context.i18n("eventchannel_disabled");
        if(settings.get("eventchannel") != null) {
            TextChannel textChannel = context.getGuild().getTextChannelById(settings.get("eventchannel"));
            eventchannel = textChannel != null ? textChannel.getAsMention() : context.i18n("missing");
        }

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle(context.i18n("title").formatted(context.getGuild().getName()))
                .setDescription(context.i18n("desc"))
                .addField(context.getPrefix() + "language <string>", "`" + settings.get("language") + "` - " + context.i18n("language_desc"), false)
                .addField(context.getPrefix() + "prefix <string>", "`" + settings.get("prefix") + "` - " + context.i18n("prefix_desc"), false)
                .addField(context.getPrefix() + "cleanupcommands <boolean>", "`" + settings.get("cleanupcommands") + "` - " + context.i18n("cleanupcommands_desc"), false)
                .addField(context.getPrefix() + "playnotifications <boolean>", "`" + settings.get("playnotifications") + "` - " + context.i18n("playnotifications_desc"), false)
                .addField(context.getPrefix() + "djmode <boolean>", "`" + settings.get("djmode") + "` - " + context.i18n("djmode_desc"), false)
                .addField(context.getPrefix() + "starboard <#channel>",  starboard, false)
                .addField(context.getPrefix() + "commandlog <#channel>", commandlog, false)
                .addField(context.getPrefix() + "moderationlog <#channel>", moderationlog, false)
                .addField(context.getPrefix() + "eventchannel <#channel>", eventchannel, false)
                .setFooter(Yuuko.STANDARD_STRINGS.get(1) + context.getAuthor().getAsTag(), context.getAuthor().getEffectiveAvatarUrl());
        MessageDispatcher.reply(context, embed.build());
    }

}
