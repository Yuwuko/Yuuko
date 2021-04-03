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
        super("settings", 0, -1L, Arrays.asList("-settings"), false, null);
    }

    @Override
    public void onCommand(MessageEvent context) throws Exception {
        // Embed displaying all of the current settings for the server, giving information about each setting.
        HashMap<String, String> settings = GuildFunctions.getGuildSettings(context.getGuild().getId());

        String starboard = "`Disabled` - React with a ⭐ to `star` messages and send them to this channel.";
        if(settings.get("starboard") != null) {
            TextChannel textChannel = context.getGuild().getTextChannelById(settings.get("starboard"));
            starboard = textChannel != null ? textChannel.getAsMention() : "`Unable to find starboard, please reset channel.`";
        }

        String commandlog = "`Disabled` - Sends executed commands to the defined log channel.";
        if(settings.get("commandlog") != null) {
            TextChannel textChannel = context.getGuild().getTextChannelById(settings.get("commandlog"));
            commandlog = textChannel != null ? textChannel.getAsMention() : "`Unable to find commandlog, please reset channel.`";
        }

        String moderationlog = "`Disabled` - Sends moderation events to the defined log channel.";
        if(settings.get("moderationlog") != null) {
            TextChannel textChannel = context.getGuild().getTextChannelById(settings.get("moderationlog"));
            moderationlog = textChannel != null ? textChannel.getAsMention() : "`Unable to find moderationlog, please reset channel.`";
        }

        String eventchannel = "`Disabled` - Where published events will be stored.";
        if(settings.get("eventchannel") != null) {
            TextChannel textChannel = context.getGuild().getTextChannelById(settings.get("eventchannel"));
            eventchannel = textChannel != null ? textChannel.getAsMention() : "`Unable to find eventchannel, please reset channel.`";
        }

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Settings for `" + context.getGuild().getName() + "`")
                .setDescription("Settings can be changed by typing one of the commands listed below using the specified data type, e.g. `<boolean>` which you would replace with either `true` or `false`.")
                .addField(context.getPrefix() + "language <string>", "`" + settings.get("language") + "` - The language used when displaying output.", false)
                .addField(context.getPrefix() + "prefix <string>", "`" + settings.get("prefix") + "` - The message prefix used to symbolise a command.", false)
                .addField(context.getPrefix() + "cleanupcommands <boolean>", "`" + settings.get("cleanupcommands") + "` - Deletes the users command when it is executed.", false)
                .addField(context.getPrefix() + "playnotifications <boolean>", "`" + settings.get("playnotifications") + "` - Sends information of the mew track when it changes.", false)
                .addField(context.getPrefix() + "djmode <boolean>", "`" + settings.get("djmode") + "` - Defines if DJ mode is on, meaning only users with the role 'DJ' can use certain audio commands.", false)
                .addField(context.getPrefix() + "starboard <#channel>",  starboard, false)
                .addField(context.getPrefix() + "commandlog <#channel>", commandlog, false)
                .addField(context.getPrefix() + "moderationlog <#channel>", moderationlog, false)
                .addField(context.getPrefix() + "eventchannel <#channel>", eventchannel, false)
                .setFooter(Yuuko.STANDARD_STRINGS.get(1) + context.getAuthor().getAsTag(), context.getAuthor().getEffectiveAvatarUrl());
        MessageDispatcher.reply(context, embed.build());
    }

}
