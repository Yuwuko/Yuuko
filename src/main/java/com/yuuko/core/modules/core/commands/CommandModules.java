package com.yuuko.core.modules.core.commands;

import com.yuuko.core.Cache;
import com.yuuko.core.Configuration;
import com.yuuko.core.database.DatabaseConnection;
import com.yuuko.core.database.DatabaseFunctions;
import com.yuuko.core.modules.Command;
import com.yuuko.core.utils.MessageHandler;
import com.yuuko.core.utils.Utils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;

public class CommandModules extends Command {

    public CommandModules() {
        super("modules", "com.yuuko.core.modules.core.ModuleCore", 0, new String[]{"-modules"}, null);
    }

    @Override
    public void executeCommand(MessageReceivedEvent e, String[] command) {
        String serverId = e.getGuild().getId();
        ArrayList<String> enabled = new ArrayList<>();
        ArrayList<String> disabled = new ArrayList<>();

        try {
            Connection connection = DatabaseConnection.getConnection();

            ResultSet resultSet = new DatabaseFunctions().getModuleSettings(connection, serverId);
            resultSet.next();

            for(int i = 2; i < Cache.MODULES.size(); i++) {
                ResultSetMetaData meta = resultSet.getMetaData();
                if(resultSet.getBoolean(i)) {
                    enabled.add(meta.getColumnName(i).substring(6));
                } else {
                    disabled.add(meta.getColumnName(i).substring(6));
                }
            }

            EmbedBuilder commandModules = new EmbedBuilder()
                    .setTitle("Below are the list of bot modules!")
                    .setDescription("Each module can be toggled on or off by using the " + Configuration.GLOBAL_PREFIX + "module <name> command.")
                    .addField("Enabled Modules", enabled.toString().replace(",","\n").replaceAll("[\\[\\] ]", "").toLowerCase(), false)
                    .addField("Disabled Modules", disabled.toString().replace(",","\n").replaceAll("[\\[\\] ]", "").toLowerCase(), false)
                    .setFooter(Configuration.VERSION, e.getGuild().getMemberById(Configuration.BOT_ID).getUser().getAvatarUrl());
            MessageHandler.sendMessage(e, commandModules.build());

            resultSet.close();
            connection.close();

        } catch(Exception ex) {
            Utils.sendException(ex, e.getMessage().getContentRaw());
        }

    }

}
