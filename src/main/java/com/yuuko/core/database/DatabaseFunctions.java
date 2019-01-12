package com.yuuko.core.database;

import com.yuuko.core.Cache;
import com.yuuko.core.database.connections.DatabaseConnection;
import com.yuuko.core.database.connections.MetricsDatabaseConnection;
import com.yuuko.core.metrics.handlers.MetricsManager;
import com.yuuko.core.utilities.MessageHandler;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.utils.cache.SnowflakeCacheView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


@SuppressWarnings("ALL")
public class DatabaseFunctions {

    public DatabaseFunctions() {
    }

    /**
     * Small method that checks if a server exists on the database.
     * @param guild the server to check.
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
            MessageHandler.sendException(ex, "Existence");
        }
        return true;
    }

    /**
     * Adds a new server to the database and initialises it's settings.
     * @param guild the server to add.
     * @return if the add was successful.
     */
    public boolean addNewGuild(String guild) {
        try {
            if(!exists(guild)) {
                MetricsManager.getDatabaseMetrics().SELECT.getAndIncrement();

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
            MessageHandler.sendException(ex, "Unable to add new guild to the database. (ID: " + guild + ")");
            return false;
        }
    }

    /**
     * Adds a new server to the database and initialises it's settings.
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
            MessageHandler.sendException(ex, "Unable to add new guilds to the database.");
            return false;
        }
    }

    /**
     * Retrieves all of the server settings for a server.
     * ** Doesn't close connection or resultset is lost **
     * @param guild the server id.
     * @return the results of the query.
     */
    public ResultSet getModuleSettings(Connection connection, String guild) {
        try {
            MetricsManager.getDatabaseMetrics().SELECT.getAndIncrement();

            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM `ModuleSettings` WHERE `guildId` = ?");
            stmt.setString(1, guild);

            return stmt.executeQuery();
        } catch(Exception ex) {
            MessageHandler.sendException(ex, "Unable to get module settings. (ID: " + guild + ")");
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
            MessageHandler.sendException(ex, "Unable to get individual module setting. (Module: " + moduleName + ", Server: " + guild + ")");
            return false;
        }
    }

    /**
     * Toggles a module for a server, returns the new value.
     * @param moduleIn the module to toggle.
     * @param server the server in which the module is to be toggled.
     * @return boolean.
     */
    public boolean toggleModule(String moduleIn, String server) {
        try {
            MetricsManager.getDatabaseMetrics().UPDATE.getAndIncrement();

            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("UPDATE `ModuleSettings` SET `" + moduleIn + "` = NOT `" + moduleIn + "` WHERE `guildId` = ?");
            stmt.setString(1, server);
            stmt.execute();

            final boolean result = checkModuleSettings(moduleIn, server);

            stmt.close();
            conn.close();

            return result;

        } catch(Exception ex) {
            MessageHandler.sendException(ex, "Unable to toggle module setting. (Module: " + moduleIn + ", Server: " + server + ")");
            return false;
        }
    }

    /**
     * Returns all of the server settings for the given server.
     * ** Doesn't close connection or resultset is lost **
     * @param connection the database connection used.
     * @param guild the server to get the settings for.
     * @return ResultSet
     */
    public ResultSet getServerSettings(Connection connection, String guild) {
        try {
            MetricsManager.getDatabaseMetrics().SELECT.getAndIncrement();

            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM `GuildSettings` WHERE `guildId` = ?");
            stmt.setString(1, guild);

            return stmt.executeQuery();

        } catch(Exception ex) {
            MessageHandler.sendException(ex, "Unable to get server settings. (ID: " + guild + ")");
            return null;
        }
    }

    /**
     * Returns the value of a single server settings.
     * @param setting the setting to be checked
     * @param guild the server to check the setting for
     * @return String
     */
    public String getServerSetting(String setting, String guild) {
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
            MessageHandler.sendException(ex, "Unable to get server setting. (ID: " + guild + ")");
            return null;
        }
    }

    /**
     * Changes a setting value for the given server setting. (Very dangerous without the correct checking...)
     * @param setting the setting to be changed.
     * @param value the value of the setting being changed.
     * @param guild the server where the setting will be changed.
     * @return if the set was successful.
     */
    public boolean setServerSettings(String setting, String value, String guild) {
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
            MessageHandler.sendException(ex, "Unable to set server setting '"+ setting +"'. (" + guild + ")");
            return false;
        }
    }

    /**
     * Binds a particular module to a channel.
     * @param modName the name of the module.
     * @param channel the idLong of the channel.
     * @param guild the idLong of the server.
     * @return boolean
     */
    public int toggleBinding(String modName, String channel, String guild) {
        String moduleIn = "module" + modName;

        try {
            MetricsManager.getDatabaseMetrics().SELECT.getAndIncrement();

            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM `ModuleBindings` WHERE `guildId` = ? AND `channelId` = ? AND `moduleName` = ?");
            stmt.setString(1, guild);
            stmt.setString(2, channel);
            stmt.setString(3, moduleIn);
            ResultSet resultSet = stmt.executeQuery();

            if(!resultSet.next()) {
                PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO `ModuleBindings` VALUES (?,?,?)");
                stmt2.setString(1, guild);
                stmt2.setString(2, channel);
                stmt2.setString(3, moduleIn);
                if(!stmt2.execute()) {
                    stmt2.close();
                    conn.close();
                    MetricsManager.getDatabaseMetrics().INSERT.getAndIncrement();
                    return 0;
                }
            }

            stmt.close();
            conn.close();
            return deleteBindingsRecord(guild, channel, moduleIn);

        } catch(Exception ex) {
            MessageHandler.sendException(ex, "Unable to bind module to channel. (Module: " + moduleIn + ", Server: " + guild + ", Channel: " + channel + ")");
            return -1;
        }
    }

    /**
     * Removes a binding record from the database.
     * @param guild String
     * @param channel String
     * @param moduleIn String
     * @return int
     */
    private int deleteBindingsRecord(String guild, String channel, String moduleIn) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM `ModuleBindings` WHERE `guildId` = ? AND `channelId` = ? AND `moduleName` = ?");
            stmt.setString(1, guild);
            stmt.setString(2, channel);
            stmt.setString(3, moduleIn);
            if(!stmt.execute()) {
                stmt.close();
                conn.close();
                MetricsManager.getDatabaseMetrics().DELETE.getAndIncrement();
                return 1;
            }

            stmt.close();
            conn.close();
            return -1;

        } catch(Exception ex) {
            MessageHandler.sendException(ex, "Unable to return bindings/exclusions. (" + guild + ")");
            return -1;
        }
    }

    /**
     * Gets the bindings/exclusions for a particular channel.
     * ** Doesn't close connection or resultset is lost **
     * @param guild the idLong of the server.
     * @return ResultSet
     */
    public ResultSet getBindingsByModule(Connection connection, String guild, String moduleIn) {
        try {
            MetricsManager.getDatabaseMetrics().SELECT.getAndIncrement();

            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM `ModuleBindings` WHERE `guildId` = ? AND `moduleName` = ?");
            stmt.setString(1, guild);
            stmt.setString(2, moduleIn);

            return stmt.executeQuery();

        } catch(Exception ex) {
            MessageHandler.sendException(ex, "Unable to return bindings. (" + guild + ")");
            return null;
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
            stmt.close();

            PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO `EventMetrics`(`shardId`, `messagesProcessed`, `reactsProcessed`, `commandsExecuted`, `commandsFailed`) VALUES(?, ?, ?, ?, ?)");
            stmt.setInt(1, Cache.JDA.getShardInfo().getShardId());
            stmt.setInt(2, MetricsManager.getEventMetrics().MESSAGES_PROCESSED.get());
            stmt.setInt(3, MetricsManager.getEventMetrics().REACTS_PROCESSED.get());
            stmt.setInt(4, MetricsManager.getEventMetrics().COMMANDS_EXECUTED.get());
            stmt.setInt(5, MetricsManager.getEventMetrics().COMMANDS_FAILED.get());
            stmt2.execute();
            stmt2.close();

            PreparedStatement stmt3 = conn.prepareStatement("INSERT INTO `DiscordMetrics`(`shardId`, `ping`, `guildCount`, `channelCount`, `userCount`, `roleCount`, `emoteCount`) VALUES(?, ?, ?, ?, ?, ?, ?)");
            stmt.setInt(1, Cache.JDA.getShardInfo().getShardId());
            stmt.setDouble(2, MetricsManager.getDiscordMetrics().PING.get());
            stmt.setInt(3, MetricsManager.getDiscordMetrics().GUILD_COUNT);
            stmt.setInt(4, MetricsManager.getDiscordMetrics().CHANNEL_COUNT);
            stmt.setInt(5, MetricsManager.getDiscordMetrics().USER_COUNT);
            stmt.setInt(6, MetricsManager.getDiscordMetrics().ROLE_COUNT);
            stmt.setInt(7, MetricsManager.getDiscordMetrics().EMOTE_COUNT);
            stmt3.execute();
            stmt3.close();

            PreparedStatement stmt4 = conn.prepareStatement("INSERT INTO `DatabaseMetrics`(`shardId`, `selects`, `inserts`, `updates`, `deletes`) VALUES(?, ?, ?, ?, ?)");
            stmt.setInt(1, Cache.JDA.getShardInfo().getShardId());
            stmt.setInt(2, MetricsManager.getDatabaseMetrics().SELECT.get());
            stmt.setInt(3, MetricsManager.getDatabaseMetrics().INSERT.get());
            stmt.setInt(4, MetricsManager.getDatabaseMetrics().UPDATE.get());
            stmt.setInt(5, MetricsManager.getDatabaseMetrics().DELETE.get());
            stmt4.execute();
            stmt4.close();

            conn.close();

        } catch(Exception ex) {
            MessageHandler.sendException(ex, "Unable to update metrics.");
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
            MessageHandler.sendException(ex, "Unable to update commands.");
        }
    }

    /**
     * Truncates the metrics database. (This happens when the bot is first loaded.)
     */
    public void truncateMetrics() {
        try {
            MetricsManager.getDatabaseMetrics().DELETE.getAndAdd(4);

            Connection conn = MetricsDatabaseConnection.getConnection();

            PreparedStatement stmt = conn.prepareStatement("DELETE FROM `SystemMetrics`");
            stmt.execute();
            stmt.close();

            PreparedStatement stmt2 = conn.prepareStatement("DELETE FROM `EventMetrics`");
            stmt2.execute();
            stmt2.close();

            PreparedStatement stmt3 = conn.prepareStatement("DELETE FROM `DiscordMetrics`");
            stmt3.execute();
            stmt3.close();

            PreparedStatement stmt4 = conn.prepareStatement("DELETE FROM `DatabaseMetrics`");
            stmt4.execute();
            stmt4.close();

            conn.close();

        } catch(Exception ex) {
            MessageHandler.sendException(ex, "Unable to truncate database..");
        }
    }

    /**
     * Cleans up any server's that ask the bot to leave. (Uses CASCADE)
     * @param guild the server's id.
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
            MessageHandler.sendException(ex, "Unable to remove server from the database. (" + guild + ")");
        }
    }

    /**
     * Removes binding from channels that are deleted.
     * @param channel the channel to clean up.
     */
    public void cleanupBindings(String channel) {
        try {
            MetricsManager.getDatabaseMetrics().DELETE.getAndIncrement();

            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM `ModuleBindings` WHERE `channelId` = ?");
            stmt.setString(1, channel);
            stmt.execute();

            stmt.close();
            conn.close();

        } catch(Exception ex) {
            MessageHandler.sendException(ex, "Unable to update bindings in the database. (Channel:" + channel + ")");
        }
    }



}
