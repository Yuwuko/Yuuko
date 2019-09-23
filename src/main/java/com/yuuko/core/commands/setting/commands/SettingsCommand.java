package com.yuuko.core.commands.setting.commands;

import com.yuuko.core.Configuration;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.core.settings.Setting;
import com.yuuko.core.database.function.GuildFunctions;
import com.yuuko.core.events.entity.MessageEvent;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class SettingsCommand extends Command {

    private static final HashMap<String, Class<? extends Setting>> settings = new HashMap<>();

    public SettingsCommand() {
        super("settings", Configuration.MODULES.get("setting"), 0, Arrays.asList("-settings"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        // Embed displaying all of the current settings for the server, giving information about each setting.
        ArrayList<String> settingsList = GuildFunctions.getGuildSettings(e.getGuild().getId());
        EmbedBuilder commandModules = new EmbedBuilder()
                .setTitle("Settings for `" + e.getGuild().getName() + "`")
                .setDescription("Settings can be changed by typing one of the commands listed below using the specified data type, e.g. `<boolean>` which you would replace with either `true` or `false`.")
                .addField(e.getPrefix() + "prefix <string>", "`" + settingsList.get(0) + "` - The message prefix used to symbolise a command.", false)
                .addField(e.getPrefix() + "deleteExecuted <boolean>", "`" + settingsList.get(1) + "` - Deletes the users command string when it is executed.", false)
                .addField(e.getPrefix() + "nowPlaying <boolean>", "`" + settingsList.get(2) + "` - Sends information of the current track when it changes.", false)
                .addField(e.getPrefix() + "djMode <boolean>", "`" + settingsList.get(3) + "` - Defines if DJ mode is on, meaning only users with the role 'DJ' can use certain audio commands.", false)
                .addField(e.getPrefix() + "newMember <#channel>", (settingsList.get(4) != null ? e.getGuild().getTextChannelById(settingsList.get(4)).getAsMention() : "`Disabled`") + " - Where Yuuko will greet each new member that joins the server.", false)
                .addField(e.getPrefix() + "newMember Message <string>", (settingsList.get(5) != null) ? settingsList.get(5) : "No message set. (Default)", false)
                .addField(e.getPrefix() + "starboard <#channel>", (settingsList.get(6) != null ? e.getGuild().getTextChannelById(settingsList.get(6)).getAsMention() : "`Disabled`") + " - Where any messages reacted to with a ⭐ will be sent.", false)
                .addField(e.getPrefix() + "comLog <#channel>", (settingsList.get(7) != null ? e.getGuild().getTextChannelById(settingsList.get(7)).getAsMention() : "`Disabled`") + " - Sends executed commands to a defined log channel.", false)
                .addField(e.getPrefix() + "modLog <#channel>", (settingsList.get(8) != null ? e.getGuild().getTextChannelById(settingsList.get(8)).getAsMention() : "`Disabled`") + " - Sends moderation events to a defined log channel.", false)
                .setFooter(Configuration.STANDARD_STRINGS.get(1) + e.getMember().getEffectiveName(), e.getAuthor().getEffectiveAvatarUrl());
        MessageHandler.sendMessage(e, commandModules.build());
    }

}
