package com.yuuko.core.database;

import com.yuuko.core.database.connections.SettingsDatabaseConnection;
import com.yuuko.core.metrics.handlers.MetricsManager;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.utils.cache.SnowflakeCacheView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class GuildFunctions {

    private static final Logger log = LoggerFactory.getLogger(GuildFunctions.class);

    /**
     * Small method that checks if a guild exists on the database.
     *
     * @param guild the guild to check.
     * @return boolean
     */
    private static boolean exists(String guild) {
        try(Connection conn = SettingsDatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM `Guilds` WHERE `guildId` = ?")) {

            stmt.setString(1, guild);
            ResultSet resultSet = stmt.executeQuery();

            MetricsManager.getDatabaseMetrics().SELECT.getAndIncrement();

            return resultSet.next();

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", DatabaseFunctions.class.getSimpleName(), ex.getMessage(), ex);
        }
        return true;
    }

    /**
     * Adds a new guild to the database and initialises it's settings.
     *
     * @param guildId the guild to add.
     * @param guildName the name of the guild.
     * @param guildRegion the region of the guild.
     * @return if the add was successful.
     */
    public static boolean addNewGuild(String guildId, String guildName, String guildRegion) {
        try(Connection conn = SettingsDatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO `Guilds` (`guildId`, `guildName`, `guildRegion`) VALUES (?, ?, ?)");
            PreparedStatement stmt2 = conn.prepareStatement("UPDATE `Guilds` SET `guildName` = ?, `guildRegion` = ? WHERE `guildId` = ?")) {

            if(!exists(guildId)) {
                stmt.setString(1, guildId);
                stmt.setString(2, guildName);
                stmt.setString(3, guildRegion);
                stmt.execute();

                MetricsManager.getDatabaseMetrics().INSERT.getAndIncrement();
            } else {
                stmt2.setString(1, guildName);
                stmt2.setString(2, guildRegion);
                stmt2.setString(3, guildId);
                stmt2.execute();

                MetricsManager.getDatabaseMetrics().UPDATE.getAndIncrement();
            }

            return true;

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", DatabaseFunctions.class.getSimpleName(), ex.getMessage(), ex);
            return false;
        }
    }

    /**
     * Adds a new guild to the database and initialises it's settings, or updates current guilds if they already exist on the database.
     *
     * @param e MessageReceivedEvent.
     * @return if the add was successful.
     */
    public static boolean addGuilds(MessageReceivedEvent e) {
        try {
            SnowflakeCacheView guilds = e.getJDA().getGuildCache();

            for(Object guild : guilds) {
                Guild matchedGuild = (Guild) guild;
                if(addNewGuild(matchedGuild.getId(), matchedGuild.getName(), matchedGuild.getRegion().getName())) {
                    log.info("Guild Added: " + matchedGuild.getId());
                }
            }

            return true;

        } catch (Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", DatabaseFunctions.class.getSimpleName(), ex.getMessage(), ex);
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
        try(Connection conn = SettingsDatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("UPDATE `Guilds` SET `guildName` = ? WHERE `guildId` = ?")) {

            stmt.setString(1, guildName);
            stmt.setString(2, guildId);
            stmt.execute();

            MetricsManager.getDatabaseMetrics().UPDATE.getAndIncrement();

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", DatabaseFunctions.class.getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Updates a guilds region in the database when it is changed.
     *
     * @param guildId String
     * @param guildRegion String
     */
    public static void updateGuildRegion(String guildId, String guildRegion) {
        try(Connection conn = SettingsDatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("UPDATE `Guilds` SET `guildRegion` = ? WHERE `guildId` = ?")) {

            stmt.setString(1, guildRegion);
            stmt.setString(2, guildId);
            stmt.execute();

            MetricsManager.getDatabaseMetrics().UPDATE.getAndIncrement();

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", DatabaseFunctions.class.getSimpleName(), ex.getMessage(), ex);
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
        try(Connection conn = SettingsDatabaseConnection.getConnection();
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
                settings.add(rs.getString("starboard"));
                settings.add(rs.getString("commandLog"));
                settings.add(rs.getString("modLog"));
            }

            MetricsManager.getDatabaseMetrics().SELECT.getAndIncrement();

            return settings;

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", DatabaseFunctions.class.getSimpleName(), ex.getMessage(), ex);
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
        try(Connection conn = SettingsDatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT `" + setting + "` FROM `GuildSettings` WHERE `guildId` = ?")) {

            stmt.setString(1, guild);
            ResultSet resultSet = stmt.executeQuery();

            MetricsManager.getDatabaseMetrics().SELECT.getAndIncrement();

            return resultSet.next() ? resultSet.getString(1) : null;

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", DatabaseFunctions.class.getSimpleName(), ex.getMessage(), ex);
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
        try(Connection conn = SettingsDatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("UPDATE `GuildSettings` SET `" + setting + "` = ? WHERE `guildId` = ?")) {

            MetricsManager.getDatabaseMetrics().UPDATE.getAndIncrement();

            stmt.setString(1, value);
            stmt.setString(2, guild);

            return !stmt.execute();

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", DatabaseFunctions.class.getSimpleName(), ex.getMessage(), ex);
            return false;
        }
    }
}
