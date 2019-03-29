package com.yuuko.core.commands.core.commands;

import com.yuuko.core.Configuration;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.core.CoreModule;
import com.yuuko.core.commands.core.settings.*;
import com.yuuko.core.database.GuildFunctions;
import com.yuuko.core.events.extensions.MessageEvent;
import com.yuuko.core.utilities.Sanitiser;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;

import java.util.ArrayList;
import java.util.Arrays;

public class SettingsCommand extends Command {

    private static final String[] settings = new String[]{
            "prefix",
            "deleteexecuted",
            "commandlog",
            "nowplaying",
            "djmode",
            "newmember",
            "starboard",
            "modlog"
    };

    public SettingsCommand() {
        super("settings", CoreModule.class, 0, new String[]{"-settings", "-settings <setting> <value>"}, false, new Permission[]{Permission.MANAGE_SERVER});
    }

    @Override
    public void onCommand(MessageEvent e) {
        try {
            if(e.hasParameters()) {
                String[] parameters = e.getCommand()[1].split("\\s+", 2);

                // Check to make sure the command is a valid command.
                if(!Arrays.asList(settings).contains(parameters[0].toLowerCase())) {
                    EmbedBuilder embed = new EmbedBuilder().setTitle("_" + parameters[1].toUpperCase() + "_ is not a valid setting.");
                    MessageHandler.sendMessage(e, embed.build());
                    return;
                }

                if(!Sanitiser.checkParameters(e, 2, true)) {
                    // We expect 0 in the super class to enable vision of all settings, but then check for 2 since that's the minimum used.
                    return;
                }

                switch(parameters[0].toLowerCase()) {
                    case "prefix": new PrefixSetting(e);
                        return;
                    case "starboard": new StarboardSetting(e);
                        return;
                    case "commandlog": new CommandLogSetting(e);
                        return;
                    case "modlog": new ModerationLogSetting(e);
                        return;
                    case "newmember": new NewMemberSetting(e);
                        return;
                    default:
                        if(!parameters[1].equalsIgnoreCase("true") && !parameters[1].equalsIgnoreCase("false")) {
                            EmbedBuilder embed = new EmbedBuilder().setTitle("_" + parameters[1].toUpperCase() + "_ is not a valid value. (Valid: TRUE, FALSE)");
                            MessageHandler.sendMessage(e, embed.build());
                            return;
                        }
                        new SettingExecuteBoolean(e);
                }

            } else {
                ArrayList<String> settingsList = GuildFunctions.getGuildSettings(e.getGuild().getId());

                // Embed displaying all of the current settings for the server, giving information about each setting.
                EmbedBuilder commandModules = new EmbedBuilder()
                        .setTitle("Settings for **" + e.getGuild().getName() + "**")
                        .setDescription("Settings can be changed by typing **" + settingsList.get(0) + "settings [setting] [value]** where **[setting]** is a value found below and **[value]** is a valid value, with special values like booleans being either **TRUE** or **FALSE** (case insensitive)")
                            .addField("prefix _(String)_", "**__" + settingsList.get(0) + "__** - The message prefix used to symbolise a command.", false)
                            .addField("deleteExecuted _(Boolean)_", "**__" + settingsList.get(1) + "__** - Deletes the users command string when it is executed.", false)
                            .addField("nowPlaying _(Boolean)_", "**__" + settingsList.get(2) + "__** - Sends information of the current track when it changes.", false)
                            .addField("djMode _(Boolean)_", "**__" + settingsList.get(3) + "__** - Defines if DJ mode is on, meaning only users with the role 'DJ' can use certain audio commands.", false)
                            .addField("newMember _(Channel Mention)_", (settingsList.get(4) != null ? e.getGuild().getTextChannelById(settingsList.get(4)).getAsMention() : "**__Disabled__**") + " - Where Yuuko will greet each new member that joins the server.", false)
                            .addField("newMember Message _(Raw)_", (settingsList.get(5) != null) ? settingsList.get(5) : "No message set. (Default)", false)
                            .addField("starboard _(Channel Mention)_", (settingsList.get(6) != null ? e.getGuild().getTextChannelById(settingsList.get(6)).getAsMention() : "**__Disabled__**") + " - Where any messages reacted to with a ⭐ will be sent.", false)
                            .addField("commandLog _(Channel Mention)_", (settingsList.get(7) != null ? e.getGuild().getTextChannelById(settingsList.get(7)).getAsMention() : "**__Disabled__**") + " - Sends executed commands to a defined log channel.", false)
                            .addField("modLog _(Channel Mention)_", (settingsList.get(8) != null ? e.getGuild().getTextChannelById(settingsList.get(8)).getAsMention() : "**__Disabled__**") + " - Sends moderation events to a defined log channel.", false)
                        .setFooter(Configuration.STANDARD_STRINGS[1] + e.getMember().getEffectiveName(), e.getAuthor().getEffectiveAvatarUrl());
                MessageHandler.sendMessage(e, commandModules.build());
            }

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
        }

    }

}
