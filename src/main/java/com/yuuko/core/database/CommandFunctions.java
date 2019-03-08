package com.yuuko.core.database;

import com.yuuko.core.database.connections.SettingsDatabaseConnection;
import com.yuuko.core.metrics.handlers.MetricsManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class CommandFunctions {

    private static final Logger log = LoggerFactory.getLogger(CommandFunctions.class);

    /**
     * Retrieves all of the command settings for a guild and returns them in an arrayList of an arrayList.
     *
     * @param guild the guild id.
     * @return ArrayList<ArrayList<String>>
     */
    public static ArrayList<String> getCommandSettings(String guild) {
        try(Connection conn = SettingsDatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM `CommandBindings` WHERE `guildId` = ?")) {

            stmt.setString(1, guild);
            ResultSet rs = stmt.executeQuery();

            MetricsManager.getDatabaseMetrics().SELECT.getAndIncrement();

            ArrayList<String> disabled = new ArrayList<>();

            int hits = 0;
            while(rs.next()) {
                String command = (rs.getString(3).equals("*")) ? "All" : rs.getString(3);
                String channel = (rs.getString(2).equals("*")) ? "Global" : rs.getString(2);
                disabled.add("`" + command + "` : " + channel );
                hits++;
            }

            return (hits == 0) ? new ArrayList<>() : disabled;

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", DatabaseFunctions.class.getSimpleName(), ex.getMessage(), ex);
            return new ArrayList<>();
        }
    }

    /**
     * Checks to see if a command is active before parsing a command.
     *
     * @param guild the guild id to check against.
     * @param channel the channel id to check against.
     * @param command the name of the command.
     * @return (boolean) if the module is active or not.
     */
    public static boolean isEnabled(String guild, String channel, String command) {
        try(Connection conn = SettingsDatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT `command` FROM `CommandBindings` WHERE `guildId` = ? AND `channelId` = ? AND `command` = ?")) {

            stmt.setString(1, guild);
            stmt.setString(2, channel);
            stmt.setString(3, command);
            ResultSet resultSet = stmt.executeQuery();

            MetricsManager.getDatabaseMetrics().SELECT.getAndIncrement();

            return resultSet.next();

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", DatabaseFunctions.class.getSimpleName(), ex.getMessage(), ex);
            return false;
        }
    }

    /**
     * Toggles a command for a guild, returns the new value.
     *
     * @param guild the guild in which the command is to be toggled.
     * @param channel the channel where the command will be disabled
     * @param command the command to toggle.
     * @return boolean.
     */
    public static boolean toggleCommand(String guild, String channel, String command) {
        try(Connection conn = SettingsDatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO `CommandBindings` (`guildId`, `channelId`, `command`) VALUES (?, ?, ?)");
            PreparedStatement stmt2 = conn.prepareStatement("DELETE FROM `CommandBindings` WHERE guildId = ? AND channelId = ? AND command = ?")) {

            if(! isEnabled(command, guild, channel)) {
                stmt.setString(1, guild);
                stmt.setString(2, channel);
                stmt.setString(3, command);
                stmt.execute();
                MetricsManager.getDatabaseMetrics().INSERT.getAndIncrement();
            } else {
                stmt2.setString(1, guild);
                stmt2.setString(2, channel);
                stmt2.setString(3, command);
                stmt2.execute();
                MetricsManager.getDatabaseMetrics().DELETE.getAndIncrement();
            }

            return ! isEnabled(command, guild, channel); // Returns inverse because command being enabled means that they ARENT found in the database.

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", DatabaseFunctions.class.getSimpleName(), ex.getMessage(), ex);
            return false;
        }
    }

    /**
     * Resets all of the command settings for a guild.
     *
     * @param guild the guild in which the settings are reset.
     */
    public static void resetCommandSettings(String guild) {
        try(Connection conn = SettingsDatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM `CommandBindings` WHERE guildId = ?")) {

            stmt.setString(1, guild);
            stmt.execute();
            MetricsManager.getDatabaseMetrics().DELETE.getAndIncrement();

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", DatabaseFunctions.class.getSimpleName(), ex.getMessage(), ex);
        }
    }
}
