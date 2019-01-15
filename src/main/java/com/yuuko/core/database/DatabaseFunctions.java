package com.yuuko.core.database;

import com.yuuko.core.Cache;
import com.yuuko.core.database.connections.DatabaseConnection;
import com.yuuko.core.database.connections.MetricsDatabaseConnection;
import com.yuuko.core.metrics.handlers.MetricsManager;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.utils.cache.SnowflakeCacheView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


@SuppressWarnings("ALL")
public class DatabaseFunctions {

    private static final Logger log = LoggerFactory.getLogger(DatabaseFunctions.class);

    public DatabaseFunctions() {
    }

    /**
     * Small method that checks if a guild exists on the database.
     * @param guild the guild to check.
     * @return boolean
     */
    private boolean exists(String guild) {
        try {
            MetricsManager.getDatabaseMetrics().SELECT.getAndIncrement();

            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM `Guilds` WHERE `guildId` = ?");
            stmt.setString(1, guild);
            ResultSet resultSet = stmt.executeQuery();

            final boolean existence = resultSet.next();

            stmt.close();
            conn.close();

            return existence;

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this.getClass().getSimpleName(), ex.getMessage(), ex);
        }
        return true;
    }

    /**
     * Adds a new guild to the database and initialises it's settings.
     * @param guild the guild to add.
     * @return if the add was successful.
     */
    public boolean addNewGuild(String guild) {
        try {
            if(!exists(guild)) {
                MetricsManager.getDatabaseMetrics().INSERT.getAndIncrement();

                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO `Guilds` (`guildId`) VALUES (?)");
                stmt.setString(1, guild);
                stmt.execute();

                stmt.close();
                conn.close();

                return true;
            } else {
                return false;
            }
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this.getClass().getSimpleName(), ex.getMessage(), ex);
            return false;
        }
    }

    /**
     * Adds a new guild to the database and initialises it's settings.
     * @param e MessageReceivedEvent.
     * @return if the add was successful.
     */
    public boolean addGuilds(MessageReceivedEvent e) {
        try {
            SnowflakeCacheView guilds = e.getJDA().getGuildCache();

            for(Object guild : guilds) {
                Guild matchedGuild = (Guild) guild;
                if(!exists(matchedGuild.getId())) {
                    addNewGuild(matchedGuild.getId());
                }
            }

            return true;

        } catch (Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this.getClass().getSimpleName(), ex.getMessage(), ex);
            return false;
        }
    }

    /**
     * Retrieves all of the guild settings for a guild.
     * ** Doesn't close connection or resultset is lost **
     * @param guild the guild id.
     * @return the results of the query.
     */
    public ResultSet getModuleSettings(Connection connection, String guild) {
        try {
            MetricsManager.getDatabaseMetrics().SELECT.getAndIncrement();

            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM `ModuleSettings` WHERE `guildId` = ?");
            stmt.setString(1, guild);

            return stmt.executeQuery();
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this.getClass().getSimpleName(), ex.getMessage(), ex);
            return null;
        }
    }

    /**
     * Checks to see if a module is active before parsing a command.
     * @param moduleName the name of the module.
     * @return (boolean) if the module is active or not.
     */
    public boolean checkModuleSettings(String moduleName, String guild) {
        try {
            MetricsManager.getDatabaseMetrics().SELECT.getAndIncrement();

            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT `" + moduleName + "` FROM `ModuleSettings` WHERE `guildId` = ?");
            stmt.setString(1, guild);
            ResultSet resultSet = stmt.executeQuery();
            resultSet.next();

            final boolean result = resultSet.getBoolean(1);

            stmt.close();
            conn.close();

            return result;

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this.getClass().getSimpleName(), ex.getMessage(), ex);
            return false;
        }
    }

    /**
     * Toggles a module for a guild, returns the new value.
     * @param moduleIn the module to toggle.
     * @param guild the guild in which the module is to be toggled.
     * @return boolean.
     */
    public boolean toggleModule(String moduleIn, String guild) {
        try {
            MetricsManager.getDatabaseMetrics().UPDATE.getAndIncrement();

            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("UPDATE `ModuleSettings` SET `" + moduleIn + "` = NOT `" + moduleIn + "` WHERE `guildId` = ?");
            stmt.setString(1, guild);
            stmt.execute();

            final boolean result = checkModuleSettings(moduleIn, guild);

            stmt.close();
            conn.close();

            return result;

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this.getClass().getSimpleName(), ex.getMessage(), ex);
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
    public ResultSet getGuildSettings(Connection connection, String guild) {
        try {
            MetricsManager.getDatabaseMetrics().SELECT.getAndIncrement();

            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM `GuildSettings` WHERE `guildId` = ?");
            stmt.setString(1, guild);

            return stmt.executeQuery();

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this.getClass().getSimpleName(), ex.getMessage(), ex);
            return null;
        }
    }

    /**
     * Returns the value of a single guild settings.
     * @param setting the setting to be checked
     * @param guild the guild to check the setting for
     * @return String
     */
    public String getGuildSetting(String setting, String guild) {
        try {
            MetricsManager.getDatabaseMetrics().SELECT.getAndIncrement();

            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT `" + setting + "` FROM `GuildSettings` WHERE `guildId` = ?");
            stmt.setString(1, guild);
            ResultSet resultSet = stmt.executeQuery();
            resultSet.next();

            final String result = resultSet.getString(1);

            stmt.close();
            conn.close();

            return result;

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this.getClass().getSimpleName(), ex.getMessage(), ex);
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
    public boolean setGuildSettings(String setting, String value, String guild) {
        try {
            MetricsManager.getDatabaseMetrics().UPDATE.getAndIncrement();

            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("UPDATE `GuildSettings` SET `" + setting + "` = ? WHERE `guildId` = ?");
            stmt.setString(1, value);
            stmt.setString(2, guild);

            final boolean result = !stmt.execute();

            stmt.close();
            conn.close();

            return result;

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this.getClass().getSimpleName(), ex.getMessage(), ex);
            return false;
        }
    }

    /**
     * Updates the database with the latest metrics.
     */
    public void updateMetricsDatabase() {
        try {
            MetricsManager.getDatabaseMetrics().INSERT.getAndAdd(4);

            Connection conn = MetricsDatabaseConnection.getConnection();

            PreparedStatement stmt = conn.prepareStatement("INSERT INTO `SystemMetrics`(`shardId`, `uptime`, `memoryTotal`, `memoryUsed`) VALUES(?, ?, ?, ?)");
            stmt.setInt(1, Cache.JDA.getShardInfo().getShardId());
            stmt.setLong(2, MetricsManager.getSystemMetrics().UPTIME);
            stmt.setLong(3, MetricsManager.getSystemMetrics().MEMORY_TOTAL);
            stmt.setLong(4, MetricsManager.getSystemMetrics().MEMORY_USED);
            stmt.execute();

            PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO `EventMetrics`(`shardId`, `messagesProcessed`, `reactsProcessed`, `commandsExecuted`, `commandsFailed`) VALUES(?, ?, ?, ?, ?)");
            stmt2.setInt(1, Cache.JDA.getShardInfo().getShardId());
            stmt2.setInt(2, MetricsManager.getEventMetrics().MESSAGES_PROCESSED.get());
            stmt2.setInt(3, MetricsManager.getEventMetrics().REACTS_PROCESSED.get());
            stmt2.setInt(4, MetricsManager.getEventMetrics().COMMANDS_EXECUTED.get());
            stmt2.setInt(5, MetricsManager.getEventMetrics().COMMANDS_FAILED.get());
            stmt2.execute();

            PreparedStatement stmt3 = conn.prepareStatement("INSERT INTO `DiscordMetrics`(`shardId`, `ping`, `guildCount`, `channelCount`, `userCount`, `roleCount`, `emoteCount`) VALUES(?, ?, ?, ?, ?, ?, ?)");
            stmt3.setInt(1, Cache.JDA.getShardInfo().getShardId());
            stmt3.setDouble(2, MetricsManager.getDiscordMetrics().PING.get());
            stmt3.setInt(3, MetricsManager.getDiscordMetrics().GUILD_COUNT);
            stmt3.setInt(4, MetricsManager.getDiscordMetrics().CHANNEL_COUNT);
            stmt3.setInt(5, MetricsManager.getDiscordMetrics().USER_COUNT);
            stmt3.setInt(6, MetricsManager.getDiscordMetrics().ROLE_COUNT);
            stmt3.setInt(7, MetricsManager.getDiscordMetrics().EMOTE_COUNT);
            stmt3.execute();

            PreparedStatement stmt4 = conn.prepareStatement("INSERT INTO `DatabaseMetrics`(`shardId`, `selects`, `inserts`, `updates`, `deletes`) VALUES(?, ?, ?, ?, ?)");
            stmt4.setInt(1, Cache.JDA.getShardInfo().getShardId());
            stmt4.setInt(2, MetricsManager.getDatabaseMetrics().SELECT.get());
            stmt4.setInt(3, MetricsManager.getDatabaseMetrics().INSERT.get());
            stmt4.setInt(4, MetricsManager.getDatabaseMetrics().UPDATE.get());
            stmt4.setInt(5, MetricsManager.getDatabaseMetrics().DELETE.get());
            stmt4.execute();

            stmt.close();
            stmt2.close();
            stmt3.close();
            stmt4.close();
            conn.close();

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this.getClass().getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Updates the database with the latest command.
     * @param guildId String
     * @param command String
     */
    public void updateCommandsLog(String guildId, String command) {
        try {
            MetricsManager.getDatabaseMetrics().INSERT.getAndIncrement();

            Connection conn = MetricsDatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO `CommandsLog`(`shardId`, `guildId`, `command`) VALUES(?, ?, ?)");
            stmt.setInt(1, Cache.JDA.getShardInfo().getShardId());
            stmt.setString(2, guildId);
            stmt.setString(3, command);
            stmt.execute();

            stmt.close();
            conn.close();

        } catch (Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this.getClass().getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Truncates the metrics database. (This happens when the bot is first loaded.)
     */
    public void truncateMetrics() {
        try {
            MetricsManager.getDatabaseMetrics().DELETE.getAndAdd(5);

            Connection conn = MetricsDatabaseConnection.getConnection();

            PreparedStatement stmt = conn.prepareStatement("DELETE FROM `SystemMetrics`");
            stmt.execute();
            PreparedStatement stmt2 = conn.prepareStatement("DELETE FROM `EventMetrics`");
            stmt2.execute();
            PreparedStatement stmt3 = conn.prepareStatement("DELETE FROM `DiscordMetrics`");
            stmt3.execute();
            PreparedStatement stmt4 = conn.prepareStatement("DELETE FROM `DatabaseMetrics`");
            stmt4.execute();
            PreparedStatement stmt5 = conn.prepareStatement("DELETE FROM `CommandsLog`");
            stmt5.execute();

            stmt.close();
            stmt2.close();
            stmt3.close();
            stmt4.close();
            stmt5.close();
            conn.close();

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this.getClass().getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Cleans up any guild's that ask the bot to leave. (Uses CASCADE)
     * @param guild the guild's id.
     */
    public void cleanup(String guild) {
        try {
            MetricsManager.getDatabaseMetrics().DELETE.getAndIncrement();

            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM `Guilds` WHERE `guildId` = ?");
            stmt.setString(1, guild);
            stmt.execute();

            stmt.close();
            conn.close();

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this.getClass().getSimpleName(), ex.getMessage(), ex);
        }
    }

}
