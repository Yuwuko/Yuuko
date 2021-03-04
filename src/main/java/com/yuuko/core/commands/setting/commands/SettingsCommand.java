package com.yuuko.core.commands.setting.commands;

import com.yuuko.core.MessageDispatcher;
import com.yuuko.core.Yuuko;
import com.yuuko.core.commands.Command;
import com.yuuko.core.database.function.GuildFunctions;
import com.yuuko.core.events.entity.MessageEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.Arrays;

public class SettingsCommand extends Command {

    public SettingsCommand() {
        super("settings", Yuuko.MODULES.get("setting"), 0, -1L, Arrays.asList("-settings"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) throws Exception {
        // Embed displaying all of the current settings for the server, giving information about each setting.
        ArrayList<String> settingsList = GuildFunctions.getGuildSettings(e.getGuild().getId());

        String starboard = "`Disabled` - React with a ⭐ to `star` messages and send them to this channel.";
        if(settingsList.get(4) != null) {
            TextChannel textChannel = e.getGuild().getTextChannelById(settingsList.get(4));
            starboard = textChannel != null ? textChannel.getAsMention() : "Unable to find starboard.";
        }

        String commandlog = "`Disabled` - Sends executed commands to the defined log channel.";
        if(settingsList.get(5) != null) {
            TextChannel textChannel = e.getGuild().getTextChannelById(settingsList.get(5));
            commandlog = textChannel != null ? textChannel.getAsMention() : "Unable to find commandlog, please reset channel.";
        }

        String moderationlog = "`Disabled` - Sends moderation events to the defined log channel.";
        if(settingsList.get(6) != null) {
            TextChannel textChannel = e.getGuild().getTextChannelById(settingsList.get(6));
            moderationlog = textChannel != null ? textChannel.getAsMention() : "Unable to find moderationlog, please reset channel.";
        }

        String eventchannel = "`Disabled` - Where published events will be stored.";
        if(settingsList.get(7) != null) {
            TextChannel textChannel = e.getGuild().getTextChannelById(settingsList.get(7));
            eventchannel = textChannel != null ? textChannel.getAsMention() : "Unable to find eventchannel, please reset channel.";
        }

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Settings for `" + e.getGuild().getName() + "`")
                .setDescription("Settings can be changed by typing one of the commands listed below using the specified data type, e.g. `<boolean>` which you would replace with either `true` or `false`.")
                .addField(e.getPrefix() + "prefix <string>", "`" + settingsList.get(0) + "` - The message prefix used to symbolise a command.", false)
                .addField(e.getPrefix() + "cleanupcommands <boolean>", "`" + settingsList.get(1) + "` - Deletes the users command when it is executed.", false)
                .addField(e.getPrefix() + "playnotifications <boolean>", "`" + settingsList.get(2) + "` - Sends information of the mew track when it changes.", false)
                .addField(e.getPrefix() + "djmode <boolean>", "`" + settingsList.get(3) + "` - Defines if DJ mode is on, meaning only users with the role 'DJ' can use certain audio commands.", false)
                .addField(e.getPrefix() + "starboard <#channel>",  starboard, false)
                .addField(e.getPrefix() + "commandlog <#channel>", commandlog, false)
                .addField(e.getPrefix() + "moderationlog <#channel>", moderationlog, false)
                .addField(e.getPrefix() + "eventchannel <#channel>", eventchannel, false)
                .setFooter(Yuuko.STANDARD_STRINGS.get(1) + e.getAuthor().getAsTag(), e.getAuthor().getEffectiveAvatarUrl());
        MessageDispatcher.reply(e, embed.build());
    }

}
