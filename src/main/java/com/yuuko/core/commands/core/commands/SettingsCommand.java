package com.yuuko.core.commands.core.commands;

import com.yuuko.core.Cache;
import com.yuuko.core.Configuration;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.core.CoreModule;
import com.yuuko.core.commands.core.settings.PrefixSetting;
import com.yuuko.core.commands.core.settings.SettingExecuteBoolean;
import com.yuuko.core.database.DatabaseFunctions;
import com.yuuko.core.utilities.MessageHandler;
import com.yuuko.core.utilities.Sanitiser;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;

public class SettingsCommand extends Command {

    public SettingsCommand() {
        super("settings", CoreModule.class, 0, new String[]{"-settings", "-settings [setting] [value]"}, new Permission[]{Permission.MANAGE_SERVER});
    }

    @Override
    public void executeCommand(MessageReceivedEvent e, String[] command) {
        try {
            if(command.length > 1) {
                String[] commandParameters = command[1].split("\\s+", 2);

                // Check to make sure the command is a valid command.
                if(!Cache.SETTINGS.contains(commandParameters[0].toLowerCase())) {
                    EmbedBuilder embed = new EmbedBuilder().setTitle("_" + commandParameters[1].toUpperCase() + "_ is not a valid setting.");
                    MessageHandler.sendMessage(e, embed.build());
                    return;
                }

                if(!Sanitiser.checkParameters(e, command, 2)) {
                    return;
                }

                if(commandParameters[0].equalsIgnoreCase("commandLogging")
                        || commandParameters[0].equalsIgnoreCase("deleteExecuted")
                        || commandParameters[0].equalsIgnoreCase("nowPlaying")
                        || commandParameters[0].equalsIgnoreCase("djmode")
                        || commandParameters[0].equalsIgnoreCase("welcomeMembers")) {
                    if(!commandParameters[1].equalsIgnoreCase("true") && !commandParameters[1].equalsIgnoreCase("false")) {
                        EmbedBuilder embed = new EmbedBuilder().setTitle("_" + commandParameters[1].toUpperCase() + "_ is not a valid value. (Valid: TRUE, FALSE)");
                        MessageHandler.sendMessage(e, embed.build());
                        return;
                    }
                    new SettingExecuteBoolean(e, commandParameters[0].toLowerCase(), commandParameters[1]);
                    return;
                }

                if(commandParameters[0].equalsIgnoreCase("prefix")) {
                    new PrefixSetting(e, commandParameters[1]);
                }

            } else {
                ArrayList<Boolean> settings = DatabaseFunctions.getGuildSettings(e.getGuild().getId());

                // Embed displaying all of the current settings for the server, giving information about each setting.
                EmbedBuilder commandModules = new EmbedBuilder()
                        .setTitle("Settings for **" + e.getGuild().getName() + "**")
                        .setDescription("Settings can be changed by typing '<prefix>settings [setting] [value]' where [setting] is a value found below and [value] is a valid value, with special values like booleans being either TRUE or FALSE (case insensitive)")
                            .addField("prefix", "[**" + DatabaseFunctions.getGuildSetting("commandPrefix", e.getGuild().getId()) + "**] - The message prefix used to symbolise a command.", false)
                            .addField("deleteExecuted", "[**" + settings.get(0) + "**] - Deletes the users command string when it is executed.", false)
                            .addField("commandLogging", "[**" + settings.get(1) + "**] - Sends executed commands to a predefined logging channel.", false)
                            .addField("nowPlaying", "[**" + settings.get(2) + "**] - Sends information of the current track when it changes.", false)
                            .addField("djMode", "[**" + settings.get(3) + "**] - Defines if DJ mode is on, meaning only users with the role 'DJ' can use certain audio commands.", false)
                            .addField("welcomeMembers", "[**" + settings.get(4) + "**] - Whether or not Yuuko will greet each new member that joins the server.", false)
                        .setFooter(Cache.STANDARD_STRINGS[1] + e.getMember().getEffectiveName(), e.getGuild().getMemberById(Configuration.BOT_ID).getUser().getAvatarUrl());
                MessageHandler.sendMessage(e, commandModules.build());
            }

        } catch(Exception ex) {
            MessageHandler.sendException(ex, e.getMessage().getContentRaw());
        }

    }

}
