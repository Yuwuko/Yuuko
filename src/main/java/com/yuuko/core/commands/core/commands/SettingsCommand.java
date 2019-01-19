package com.yuuko.core.commands.core.commands;

import com.yuuko.core.Cache;
import com.yuuko.core.Configuration;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.core.CoreModule;
import com.yuuko.core.commands.core.settings.*;
import com.yuuko.core.database.DatabaseFunctions;
import com.yuuko.core.utilities.MessageHandler;
import com.yuuko.core.utilities.Sanitiser;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

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
            "starboard"
    };

    public SettingsCommand() {
        super("settings", CoreModule.class, 0, new String[]{"-settings", "-settings [setting] [value]"}, new Permission[]{Permission.MANAGE_SERVER});
    }

    @Override
    public void executeCommand(MessageReceivedEvent e, String[] command) {
        try {
            if(command.length > 1) {
                String[] commandParameters = command[1].split("\\s+", 2);

                // Check to make sure the command is a valid command.
                if(!Arrays.asList(settings).contains(commandParameters[0].toLowerCase())) {
                    EmbedBuilder embed = new EmbedBuilder().setTitle("_" + commandParameters[1].toUpperCase() + "_ is not a valid setting.");
                    MessageHandler.sendMessage(e, embed.build());
                    return;
                }

                if(!Sanitiser.checkParameters(e, command, 2)) {
                    // We expect 0 in the super class to enable vision of all settings, but then check for 2 since that's the minimum used.
                    return;
                }

                switch(commandParameters[0].toLowerCase()) {
                    case "prefix": new PrefixSetting(e, commandParameters[1]);
                        return;
                    case "starboard": new StarboardSetting(e, commandParameters[1]);
                        return;
                    case "commandlog": new CommandLogSetting(e, commandParameters[1]);
                        return;
                    case "newmember": new NewMemberSetting(e);
                        return;
                    default:
                        if(!commandParameters[1].equalsIgnoreCase("true") && !commandParameters[1].equalsIgnoreCase("false")) {
                            EmbedBuilder embed = new EmbedBuilder().setTitle("_" + commandParameters[1].toUpperCase() + "_ is not a valid value. (Valid: TRUE, FALSE)");
                            MessageHandler.sendMessage(e, embed.build());
                            return;
                        }
                        new SettingExecuteBoolean(e, commandParameters[0].toLowerCase(), commandParameters[1]);
                }

            } else {
                ArrayList<String> settings = DatabaseFunctions.getGuildSettings(e.getGuild().getId());

                // Embed displaying all of the current settings for the server, giving information about each setting.
                EmbedBuilder commandModules = new EmbedBuilder()
                        .setTitle("Settings for **" + e.getGuild().getName() + "**")
                        .setDescription("Settings can be changed by typing **"+settings.get(0)+"settings [setting] [value]** where **[setting]** is a value found below and **[value]** is a valid value, with special values like booleans being either **TRUE** or **FALSE** (case insensitive)")
                            .addField("prefix (String)", "**__" + settings.get(0) + "__** - The message prefix used to symbolise a command.", false)
                            .addField("deleteExecuted (Boolean)", "**__" + settings.get(1) + "__** - Deletes the users command string when it is executed.", false)
                            .addField("nowPlaying (Boolean)", "**__" + settings.get(2) + "__** - Sends information of the current track when it changes.", false)
                            .addField("djMode (Boolean)", "**__" + settings.get(3) + "__** - Defines if DJ mode is on, meaning only users with the role 'DJ' can use certain audio commands.", false)
                            .addField("newMember (Mention)", (settings.get(4) != null ? e.getGuild().getTextChannelById(settings.get(4)).getAsMention() : "**__Disabled__**") + " - Where Yuuko will greet each new member that joins the server.", false)
                            .addField("starboard (Mention)", (settings.get(5) != null ? e.getGuild().getTextChannelById(settings.get(5)).getAsMention() : "**__Disabled__**") + " - Where any messages reacted to with a ⭐ will be sent.", false)
                            .addField("commandLog (Mention)", (settings.get(6) != null ? e.getGuild().getTextChannelById(settings.get(6)).getAsMention() : "**__Disabled__**") + " - Sends executed commands to a defined log channel.", false)
                        .setFooter(Cache.STANDARD_STRINGS[1] + e.getMember().getEffectiveName(), e.getGuild().getMemberById(Configuration.BOT_ID).getUser().getAvatarUrl());
                MessageHandler.sendMessage(e, commandModules.build());
            }

        } catch(Exception ex) {
            MessageHandler.sendException(ex, e.getMessage().getContentRaw());
        }

    }

}
