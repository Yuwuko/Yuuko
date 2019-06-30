package com.yuuko.core.database.function;

import com.yuuko.core.database.connection.YuukoDatabaseConnection;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
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
     *
     * @param guild the guild to check.
     * @return boolean
     */
    private static boolean exists(String guild) {
        try(Connection conn = YuukoDatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM `Guilds` WHERE `guildId` = ?")) {

            stmt.setString(1, guild);
            ResultSet resultSet = stmt.executeQuery();
            return resultSet.next();

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", GuildFunctions.class.getSimpleName(), ex.getMessage(), ex);
        }

        return true;
    }

    /**
     * Adds a new guild to the database and initialises it's settings.
     *
     * @param guild Object of type Guild that is added to the database.
     */
    public static void addGuild(Guild guild) {
        try(Connection conn = YuukoDatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO `Guilds` (`guildId`, `guildName`, `guildRegion`, `memberCount`, `guildIcon`, `guildSplash`) VALUES (?, ?, ?, ?, ?, ?)");
            PreparedStatement stmt2 = conn.prepareStatement("UPDATE `Guilds` SET `guildName` = ?, `guildRegion` = ?, `memberCount` = ?, `guildIcon` = ?, `guildSplash` = ?, `lastSync` = CURRENT_TIMESTAMP WHERE `guildId` = ?")) {

            String guildId = guild.getId();
            String guildName = guild.getName();
            String guildRegion = guild.getRegion().getName();
            long memberCount = guild.getMemberCache().size();
            String guildIcon = guild.getIconUrl();
            String guildSplash = guild.getSplashUrl();

            // Encodes all server names to base64 to prevent special characters messing things up. (not for encryption)
            String encodedName = Base64.getEncoder().encodeToString(guildName.getBytes());

            if(!exists(guildId)) {
                stmt.setString(1, guildId);
                stmt.setString(2, encodedName);
                stmt.setString(3, guildRegion);
                stmt.setLong(4, memberCount);
                stmt.setString(5, guildIcon);
                stmt.setString(6, guildSplash);
                stmt.execute();

                log.info("Guild Synced: " + encodedName + " (" + guildId + ")");
            } else {
                stmt2.setString(1, encodedName);
                stmt2.setString(2, guildRegion);
                stmt2.setLong(3, memberCount);
                stmt2.setString(4, guildIcon);
                stmt2.setString(5, guildSplash);
                stmt2.setString(6, guildId);
                stmt2.execute();

            }

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", GuildFunctions.class.getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Adds a new guild to the database and initialises it's settings, or updates current guilds if they already exist on the database.
     *
     * @param jda JDA object to pull the guild cache from.
     * @return if the add was successful.
     */
    public static boolean addGuilds(JDA jda) {
        try {
            jda.getGuildCache().forEach(GuildFunctions::addGuild);
            return true;

        } catch (Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", GuildFunctions.class.getSimpleName(), ex.getMessage(), ex);
            return false;
        }
    }

    /**
     * Updates a guilds name in the database when it is changed.
     *
     * @param guildId String
     * @param guildName String
     */
    public static void updateGuildName(String guildId, String guildName) {
        try(Connection conn = YuukoDatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("UPDATE `Guilds` SET `guildName` = ? WHERE `guildId` = ?")) {

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
     * Updates a guilds region in the database when it is changed.
     *
     * @param guildId String
     * @param guildRegion String
     */
    public static void updateGuildRegion(String guildId, String guildRegion) {
        try(Connection conn = YuukoDatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("UPDATE `Guilds` SET `guildRegion` = ? WHERE `guildId` = ?")) {

            stmt.setString(1, guildRegion);
            stmt.setString(2, guildId);
            stmt.execute();

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", GuildFunctions.class.getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Updates a guilds region in the database when it is changed.
     *
     * @param guildId String
     * @param memberCount long
     */
    public static void updateMemberCount(String guildId, long memberCount) {
        try(Connection conn = YuukoDatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("UPDATE `Guilds` SET `memberCount` = ? WHERE `guildId` = ?")) {

            stmt.setLong(1, memberCount);
            stmt.setString(2, guildId);
            stmt.execute();

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", GuildFunctions.class.getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Updates a guilds region in the database when it is changed.
     *
     * @param guildId String
     * @param guildIcon String (url)
     */
    public static void updateGuildIcon(String guildId, String guildIcon) {
        try(Connection conn = YuukoDatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("UPDATE `Guilds` SET `guildIcon` = ? WHERE `guildId` = ?")) {

            stmt.setString(1, guildIcon);
            stmt.setString(2, guildId);
            stmt.execute();

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", GuildFunctions.class.getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Updates a guilds region in the database when it is changed.
     *
     * @param guildId String
     * @param guildSplash String
     */
    public static void updateGuildSplash(String guildId, String guildSplash) {
        try(Connection conn = YuukoDatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("UPDATE `Guilds` SET `guildSplash` = ? WHERE `guildId` = ?")) {

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
     *
     * @param guild the guild to get the settings for.
     * @return ResultSet
     */
    public static ArrayList<String> getGuildSettings(String guild) {
        try(Connection conn = YuukoDatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM `GuildSettings` WHERE `guildId` = ?")) {

            stmt.setString(1, guild);
            ResultSet rs = stmt.executeQuery();

            ArrayList<String> settings = new ArrayList<>();

            while(rs.next()) {
                settings.add(rs.getString("prefix"));
                settings.add(rs.getBoolean("deleteExecuted") ? "Enabled" : "Disabled");
                settings.add(rs.getBoolean("nowPlaying") ? "Enabled" : "Disabled");
                settings.add(rs.getBoolean("djMode") ? "Enabled" : "Disabled");
                settings.add(rs.getString("newMember"));
                settings.add(rs.getString("newMemberMessage"));
                settings.add(rs.getString("starboard"));
                settings.add(rs.getString("comLog"));
                settings.add(rs.getString("modLog"));
            }


            return settings;

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", GuildFunctions.class.getSimpleName(), ex.getMessage(), ex);
            return new ArrayList<>();
        }
    }

    /**
     * Returns the value of a single guild settings.
     *
     * @param setting the setting to be checked
     * @param guild the guild to check the setting for
     * @return String
     */
    public static String getGuildSetting(String setting, String guild) {
        try(Connection conn = YuukoDatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT `" + setting + "` FROM `GuildSettings` WHERE `guildId` = ?")) {

            stmt.setString(1, guild);
            ResultSet resultSet = stmt.executeQuery();

            return resultSet.next() ? resultSet.getString(1) : null;

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", GuildFunctions.class.getSimpleName(), ex.getMessage(), ex);
            return null;
        }
    }

    /**
     * Changes a setting value for the given guild setting. (Very dangerous without the correct checking...)
     *
     * @param setting the setting to be changed.
     * @param value the value of the setting being changed.
     * @param guild the guild where the setting will be changed.
     * @return if the set was successful.
     */
    public static boolean setGuildSettings(String setting, String value, String guild) {
        try(Connection conn = YuukoDatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("UPDATE `GuildSettings` SET `" + setting + "` = ? WHERE `guildId` = ?")) {

            stmt.setString(1, value);
            stmt.setString(2, guild);

            return !stmt.execute();

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", GuildFunctions.class.getSimpleName(), ex.getMessage(), ex);
            return false;
        }
    }

    /**
     * Set the guild invite link to allow advertising.
     *
     * @param link invite url to be advertised.
     * @param guild the guild attached to the link.
     * @return if the set was successful.
     */
    public static boolean setGuildInvite(String link, String guild) {
        try(Connection conn = YuukoDatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("UPDATE `Guilds` SET `inviteLink` = ? WHERE `guildId` = ?")) {

            stmt.setString(1, link);
            stmt.setString(2, guild);

            return !stmt.execute();

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", GuildFunctions.class.getSimpleName(), ex.getMessage(), ex);
            return false;
        }
    }

    /**
     * Cleans up any guild's that ask the bot to leave. (Uses CASCADE)
     *
     * @param guild the guild's id.
     */
    public static void cleanup(String guild) {
        try(Connection conn = YuukoDatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM `Guilds` WHERE `guildId` = ?")) {

            stmt.setString(1, guild);
            stmt.execute();

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", GuildFunctions.class.getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Cleans up any guild's that didn't get synced within 24 hours of the last startup phase.
     *
     * @return boolean if the purge was successful.
     */
    public static boolean purgeGuilds() {
        try(Connection conn = YuukoDatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM `Guilds` WHERE `lastSync` < DATE_SUB(NOW(), INTERVAL 24 HOUR)")) {

            stmt.execute();
            return true;

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", GuildFunctions.class.getSimpleName(), ex.getMessage(), ex);
            return false;
        }
    }


}
