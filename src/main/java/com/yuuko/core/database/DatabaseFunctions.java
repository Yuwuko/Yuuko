package com.yuuko.core.database;

import com.yuuko.core.Cache;
import com.yuuko.core.SystemClock;
import com.yuuko.core.utils.Utils;
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
     * @param server the server to check.
     * @return boolean
     */
    private boolean exists(String server) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT `serverId` FROM `ServerSettings` WHERE `serverId` = ?");
            stmt.setString(1, server);
            ResultSet resultSet = stmt.executeQuery();

            final boolean existence = resultSet.next();

            stmt.close();
            conn.close();

            return existence;

        } catch(Exception ex) {
            Utils.sendException(ex, "Existence");
        }
        return true;
    }

    /**
     * Adds a new server to the database and initialises it's settings.
     * @param server the server to add.
     * @return if the add was successful.
     */
    public boolean addNewServer(String server) {
        try {
            if(!exists(server)) {
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO `ServerSettings` (`serverId`, `commandPrefix`) VALUES (?, '-')");
                stmt.setString(1, server);
                stmt.execute();

                stmt.close();
                conn.close();

                return true;
            } else {
                return false;
            }
        } catch(Exception ex) {
            Utils.sendException(ex, "Unable to add new server to the database. (ID: " + server + ")");
            return false;
        }
    }

    /**
     * Adds a new server to the database and initialises it's settings.
     * @param e MessageReceivedEvent.
     * @return if the add was successful.
     */
    public boolean addServers(MessageReceivedEvent e) {
        Guild guild;

        try {
            SnowflakeCacheView servers = e.getJDA().getGuildCache();

            for(Object server : servers) {
                guild = (Guild) server;
                if(!exists(guild.getId())) {
                    Connection conn = DatabaseConnection.getConnection();
                    PreparedStatement stmt = conn.prepareStatement("INSERT INTO `ServerSettings` (`serverId`, `commandPrefix`) VALUES (?, '-')");
                    stmt.setString(1, guild.getId());
                    stmt.execute();

                    stmt.close();
                    conn.close();
                }
            }

            return true;

        } catch (Exception ex) {
            Utils.sendException(ex, "Unable to add new server to the database.");
            return false;
        }
    }

    /**
     * Retrieves all of the server settings for a server.
     * ** Doesn't close connection or resultset is lost **
     * @param server the server id.
     * @return the results of the query.
     */
    public ResultSet getModuleSettings(Connection connection, String server) {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM `ModuleSettings` WHERE `serverId` = ?");
            stmt.setString(1, server);
            return stmt.executeQuery();
        } catch(Exception ex) {
            Utils.sendException(ex, "Unable to get module settings. (ID: " + server + ")");
            return null;
        }
    }

    /**
     * Checks to see if a module is active before parsing a command.
     * @param moduleName the name of the module.
     * @return (boolean) if the module is active or not.
     */
    public boolean checkModuleSettings(String moduleName, String server) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT `" + moduleName + "` FROM `ModuleSettings` WHERE `serverId` = ?");
            stmt.setString(1, server);
            ResultSet resultSet = stmt.executeQuery();
            resultSet.next();

            final boolean result = resultSet.getBoolean(1);

            stmt.close();
            conn.close();

            return result;

        } catch(Exception ex) {
            Utils.sendException(ex, "Unable to get individual module setting. (Module: " + moduleName + ", Server: " + server + ")");
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
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("UPDATE `ModuleSettings` SET `" + moduleIn + "` = NOT `" + moduleIn + "` WHERE `serverId` = ?");
            stmt.setString(1, server);
            stmt.execute();

            final boolean result = checkModuleSettings(moduleIn, server);

            stmt.close();
            conn.close();

            return result;

        } catch(Exception ex) {
            Utils.sendException(ex, "Unable to toggle module setting. (Module: " + moduleIn + ", Server: " + server + ")");
            return false;
        }
    }

    /**
     * Returns all of the server settings for the given server.
     * ** Doesn't close connection or resultset is lost **
     * @param connection the database connection used.
     * @param server the server to get the settings for.
     * @return ResultSet
     */
    public ResultSet getServerSettings(Connection connection, String server) {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM `ServerSettings` WHERE `serverId` = ?");
            stmt.setString(1, server);
            return stmt.executeQuery();

        } catch(Exception ex) {
            Utils.sendException(ex, "Unable to get server settings. (ID: " + server + ")");
            return null;
        }
    }

    public String getServerSetting(String setting, String server) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT `" + setting + "` FROM `ServerSettings` WHERE `serverId` = ?");
            stmt.setString(1, server);
            ResultSet resultSet = stmt.executeQuery();
            resultSet.next();

            final String result = resultSet.getString(1);

            stmt.close();
            conn.close();

            return result;

        } catch(Exception ex) {
            Utils.sendException(ex, "Unable to get server setting. (ID: " + server + ")");
            return null;
        }
    }

    /**
     * Changes a setting value for the given server setting. (Very dangerous without the correct checking...)
     * @param setting the setting to be changed.
     * @param value the value of the setting being changed.
     * @param server the server where the setting will be changed.
     * @return if the set was successful.
     */
    public boolean setServerSettings(String setting, String value, String server) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("UPDATE `ServerSettings` SET `" + setting + "` = ? WHERE `serverId` = ?");
            stmt.setString(1, value);
            stmt.setString(2, server);

            final boolean result = !stmt.execute();

            stmt.close();
            conn.close();

            return result;

        } catch(Exception ex) {
            Utils.sendException(ex, "Unable to set server setting '"+ setting +"'. (" + server + ")");
            return false;
        }
    }

    /**
     * Binds a particular module to a channel.
     * @param modName the name of the module.
     * @param channel the idLong of the channel.
     * @param server the idLong of the server.
     * @return boolean
     */
    public int toggleBinding(String modName, String channel, String server) {
        String moduleIn = "module" + modName;

        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM `ModuleBindings` WHERE `serverId` = ? AND `channelId` = ? AND `moduleName` = ?");
            stmt.setString(1, server);
            stmt.setString(2, channel);
            stmt.setString(3, moduleIn);
            ResultSet resultSet = stmt.executeQuery();

            if(!resultSet.next()) {
                PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO `ModuleBindings` VALUES (?,?,?, '1', '0')");
                stmt2.setString(1, server);
                stmt2.setString(2, channel);
                stmt2.setString(3, moduleIn);
                if(!stmt2.execute()) {
                    stmt2.close();
                    return 0;
                }
            }

            if(!resultSet.getBoolean("bound") && resultSet.getBoolean("excluded")) {
                PreparedStatement stmt2 = conn.prepareStatement("UPDATE `ModuleBindings` SET `bound` = '1', `excluded` = '0' WHERE `serverId` = ? AND `channelId` = ? AND `moduleName` = ?");
                stmt2.setString(1, server);
                stmt2.setString(2, channel);
                stmt2.setString(3, moduleIn);
                if(!stmt2.execute()) {
                    stmt2.close();
                    return 0;
                }
            }

            if(resultSet.getBoolean("bound")) {
                return deleteBindingsRecord(server, channel, moduleIn);
            }

            stmt.close();
            conn.close();

            return -1;

        } catch(Exception ex) {
            Utils.sendException(ex, "Unable to bind module to channel. (Module: " + moduleIn + ", Server: " + server + ", Channel: " + channel + ")");
            return -1;
        }
    }

    /**
     * Excludes a particular module from a particular channel.
     * @param modName the name of the module.
     * @param channel the idLong of the channel.
     * @param server the idLong of the server.
     * @return boolean
     */
    public int toggleExclusion(String modName, String channel, String server) {
        String moduleIn = "module" + modName;

        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM `ModuleBindings` WHERE `serverId` = ? AND `channelId` = ? AND `moduleName` = ?");
            stmt.setString(1, server);
            stmt.setString(2, channel);
            stmt.setString(3, moduleIn);
            ResultSet resultSet = stmt.executeQuery();

            if(!resultSet.next()) {
                PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO `ModuleBindings` VALUES (?,?,?, '0', '1')");
                stmt2.setString(1, server);
                stmt2.setString(2, channel);
                stmt2.setString(3, moduleIn);
                if(!stmt2.execute()) {
                    stmt2.close();
                    return 0;
                }
            }

            if(resultSet.getBoolean("bound") && !resultSet.getBoolean("excluded")) {
                PreparedStatement stmt2 = conn.prepareStatement("UPDATE `ModuleBindings` SET `bound` = '0', `excluded` = '1' WHERE `serverId` = ? AND `channelId` = ? AND `moduleName` = ?");
                stmt2.setString(1, server);
                stmt2.setString(2, channel);
                stmt2.setString(3, moduleIn);
                if(!stmt2.execute()) {
                    stmt2.close();
                    return 0;
                }
            }

            if(resultSet.getBoolean("excluded")) {
                return deleteBindingsRecord(server, channel, moduleIn);
            }

            stmt.close();
            conn.close();

            return -1;

        } catch(Exception ex) {
            Utils.sendException(ex, "Unable to exclude module from channel. (Module: " + moduleIn + ", Server: " + server + ", Channel: " + channel + ")");
            return -1;
        }
    }

    /**
     * Removes a binding record from the database.
     * @param server String
     * @param channel String
     * @param moduleIn String
     * @return int
     */
    private int deleteBindingsRecord(String server, String channel, String moduleIn) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM `ModuleBindings` WHERE `serverId` = ? AND `channelId` = ? AND `moduleName` = ?");
            stmt.setString(1, server);
            stmt.setString(2, channel);
            stmt.setString(3, moduleIn);
            if(!stmt.execute()) {
                stmt.close();
                conn.close();

                return 1;
            }

            stmt.close();
            conn.close();
            return -1;

        } catch(Exception ex) {
            Utils.sendException(ex, "Unable to return bindings/exclusions. (" + server + ")");
            return -1;
        }
    }

    /**
     * Gets the bindings/exclusions for a particular channel.
     * ** Doesn't close connection or resultset is lost **
     * @param server the idLong of the server.
     * @return ResultSet
     */
    public ResultSet getBindingsExclusionsChannel(Connection connection, String server, String moduleIn) {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM `ModuleBindings` WHERE `serverId` = ? AND `moduleName` = ?");
            stmt.setString(1, server);
            stmt.setString(2, moduleIn);
            return stmt.executeQuery();

        } catch(Exception ex) {
            Utils.sendException(ex, "Unable to return bindings/exclusions. (" + server + ")");
            return null;
        }
    }

    public void updateServerStatus() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("UPDATE `ServerStatus` SET `uptime` = ?, `ping` = ?, `guilds` = ?, `modules` = ?, `commands` = ?, `messagesProcessed` = ?, `reactsProcessed` = ?, `commandsProcessed` = ?, `latestInfo` = ?");
            stmt.setInt(1, SystemClock.getRuntime());
            stmt.setDouble(2, Cache.PING);
            stmt.setInt(3, Cache.GUILD_COUNT);
            stmt.setInt(4, Cache.MODULES.size());
            stmt.setInt(5, Cache.COMMANDS.size());
            stmt.setInt(6, Cache.MESSAGES_PROCESSED);
            stmt.setInt(7, Cache.REACTS_PROCESSED);
            stmt.setInt(8, Cache.COMMANDS_PROCESSED);
            stmt.setString(9, Cache.LATEST_INFO);
            stmt.execute();

            stmt.close();
            conn.close();

        } catch(Exception ex) {
            Utils.sendException(ex, "Unable to update server status.");
        }
    }

    /**
     * Cleans up any server's that ask the bot to leave.
     * @param server the server's id.
     */
    public void cleanup(String server) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM `ServerSettings` WHERE `serverId` = ?");
            stmt.setString(1, server);
            stmt.execute();

            stmt.close();
            conn.close();

        } catch(Exception ex) {
            Utils.sendException(ex, "Unable to remove server from the database. (" + server + ")");
        }
    }

}
