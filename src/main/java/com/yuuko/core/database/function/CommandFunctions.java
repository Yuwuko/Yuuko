package com.yuuko.core.database.function;

import com.yuuko.core.commands.Command;
import com.yuuko.core.database.connection.SettingsDatabaseConnection;
import net.dv8tion.jda.core.entities.Guild;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class CommandFunctions {

    private static final Logger log = LoggerFactory.getLogger(CommandFunctions.class);

    /**
     * Retrieves the command settings for a guild and particular command and returns them in an arrayList of an arrayList.
     *
     * @param guild the guild id.
     * @param command the command to get the settings for.
     * @return ArrayList<String>
     */
    public static ArrayList<String> getCommandSetting(Guild guild, Command command) {
        try(Connection conn = SettingsDatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM `CommandBindings` WHERE `guildId` = ? AND `command` = ?")) {

            stmt.setString(1, guild.getId());
            stmt.setString(2, command.getName());
            ResultSet rs = stmt.executeQuery();

            ArrayList<String> disabled = new ArrayList<>();

            int hits = 0;
            while(rs.next()) {
                String cmd = (rs.getString(3).equals("*")) ? "All" : rs.getString(3);
                String channel = (rs.getString(2).equals("*")) ? "Global" : rs.getString(2);
                disabled.add("`" + cmd + "` : " + channel );
                hits++;
            }

            return (hits == 0) ? new ArrayList<>() : disabled;

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", DatabaseFunctions.class.getSimpleName(), ex.getMessage(), ex);
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves all of the command settings for a guild and returns them in an arrayList.
     *
     * @param guild the guild id.
     * @return ArrayList<String>
     */
    public static ArrayList<String> getCommandSettings(String guild) {
        try(Connection conn = SettingsDatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM `CommandBindings` WHERE `guildId` = ?")) {

            stmt.setString(1, guild);
            ResultSet rs = stmt.executeQuery();

            ArrayList<String> disabled = new ArrayList<>();

            int hits = 0;
            while(rs.next()) {
                String cmd = (rs.getString(3).equals("*")) ? "All" : rs.getString(3);
                String channel = (rs.getString(2).equals("*")) ? "Global" : rs.getString(2);
                disabled.add("`" + cmd + "` : " + channel );
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
    public static boolean isDisabled(String guild, String channel, String command) {
        try(Connection conn = SettingsDatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT `command` FROM `CommandBindings` WHERE `guildId` = ? AND `channelId` = ? AND `command` = ?")) {

            stmt.setString(1, guild);
            stmt.setString(2, channel);
            stmt.setString(3, command);
            ResultSet resultSet = stmt.executeQuery();

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

            if(!isDisabled(guild, channel, command)) {
                stmt.setString(1, guild);
                stmt.setString(2, channel);
                stmt.setString(3, command);
                stmt.execute();
            } else {
                stmt2.setString(1, guild);
                stmt2.setString(2, channel);
                stmt2.setString(3, command);
                stmt2.execute();
            }

            return !isDisabled(guild, channel, command); // Returns inverse because command being enabled means that they ARE'NT found in the database.

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
    public static void resetCommandSettings(Guild guild, String command) {
        try(Connection conn = SettingsDatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM `CommandBindings` WHERE guildId = ?");
            PreparedStatement stmt2 = conn.prepareStatement("DELETE FROM `CommandBindings` WHERE guildId = ? AND command = ?")) {

            if(command == null) {
                stmt.setString(1, guild.getId());
                stmt.execute();
            } else {
                stmt2.setString(1, guild.getId());
                stmt2.setString(2, command);
                stmt2.execute();
            }

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", DatabaseFunctions.class.getSimpleName(), ex.getMessage(), ex);
        }
    }
}
