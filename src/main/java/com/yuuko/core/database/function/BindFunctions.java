package com.yuuko.core.database.function;

import com.yuuko.core.database.connection.DatabaseConnection;
import com.yuuko.core.utilities.TextUtilities;
import net.dv8tion.jda.api.entities.Guild;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BindFunctions {
    private static final Logger log = LoggerFactory.getLogger(BindFunctions.class);

    /**
     * Binds a particular module to a channel.
     * @param guildId the idLong of the guild.
     * @param channel the idLong of the channel.
     * @param module the name of the module.
     * @return boolean
     */
    public static int toggleBind(String guildId, String channel, String module) {
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM `module_bindings` WHERE `guildId` = ? AND `channelId` = ? AND `moduleName` = ?");
            PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO `module_bindings`(`guildId`, `channelId`, `moduleName`) VALUES (?,?,?)")) {

            stmt.setString(1, guildId);
            stmt.setString(2, channel);
            stmt.setString(3, module);
            ResultSet resultSet = stmt.executeQuery();

            if(!resultSet.next()) {
                stmt2.setString(1, guildId);
                stmt2.setString(2, channel);
                stmt2.setString(3, module);
                if(!stmt2.execute()) {
                    return 0;
                }
            }

            return (clearBind(guildId, channel, module)) ? 1 : -1;

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", BindFunctions.class.getSimpleName(), ex.getMessage(), ex);
            return -1;
        }
    }

    /**
     * Returns a formatted string of all of the selected guild's binds.
     * @param guild a guild object.
     * @param delimiter the delimiter used in the returned string.
     * @return String
     */
    public static String getGuildBinds(Guild guild, String delimiter) {
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM `module_bindings` WHERE `guildId` = ? ORDER BY `channelId`")) {

            stmt.setString(1, guild.getId());
            ResultSet rs = stmt.executeQuery();

            StringBuilder string = new StringBuilder();
            while(rs.next()) {
                string.append(rs.getString(3)).append(" : ").append(guild.getTextChannelCache().getElementById(rs.getString(2)).getAsMention()).append(" (").append(guild.getTextChannelCache().getElementById(rs.getString(2)).getId()).append(")").append(delimiter);
            }

            if(string.length() > 0) {
                TextUtilities.removeLast(string, delimiter);
            } else {
                string.append("None");
            }

            return string.toString();

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", BindFunctions.class.getSimpleName(), ex.getMessage(), ex);
            return null;
        }
    }

    /**
     * Returns a formatted string of all of the guild's binds, which match a given module.
     * @param guild Guild
     * @param module String
     * @param delimiter String
     * @return String
     */
    public static String getBindsByModule(Guild guild, String module, String delimiter) {
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM `module_bindings` WHERE (`guildId` = ? AND `moduleName` = ?) OR (`guildId` = ? AND `moduleName` = '*')")) {

            stmt.setString(1, guild.getId());
            stmt.setString(2, module);
            stmt.setString(3, guild.getId());

            ResultSet rs = stmt.executeQuery();

            StringBuilder string = new StringBuilder();
            while(rs.next()) {
                string.append(guild.getTextChannelCache().getElementById(rs.getString("channelId")).getAsMention()).append(delimiter);
            }

            if(string.length() > 0) {
                TextUtilities.removeLast(string, delimiter);
            } else {
                string.append("`None`");
            }

            return string.toString();

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", BindFunctions.class.getSimpleName(), ex.getMessage(), ex);
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
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM `module_bindings` WHERE (`guildId` = ? AND `moduleName` = ?) OR (`guildId` = ? AND `moduleName` = '*')")) {

            stmt.setString(1, guildId);
            stmt.setString(2, moduleName);
            stmt.setString(3, guildId);

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
            log.error("An error occurred while running the {} class, message: {}", BindFunctions.class.getSimpleName(), ex.getMessage(), ex);
            return false;
        }
    }

    /**
     * Verifies all binds, removing any that are no longer valid.
     * @param guild {@link Guild} object
     */
    public static void verifyBinds(Guild guild) {
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM `module_bindings` WHERE `guildId` = ?")) {

            stmt.setString(1, guild.getId());
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                String channelId = rs.getString("channelId");
                if(guild.getTextChannelCache().getElementById(channelId) == null) {
                    clearBindByChannel(channelId);
                }
            }

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", BindFunctions.class.getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Clears a module bind.
     * @param guild String
     * @param channel String
     * @param module String
     * @return boolean
     */
    private static boolean clearBind(String guild, String channel, String module) {
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM `module_bindings` WHERE `guildId` = ? AND `channelId` = ? AND `moduleName` = ?")) {

            stmt.setString(1, guild);
            stmt.setString(2, channel);
            stmt.setString(3, module);

            if(!stmt.execute()) {
                stmt.close();
                conn.close();
                return true;
            }

            return false;

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", BindFunctions.class.getSimpleName(), ex.getMessage(), ex);
            return false;
        }
    }

    /**
     * Clears all binds that are connected to a specific channelId
     * @param channelId channelId string.
     */
    private static void clearBindByChannel(String channelId) {
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM `module_bindings` WHERE `channelId` = ?")) {

            stmt.setString(1, channelId);
            stmt.execute();

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", BindFunctions.class.getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Removes all references of channels that are deleted.
     * @param channel String id of the channel referenced
     */
    public static void cleanupReferences(String channel) {
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM `module_bindings` WHERE `channelId` = ?");
            PreparedStatement stmt2 = conn.prepareStatement("UPDATE `guilds_settings` SET `starboard` = null WHERE 'starboard' = ?");
            PreparedStatement stmt3 = conn.prepareStatement("UPDATE `guilds_settings` SET `comLog` = null WHERE 'comLog' = ?");
            PreparedStatement stmt4 = conn.prepareStatement("UPDATE `guilds_settings` SET `modLog` = null WHERE 'modLog' = ?")) {

            stmt.setString(1, channel);
            stmt.execute();
            stmt2.setString(1, channel);
            stmt2.execute();
            stmt3.setString(1, channel);
            stmt3.execute();
            stmt4.setString(1, channel);
            stmt4.execute();

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", BindFunctions.class.getSimpleName(), ex.getMessage(), ex);
        }
    }
}
