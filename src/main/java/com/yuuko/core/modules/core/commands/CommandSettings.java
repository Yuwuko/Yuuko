package com.yuuko.core.modules.core.commands;

import com.yuuko.core.Cache;
import com.yuuko.core.Configuration;
import com.yuuko.core.database.DatabaseConnection;
import com.yuuko.core.database.DatabaseFunctions;
import com.yuuko.core.modules.Command;
import com.yuuko.core.modules.core.settings.SettingCommandPrefix;
import com.yuuko.core.modules.core.settings.SettingExecuteBoolean;
import com.yuuko.core.utils.MessageHandler;
import com.yuuko.core.utils.Sanitise;
import com.yuuko.core.utils.Utils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.sql.Connection;
import java.sql.ResultSet;

public class CommandSettings extends Command {

    public CommandSettings() {
        super("settings", "com.yuuko.core.modules.core.ModuleCore", 0, new String[]{"-settings", "-settings [setting] [value]"}, Permission.MANAGE_SERVER);
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

                if(!Sanitise.checkParameters(e, command, 2)) {
                    return;
                }

                if(commandParameters[0].equalsIgnoreCase("commandLogging") || commandParameters[0].equalsIgnoreCase("deleteExecuted") || commandParameters[0].equalsIgnoreCase("nowPlaying") || commandParameters[0].equalsIgnoreCase("djmode")) {
                    if(!commandParameters[1].equalsIgnoreCase("true") && !commandParameters[1].equalsIgnoreCase("false")) {
                        EmbedBuilder embed = new EmbedBuilder().setTitle("_" + commandParameters[1].toUpperCase() + "_ is not a valid value. (Valid: TRUE, FALSE)");
                        MessageHandler.sendMessage(e, embed.build());
                        return;
                    }
                    new SettingExecuteBoolean(e, commandParameters[0].toLowerCase(), commandParameters[1]);
                    return;
                }

                if(commandParameters[0].equalsIgnoreCase("commandPrefix")) {
                    new SettingCommandPrefix(e, commandParameters[1]);
                }

            } else {

                Connection connection = DatabaseConnection.getConnection();
                try {
                    ResultSet resultSet = new DatabaseFunctions().getServerSettings(connection, e.getGuild().getId());
                    resultSet.next();

                    // Embed displaying all of the current settings for the server, giving information about each setting.
                    EmbedBuilder commandModules = new EmbedBuilder()
                        .setTitle("Settings for **" + e.getGuild().getName() + "**")
                        .setDescription("Settings can be changed by typing '<prefix>settings [setting] [value]' where [setting] is a value found below and [value] is a valid value, with special values like booleans being either TRUE or FALSE (case insensitive)")
                            .addField("commandPrefix", "[" + resultSet.getString("commandPrefix") + "] - The message prefix used to symbolise a command.", false)
                            .addField("deleteExecuted", "[" + resultSet.getBoolean("deleteExecuted") + "] - Deletes the users command string when it is executed.", false)
                            .addField("commandLogging", "[" + resultSet.getBoolean("commandLogging") + "] - Sends executed commands to a predefined logging channel.", false)
                            .addField("nowPlaying", "[" + resultSet.getBoolean("nowPlaying") + "] - Sends information of the current track when it changes.", false)
                            .addField("djMode", "[" + resultSet.getBoolean("djMode") + "] - Defines if DJ mode is on, meaning only users with the role 'DJ' can use certain audio commands.", false)
                        .setFooter(Configuration.VERSION, e.getGuild().getMemberById(Configuration.BOT_ID).getUser().getAvatarUrl());
                    MessageHandler.sendMessage(e, commandModules.build());
                } catch(Exception ex) {
                    Utils.sendException(ex, e.getMessage().getContentRaw());
                } finally {
                    try {
                        connection.close();
                    } catch(Exception ex) {
                        Utils.sendException(ex, "Unable to close connection to database. [CommandSettings]");
                    }
                }
            }

        } catch(Exception ex) {
            Utils.sendException(ex, e.getMessage().getContentRaw());
        }

    }

}
