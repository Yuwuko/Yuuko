package com.basketbandit.core.modules.core.commands;

import com.basketbandit.core.Configuration;
import com.basketbandit.core.database.DatabaseConnection;
import com.basketbandit.core.database.DatabaseFunctions;
import com.basketbandit.core.modules.Command;
import com.basketbandit.core.utils.Utils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;

public class CommandSettings extends Command {

    public CommandSettings() {
        super("settings", "com.basketbandit.core.modules.core.ModuleCore", new String[]{"-settings", "-settings [setting] [value]"}, Permission.MANAGE_SERVER);
    }

    public CommandSettings(MessageReceivedEvent e, String[] command) {
        executeCommand(e, command);
    }

    @Override
    protected void executeCommand(MessageReceivedEvent e, String[] command) {
        try {
            if(command.length > 1) {
                String[] commandParameters = command[1].split("\\s+", 2);
                boolean value;

                // Check to see if the input value is actually a boolean.
                try {
                    value = Boolean.parseBoolean(commandParameters[1]);
                } catch(Exception ex) {
                    Utils.sendMessage(e, "Sorry, '" + commandParameters[1] + "' is not a valid boolean value.");
                    return;
                }

                // Check to stop people trying to set arbitrary column values.
                if(!commandParameters[0].toLowerCase().equals("deleteexecuted")) {
                    Utils.sendMessage(e, "Sorry, '" + commandParameters[0] + "' is not a setting.");
                    return;
                }

                if(new DatabaseFunctions().setServerSettings(commandParameters[0], value, e.getGuild().getId())) {
                    if(value) {
                        EmbedBuilder embed = new EmbedBuilder().setColor(Color.GREEN).setAuthor("'" + commandParameters[0] + "' set to TRUE.");
                        Utils.sendMessage(e, embed.build());
                    } else {
                        EmbedBuilder embed = new EmbedBuilder().setColor(Color.RED).setAuthor("'" + commandParameters[0] + "' set to FALSE.");
                        Utils.sendMessage(e, embed.build());
                    }
                } else {
                    Utils.sendMessage(e, "Sorry, unable to change server setting.");
                }

            } else {
                Connection connection = new DatabaseConnection().getConnection();
                try {
                    ResultSet resultSet = new DatabaseFunctions().getServerSettings(connection, e.getGuild().getId());
                    resultSet.next();

                    EmbedBuilder commandModules = new EmbedBuilder()
                        .setColor(Color.DARK_GRAY)
                        .setTitle(e.getGuild().getName() + " settings")
                        .setDescription("Settings can be changed by typing '<prefix>settings [setting] [value]' where [setting] is a value found below and [value] is a valid boolean value.")
                        .addField("deleteExecuted", "[**" + resultSet.getBoolean("deleteExecuted") + "**] - Deletes the users command string when it is executed.", false)
                        .setFooter(Configuration.VERSION, e.getGuild().getMemberById(Configuration.BOT_ID).getUser().getAvatarUrl());

                    Utils.sendMessage(e, commandModules.build());
                } catch(Exception ex) {
                    Utils.sendException(ex, e.getMessage().getContentRaw());
                } finally {
                    try {
                        connection.close();
                    } catch(Exception ex) {
                        Utils.consoleOutput("[ERROR] Unable to close connection to database.");
                    }
                }
            }
        } catch(Exception ex) {
            Utils.sendException(ex, e.getMessage().getContentRaw());
        }

    }

}
