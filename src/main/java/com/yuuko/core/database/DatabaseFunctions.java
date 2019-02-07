package com.yuuko.core.database;

import com.yuuko.core.Cache;
import com.yuuko.core.database.connections.MetricsDatabaseConnection;
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
import java.sql.ResultSetMetaData;
import java.util.ArrayList;


@SuppressWarnings("ALL")
public class DatabaseFunctions {

    private static final Logger log = LoggerFactory.getLogger(DatabaseFunctions.class);

    /**
     * Small method that checks if a guild exists on the database.
     * @param guild the guild to check.
     * @return boolean
     */
    private static boolean exists(String guild) {
        try(Connection conn = SettingsDatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM `Guilds` WHERE `guildId` = ?");) {

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
     * @param guild the guild to add.
     * @return if the add was successful.
     */
    public static boolean addNewGuild(String guildId, String guildName, String guildRegion) {
        try(Connection conn = SettingsDatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO `Guilds` (`guildId`, `guildName`, `guildRegion`) VALUES (?, ?, ?)");
            PreparedStatement stmt2 = conn.prepareStatement("UPDATE `Guilds` SET `guildName` = ?, `guildRegion` = ? WHERE `guildId` = ?");) {

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
     * @param e MessageReceivedEvent.
     * @return if the add was successful.
     */
    public static boolean addGuilds(MessageReceivedEvent e) {
        try {
            SnowflakeCacheView guilds = e.getJDA().getGuildCache();

            for(Object guild : guilds) {
                Guild matchedGuild = (Guild) guild;
                addNewGuild(matchedGuild.getId(), matchedGuild.getName(), matchedGuild.getRegion().getName());
            }

            return true;

        } catch (Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", DatabaseFunctions.class.getSimpleName(), ex.getMessage(), ex);
            return false;
        }
    }

    /**
     * Updates a guilds name in the database when it is changed.
     * @param guildId String
     * @param guildName String
     */
    public static void updateGuildName(String guildId, String guildName) {
        try(Connection conn = SettingsDatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("UPDATE `Guilds` SET `guildName` = ? WHERE `guildId` = ?");) {



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
     * @param guildId String
     * @param guildRegion String
     */
    public static void updateGuildRegion(String guildId, String guildRegion) {
        try(Connection conn = SettingsDatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("UPDATE `Guilds` SET `guildRegion` = ? WHERE `guildId` = ?");) {

            stmt.setString(1, guildRegion);
            stmt.setString(2, guildId);
            stmt.execute();

            MetricsManager.getDatabaseMetrics().UPDATE.getAndIncrement();

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", DatabaseFunctions.class.getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Retrieves all of the module settings for a guild and returns them in an arrayList of an arrayList.
     * @param guild the guild id.
     * @return ArrayList<ArrayList<String>>
     */
    public static ArrayList<ArrayList<String>> getModuleSettings(String guildId) {
        try(Connection conn = SettingsDatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM `ModuleSettings` WHERE `guildId` = ?");) {

            stmt.setString(1, guildId);
            ResultSet rs = stmt.executeQuery();
            ResultSetMetaData meta = rs.getMetaData();

            ArrayList<ArrayList<String>> arrayLists = new ArrayList<>();
            ArrayList<String> enabled = new ArrayList<>();
            ArrayList<String> disabled = new ArrayList<>();

            if(rs.next()) {
                for(int i = 2; i < Cache.MODULES.size(); i++) {
                    if(rs.getBoolean(i)) {
                        enabled.add(meta.getColumnName(i).substring(6));
                    } else {
                        disabled.add(meta.getColumnName(i).substring(6));
                    }
                }
            } else {
                return new ArrayList<>();
            }

            arrayLists.add(enabled);
            arrayLists.add(disabled);

            MetricsManager.getDatabaseMetrics().SELECT.getAndIncrement();

            return arrayLists;
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", DatabaseFunctions.class.getSimpleName(), ex.getMessage(), ex);
            return new ArrayList<>();
        }
    }

    /**
     * Checks to see if a module is active before parsing a command.
     * @param moduleName the name of the module.
     * @return (boolean) if the module is active or not.
     */
    public static boolean checkModuleSettings(String moduleName, String guild) {
        try(Connection conn = SettingsDatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT `" + moduleName + "` FROM `ModuleSettings` WHERE `guildId` = ?");) {

            stmt.setString(1, guild);
            ResultSet resultSet = stmt.executeQuery();

            MetricsManager.getDatabaseMetrics().SELECT.getAndIncrement();

            return resultSet.next() ? resultSet.getBoolean(1) : false;

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", DatabaseFunctions.class.getSimpleName(), ex.getMessage(), ex);
            return false;
        }
    }

    /**
     * Toggles a module for a guild, returns the new value.
     * @param moduleIn the module to toggle.
     * @param guild the guild in which the module is to be toggled.
     * @return boolean.
     */
    public static boolean toggleModule(String moduleIn, String guild) {
        try(Connection conn = SettingsDatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("UPDATE `ModuleSettings` SET `" + moduleIn + "` = NOT `" + moduleIn + "` WHERE `guildId` = ?");) {

            stmt.setString(1, guild);
            stmt.execute();

            MetricsManager.getDatabaseMetrics().UPDATE.getAndIncrement();

            return checkModuleSettings(moduleIn, guild);

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", DatabaseFunctions.class.getSimpleName(), ex.getMessage(), ex);
            return false;
        }
    }

    /**
     * Returns all of the guild settings for the given guild.
     * ** Doesn't close connection or resultset is lost **
     * @param connection the database connection used.
     * @param guild the guild to get the settings for.
     * @return ResultSet
     */
    public static ArrayList<String> getGuildSettings(String guild) {
        try(Connection conn = SettingsDatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM `GuildSettings` WHERE `guildId` = ?");) {

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
     * @param setting the setting to be checked
     * @param guild the guild to check the setting for
     * @return String
     */
    public static String getGuildSetting(String setting, String guild) {
        try(Connection conn = SettingsDatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT `" + setting + "` FROM `GuildSettings` WHERE `guildId` = ?");) {

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
     * @param setting the setting to be changed.
     * @param value the value of the setting being changed.
     * @param guild the guild where the setting will be changed.
     * @return if the set was successful.
     */
    public static boolean setGuildSettings(String setting, String value, String guild) {
        try(Connection conn = SettingsDatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("UPDATE `GuildSettings` SET `" + setting + "` = ? WHERE `guildId` = ?");) {

            MetricsManager.getDatabaseMetrics().UPDATE.getAndIncrement();

            stmt.setString(1, value);
            stmt.setString(2, guild);

            return !stmt.execute();

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", DatabaseFunctions.class.getSimpleName(), ex.getMessage(), ex);
            return false;
        }
    }

    /**
     * Updates the database with the latest metrics.
     */
    public static void updateMetricsDatabase() {
        try(Connection conn = MetricsDatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO `SystemMetrics`(`shardId`, `uptime`, `memoryTotal`, `memoryUsed`) VALUES(?, ?, ?, ?)");
            PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO `EventMetrics`(`shardId`, `messagesProcessed`, `reactsProcessed`, `commandsExecuted`, `commandsFailed`) VALUES(?, ?, ?, ?, ?)");
            PreparedStatement stmt3 = conn.prepareStatement("INSERT INTO `DiscordMetrics`(`shardId`, `ping`, `guildCount`, `channelCount`, `userCount`, `roleCount`, `emoteCount`) VALUES(?, ?, ?, ?, ?, ?, ?)");
            PreparedStatement stmt4 = conn.prepareStatement("INSERT INTO `DatabaseMetrics`(`shardId`, `selects`, `inserts`, `updates`, `deletes`) VALUES(?, ?, ?, ?, ?)");) {

            stmt.setInt(1, Cache.BOT.getJDA().getShardInfo().getShardId());
            stmt.setLong(2, MetricsManager.getSystemMetrics().UPTIME);
            stmt.setLong(3, MetricsManager.getSystemMetrics().MEMORY_TOTAL);
            stmt.setLong(4, MetricsManager.getSystemMetrics().MEMORY_USED);
            stmt.execute();

            stmt2.setInt(1, Cache.BOT.getJDA().getShardInfo().getShardId());
            stmt2.setInt(2, MetricsManager.getEventMetrics().MESSAGES_PROCESSED.get());
            stmt2.setInt(3, MetricsManager.getEventMetrics().REACTS_PROCESSED.get());
            stmt2.setInt(4, MetricsManager.getEventMetrics().COMMANDS_EXECUTED.get());
            stmt2.setInt(5, MetricsManager.getEventMetrics().COMMANDS_FAILED.get());
            stmt2.execute();

            stmt3.setInt(1, Cache.BOT.getJDA().getShardInfo().getShardId());
            stmt3.setDouble(2, MetricsManager.getDiscordMetrics().PING.get());
            stmt3.setInt(3, MetricsManager.getDiscordMetrics().GUILD_COUNT);
            stmt3.setInt(4, MetricsManager.getDiscordMetrics().CHANNEL_COUNT);
            stmt3.setInt(5, MetricsManager.getDiscordMetrics().USER_COUNT);
            stmt3.setInt(6, MetricsManager.getDiscordMetrics().ROLE_COUNT);
            stmt3.setInt(7, MetricsManager.getDiscordMetrics().EMOTE_COUNT);
            stmt3.execute();

            stmt4.setInt(1, Cache.BOT.getJDA().getShardInfo().getShardId());
            stmt4.setInt(2, MetricsManager.getDatabaseMetrics().SELECT.get());
            stmt4.setInt(3, MetricsManager.getDatabaseMetrics().INSERT.get());
            stmt4.setInt(4, MetricsManager.getDatabaseMetrics().UPDATE.get());
            stmt4.setInt(5, MetricsManager.getDatabaseMetrics().DELETE.get());
            stmt4.execute();

            MetricsManager.getDatabaseMetrics().INSERT.getAndAdd(4);

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", DatabaseFunctions.class.getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Updates the database with the latest command.
     * @param guildId String
     * @param command String
     */
    public static void updateCommandsLog(String guildId, String command) {
        try(Connection conn = MetricsDatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO `CommandsLog`(`shardId`, `guildId`, `command`) VALUES(?, ?, ?)");) {

            stmt.setInt(1, Cache.BOT.getJDA().getShardInfo().getShardId());
            stmt.setString(2, guildId);
            stmt.setString(3, command);
            stmt.execute();

            MetricsManager.getDatabaseMetrics().INSERT.getAndIncrement();

        } catch (Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", DatabaseFunctions.class.getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Truncates the metrics database. (This happens when the bot is first loaded.)
     */
    public static void truncateMetrics() {
        try(Connection conn = MetricsDatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM `SystemMetrics`");
            PreparedStatement stmt2 = conn.prepareStatement("DELETE FROM `EventMetrics`");
            PreparedStatement stmt3 = conn.prepareStatement("DELETE FROM `DiscordMetrics`");
            PreparedStatement stmt4 = conn.prepareStatement("DELETE FROM `DatabaseMetrics`");
            PreparedStatement stmt5 = conn.prepareStatement("DELETE FROM `CommandsLog`");) {

            stmt.execute();
            stmt2.execute();
            stmt3.execute();
            stmt4.execute();
            stmt5.execute();

            MetricsManager.getDatabaseMetrics().DELETE.getAndAdd(5);

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", DatabaseFunctions.class.getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Cleans up any guild's that ask the bot to leave. (Uses CASCADE)
     * @param guild the guild's id.
     */
    public static void cleanup(String guild) {
        try(Connection conn = SettingsDatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM `Guilds` WHERE `guildId` = ?");) {

            stmt.setString(1, guild);
            stmt.execute();

            MetricsManager.getDatabaseMetrics().DELETE.getAndIncrement();

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", DatabaseFunctions.class.getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Updates settings from channels that are deleted.
     * @param channel the channel to clean up.
     */
    public static void cleanupSettings(String setting, String guildId) {
        try(Connection conn = SettingsDatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("UPDATE `GuildSettings` SET " + setting + " = null WHERE `guildId` = guildId");){

            stmt.execute();

            MetricsManager.getDatabaseMetrics().UPDATE.getAndIncrement();

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", DatabaseFunctions.class.getSimpleName(), ex.getMessage(), ex);
        }
    }

}
