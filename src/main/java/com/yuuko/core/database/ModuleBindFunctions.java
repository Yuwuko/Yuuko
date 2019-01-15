package com.yuuko.core.database;

import com.yuuko.core.database.connections.DatabaseConnection;
import com.yuuko.core.metrics.handlers.MetricsManager;
import com.yuuko.core.utilities.TextUtility;
import net.dv8tion.jda.core.entities.Guild;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ModuleBindFunctions {

    private static final Logger log = LoggerFactory.getLogger(ModuleBindFunctions.class);

    /**
     * Binds a particular module to a channel.
     * @param guildId the idLong of the guild.
     * @param channel the idLong of the channel.
     * @param moduleName the name of the module.
     * @return boolean
     */
    public static int toggleBind(String guildId, String channel, String moduleName) {
        try {
            MetricsManager.getDatabaseMetrics().SELECT.getAndIncrement();

            moduleName = TextUtility.extractModuleName(moduleName, true, false); // Sometimes the input will be the whole classpath, this removes that junk and returns just the module name.

            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM `ModuleBindings` WHERE `guildId` = ? AND `channelId` = ? AND `moduleName` = ?");
            stmt.setString(1, guildId);
            stmt.setString(2, channel);
            stmt.setString(3, moduleName);
            ResultSet resultSet = stmt.executeQuery();

            if(!resultSet.next()) {
                PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO `ModuleBindings`(`guildId`, `channelId`, `moduleName`) VALUES (?,?,?)");
                stmt2.setString(1, guildId);
                stmt2.setString(2, channel);
                stmt2.setString(3, moduleName);
                if(!stmt2.execute()) {
                    stmt2.close();
                    conn.close();
                    MetricsManager.getDatabaseMetrics().INSERT.getAndIncrement();
                    return 0;
                }
            }

            stmt.close();
            conn.close();
            return (deleteBindsRecord(guildId, channel, moduleName)) ? 1 : -1;

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", ModuleBindFunctions.class.getSimpleName(), ex.getMessage(), ex);
            return -1;
        }
    }

    /**
     * Removes a binding record from the database.
     * @param guild String
     * @param channel String
     * @param moduleName String
     * @return int
     */
    private static boolean deleteBindsRecord(String guild, String channel, String moduleName) {
        try {
            moduleName = TextUtility.extractModuleName(moduleName, true, false);

            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM `ModuleBindings` WHERE `guildId` = ? AND `channelId` = ? AND `moduleName` = ?");
            stmt.setString(1, guild);
            stmt.setString(2, channel);
            stmt.setString(3, moduleName);
            if(!stmt.execute()) {
                stmt.close();
                conn.close();
                MetricsManager.getDatabaseMetrics().DELETE.getAndIncrement();
                return true;
            }

            stmt.close();
            conn.close();
            return false;

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", ModuleBindFunctions.class.getSimpleName(), ex.getMessage(), ex);
            return false;
        }
    }


    /**
     * Returns a formatted string of all of the selected guild's binds.
     * @param guild a guild object.
     * @param delimiter the delimiter used in the returned string.
     * @return String
     */
    public static String getGuildBinds(Guild guild, String delimiter) {
        try {
            MetricsManager.getDatabaseMetrics().SELECT.getAndIncrement();

            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM `ModuleBindings` WHERE `guildId` = ? ORDER BY `channelId` ASC");
            stmt.setString(1, guild.getId());

            ResultSet rs = stmt.executeQuery();

            StringBuilder string = new StringBuilder();
            while(rs.next()) {
                string.append(rs.getString(3)).append(" : ").append(guild.getTextChannelCache().getElementById(rs.getString(2)).getAsMention()).append(" (").append(guild.getTextChannelCache().getElementById(rs.getString(2)).getId()).append(")").append(delimiter);
            }

            if(string.length() > 0) {
                TextUtility.removeLastOccurrence(string, delimiter);
            } else {
                string.append("None");
            }

            connection.close();

            return string.toString();

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", ModuleBindFunctions.class.getSimpleName(), ex.getMessage(), ex);
            return null;
        }
    }

    /**
     * Returns a formatted string of all of the guild's binds, which match a given module.
     * @param guild Guild
     * @param moduleName String
     * @param delimiter String
     * @return String
     */
    public static String getBindsByModule(Guild guild, String moduleName, String delimiter) {
        try {
            MetricsManager.getDatabaseMetrics().SELECT.getAndIncrement();

            moduleName = TextUtility.extractModuleName(moduleName, true, false);

            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM `ModuleBindings` WHERE `guildId` = ? AND `moduleName` = ?");
            stmt.setString(1, guild.getId());
            stmt.setString(2, moduleName);

            ResultSet rs = stmt.executeQuery();

            StringBuilder string = new StringBuilder();
            while(rs.next()) {
                string.append(guild.getTextChannelCache().getElementById(rs.getString(2)).getAsMention()).append(delimiter);
            }

            if(string.length() > 0) {
                TextUtility.removeLastOccurrence(string, delimiter);
            } else {
                string.append("None");
            }

            connection.close();

            return string.toString();

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", ModuleBindFunctions.class.getSimpleName(), ex.getMessage(), ex);
            return null;
        }
    }

    /**
     * Checks to see if a bind for a certain channel/module combination exists.
     * @param guildId String
     * @param channelId String
     * @param moduleName String
     * @return boolean
     */
    public static boolean checkBind(String guildId, String channelId, String moduleName) {
        try {
            MetricsManager.getDatabaseMetrics().SELECT.getAndIncrement();

            moduleName = TextUtility.extractModuleName(moduleName, true, false);

            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM `ModuleBindings` WHERE `guildId` = ? AND `moduleName` = ?");
            stmt.setString(1, guildId);
            stmt.setString(2, moduleName);


            ResultSet rs = stmt.executeQuery();
            int count = 0;
            while(rs.next()) {
                if(rs.getString("channelId").equals(channelId)) {
                    return true;
                }
                count++;
            }

            return count < 1;

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", ModuleBindFunctions.class.getSimpleName(), ex.getMessage(), ex);
            return false;
        }
    }

    /**
     * Removes binding from channels that are deleted.
     * @param channel the channel to clean up.
     */
    public static void cleanupBinds(String channel) {
        try {
            MetricsManager.getDatabaseMetrics().DELETE.getAndIncrement();

            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM `ModuleBindings` WHERE `channelId` = ?");
            stmt.setString(1, channel);
            stmt.execute();

            stmt.close();
            conn.close();

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", ModuleBindFunctions.class.getSimpleName(), ex.getMessage(), ex);
        }
    }
}
