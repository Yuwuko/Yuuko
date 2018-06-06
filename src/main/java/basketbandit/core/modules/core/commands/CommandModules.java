package basketbandit.core.modules.core.commands;

import basketbandit.core.Configuration;
import basketbandit.core.database.DatabaseConnection;
import basketbandit.core.database.DatabaseFunctions;
import basketbandit.core.modules.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;

public class CommandModules extends Command {

    public CommandModules() {
        super("modules", "basketbandit.core.modules.core.ModuleCore", null);
    }

    public CommandModules(MessageReceivedEvent e, String[] command) {
        super("modules", "basketbandit.core.modules.core.ModuleCore", null);
        executeCommand(e, command);
    }

    /**
     * Executes command using MessageReceivedEvent e.
     * @param e; MessageReceivedEvent.
     * @return boolean; if the command executed correctly.
     */
    protected void executeCommand(MessageReceivedEvent e, String[] command) {
        String serverId = e.getGuild().getId();
        ArrayList<String> enabled = new ArrayList<>();
        ArrayList<String> disabled = new ArrayList<>();
        Connection connection = new DatabaseConnection().getConnection();
        ResultSet resultSet;

        try {
            resultSet = new DatabaseFunctions().getModuleSettings(connection, serverId);
            resultSet.next();

            for(int i = 2; i < 9; i++) {
                ResultSetMetaData meta = resultSet.getMetaData();
                if(resultSet.getBoolean(i)) {
                    enabled.add(meta.getColumnName(i).substring(6));
                } else {
                    disabled.add(meta.getColumnName(i).substring(6));
                }
            }

            EmbedBuilder commandModules = new EmbedBuilder()
                    .setColor(Color.RED)
                    .setAuthor("Hey " + e.getAuthor().getName() + ",",null,e.getAuthor().getAvatarUrl())
                    .setTitle("Below are the list of bot modules!")
                    .setDescription("Each module can be toggled on or off by using the " + Configuration.GLOBAL_PREFIX + "module <name> command.")
                    .addField("Enabled Modules", enabled.toString().replace(",","\n").replaceAll("[\\[\\] ]", "").toLowerCase(), false)
                    .addField("Disabled Modules", disabled.toString().replace(",","\n").replaceAll("[\\[\\] ]", "").toLowerCase(), false)
                    .setFooter("Version: " + Configuration.VERSION, e.getGuild().getMemberById(Configuration.BOT_ID).getUser().getAvatarUrl());

            e.getTextChannel().sendMessage(commandModules.build()).queue();
        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch(Exception ex) {
                System.out.println("[ERROR] Unable to close connection to database.");
            }
        }

    }

}
