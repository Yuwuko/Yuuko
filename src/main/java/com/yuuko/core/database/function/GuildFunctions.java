package com.yuuko.core.database.function;

import com.yuuko.core.database.connection.DatabaseConnection;
import net.dv8tion.jda.api.entities.Guild;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Base64;

public class GuildFunctions {
    private static final Logger log = LoggerFactory.getLogger(GuildFunctions.class);

    /**
     * Small method that checks if a guild exists on the database.
     * @param guild the guild to check.
     * @return boolean
     */
    private static boolean exists(String guild) {
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM `guilds` WHERE `guildId` = ?")) {

            stmt.setString(1, guild);
            ResultSet resultSet = stmt.executeQuery();
            return resultSet.next();

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", GuildFunctions.class.getSimpleName(), ex.getMessage(), ex);
        }

        return true;
    }

    /**
     * Adds or updates a guild to/on the database.
     * @param guild {@link Guild} that is added/updated to/on the database.
     */
    public static void addOrUpdateGuild(Guild guild) {
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("INSERT IGNORE INTO `guilds` (`guildId`) VALUES (?)");
            PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO `guilds_data` (`guildId`, `guildName`, `guildRegion`, `guildIcon`, `guildSplash`) VALUES (?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE `guildName` = VALUES(`guildName`), `guildRegion` = VALUES(`guildRegion`), `guildIcon` = VALUES(`guildIcon`), `guildSplash` = VALUES(`guildSplash`), `lastUpdated` = CURRENT_TIMESTAMP");
            PreparedStatement stmt3 = conn.prepareStatement("INSERT IGNORE INTO `guilds_settings` (`guildId`) VALUES (?)");
            PreparedStatement stmt4 = conn.prepareStatement("INSERT IGNORE INTO `guilds_module_settings` (`guildId`) VALUES (?)")) {

            stmt.setString(1, guild.getId());
            if(stmt.execute()) {
                log.info("Guild added: " + guild.getName() + " (" + guild.getId() + ")");
            }

            stmt2.setString(1, guild.getId());
            stmt2.setString(2, guild.getName());
            stmt2.setString(3, guild.getRegion().getName());
            stmt2.setString(4, guild.getIconUrl());
            stmt2.setString(5, guild.getSplashUrl());
            stmt2.execute();

            stmt3.setString(1, guild.getId());
            stmt3.execute();

            stmt4.setString(1, guild.getId());
            stmt4.execute();

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", GuildFunctions.class.getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Updates a guilds name in the database when it is changed.
     * @param guildId String
     * @param guildName String
     */
    public static void updateGuildName(String guildId, String guildName) {
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("UPDATE `guilds_data` SET `guildName` = ? WHERE `guildId` = ?")) {

            // Encodes all server names to base64 to prevent special characters messing things up. (not for encryption)
            String encodedName = Base64.getEncoder().encodeToString(guildName.getBytes());

            stmt.setString(1, encodedName);
            stmt.setString(2, guildId);
            stmt.execute();

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", GuildFunctions.class.getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Update guild region.
     * @param guildId String
     * @param guildRegion String
     */
    public static void updateGuildRegion(String guildId, String guildRegion) {
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("UPDATE `guilds_data` SET `guildRegion` = ? WHERE `guildId` = ?")) {

            stmt.setString(1, guildRegion);
            stmt.setString(2, guildId);
            stmt.execute();

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", GuildFunctions.class.getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Update guild icon url.
     * @param guildId String
     * @param guildIcon String (url)
     */
    public static void updateGuildIcon(String guildId, String guildIcon) {
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("UPDATE `guilds_data` SET `guildIcon` = ? WHERE `guildId` = ?")) {

            stmt.setString(1, guildIcon);
            stmt.setString(2, guildId);
            stmt.execute();

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", GuildFunctions.class.getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Update guild splash url.
     * @param guildId String
     * @param guildSplash String
     */
    public static void updateGuildSplash(String guildId, String guildSplash) {
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("UPDATE `guilds_data` SET `guildSplash` = ? WHERE `guildId` = ?")) {

            stmt.setString(1, guildSplash);
            stmt.setString(2, guildId);
            stmt.execute();


        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", GuildFunctions.class.getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Returns all of the guild settings for the given guild.
     * ** Doesn't close connection or resultset is lost **
     * @param guildId the guild to get the settings for.
     * @return ResultSet
     */
    public static ArrayList<String> getGuildSettings(String guildId) {
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM `guilds_settings` WHERE `guildId` = ?")) {

            stmt.setString(1, guildId);
            ResultSet rs = stmt.executeQuery();

            ArrayList<String> settings = new ArrayList<>();
            while(rs.next()) {
                settings.add(rs.getString("prefix"));
                settings.add(rs.getBoolean("cleanupcommands") ? "Enabled" : "Disabled");
                settings.add(rs.getBoolean("playnotifications") ? "Enabled" : "Disabled");
                settings.add(rs.getBoolean("djmode") ? "Enabled" : "Disabled");
                settings.add(rs.getString("starboard"));
                settings.add(rs.getString("commandlog"));
                settings.add(rs.getString("moderationlog"));
                settings.add(rs.getString("eventchannel"));
            }

            return settings;

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", GuildFunctions.class.getSimpleName(), ex.getMessage(), ex);
            return new ArrayList<>();
        }
    }

    /**
     * Returns the value of a single guild settings.
     * @param setting the setting to be checked
     * @param guild the guild to check the setting for
     * @return String
     */
    public static String getGuildSetting(String setting, String guild) {
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT `" + setting + "` FROM `guilds_settings` WHERE `guildId` = ?")) {

            stmt.setString(1, guild);
            ResultSet resultSet = stmt.executeQuery();

            return resultSet.next() ? resultSet.getString(1) : null;

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", GuildFunctions.class.getSimpleName(), ex.getMessage(), ex);
            return null;
        }
    }

    /**
     * Returns the boolean value of a single guild settings.
     * @param setting the setting to be checked
     * @param guild the guild to check the setting for
     * @return boolean
     */
    public static boolean getGuildSettingBoolean(String setting, String guild) {
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT `" + setting + "` FROM `guilds_settings` WHERE `guildId` = ?")) {

            stmt.setString(1, guild);
            ResultSet resultSet = stmt.executeQuery();

            return resultSet.next() && resultSet.getBoolean(1);

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", GuildFunctions.class.getSimpleName(), ex.getMessage(), ex);
            return false;
        }
    }

    /**
     * Changes a setting value for the given guild setting. (Very dangerous without the correct checking...)
     * @param setting the setting to be changed.
     * @param value the value of the setting being changed.
     * @param guild the guild where the setting will be changed.
     * @return if the set was successful.
     */
    public static boolean setGuildSettings(String setting, String value, String guild) {
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("UPDATE `guilds_settings` SET `" + setting + "` = ? WHERE `guildId` = ?")) {

            stmt.setString(1, value);
            stmt.setString(2, guild);

            return !stmt.execute();

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", GuildFunctions.class.getSimpleName(), ex.getMessage(), ex);
            return false;
        }
    }

    /**
     * Updates settings from channels that are deleted.
     * @param setting the setting to cleanup.
     * @param guildId the guild to cleanup.
     */
    public static void cleanupSetting(String setting, String guildId) {
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("UPDATE `guilds_settings` SET " + setting + " = null WHERE `guildId` = ?")){

            stmt.setString(1, guildId);
            stmt.execute();

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", ShardFunctions.class.getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Cleans up any guild's that ask the bot to leave. (Uses CASCADE)
     * @param guild the guild's id.
     */
    public static void cleanup(String guild) {
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM `guilds` WHERE `guildId` = ?")) {

            stmt.setString(1, guild);
            stmt.execute();

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", GuildFunctions.class.getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Cleans up any guild's that didn't get synced within 24 hours of the last startup phase.
     * @return boolean if the purge was successful.
     */
    public static void purgeGuilds() {
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT `guildId` FROM `guilds_data` WHERE `lastUpdated` < DATE_SUB(NOW(), INTERVAL 24 HOUR)")) {

            ResultSet resultSet = stmt.executeQuery();
            while(resultSet.next()) {
                cleanup(resultSet.getString("guildId"));
            }

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", GuildFunctions.class.getSimpleName(), ex.getMessage(), ex);
        }
    }

}
