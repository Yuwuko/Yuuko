package com.basketbandit.core.database;

import com.basketbandit.core.utils.Utils;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.utils.cache.SnowflakeCacheView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Iterator;

public class DatabaseFunctions {

    private Connection conn;

    public DatabaseFunctions() {
        conn = new DatabaseConnection().getConnection();
    }

    /**
     * Adds a new server to the database and initialises it's settings.
     * @param server the server to add.
     * @return if the add was successful.
     */
    public boolean addNewServer(String server) {
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT `serverId` FROM `ServerSettings` WHERE `serverId` = ?");
            stmt.setString(1, server);
            ResultSet resultSet = stmt.executeQuery();

            if(!resultSet.next()) {
                conn = new DatabaseConnection().getConnection();
                PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO `ServerSettings` (`serverId`, `commandPrefix`) VALUES (?, '-')");
                stmt2.setString(1, server);
                stmt2.execute();

                conn = new DatabaseConnection().getConnection();
                PreparedStatement stmt3 = conn.prepareStatement("INSERT INTO `ModuleSettings` (`serverId`) VALUES (?)");
                stmt3.setString(1, server);
                return stmt3.execute();
            } else {
                return false;
            }

        } catch(Exception ex) {
            Utils.sendException(ex, "Unable to add new server to the database. (ID: " + server + ")");
            return false;
        } finally {
            try {
                conn.close();
            } catch(Exception ex) {
                Utils.sendException(ex, "Unable to close connection to database.");
            }
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
            Iterator it = servers.iterator();

            while(it.hasNext()) {
                guild = (Guild)it.next();

                PreparedStatement stmt = conn.prepareStatement("SELECT `serverId` FROM `ServerSettings` WHERE `serverId` = ?");
                stmt.setString(1, guild.getId());
                ResultSet resultSet = stmt.executeQuery();

                if(!resultSet.next()) {
                    conn = new DatabaseConnection().getConnection();
                    PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO `ServerSettings` (`serverId`, `commandPrefix`) VALUES (?, '-')");
                    stmt2.setString(1, guild.getId());
                    stmt2.execute();

                    conn = new DatabaseConnection().getConnection();
                    PreparedStatement stmt3 = conn.prepareStatement("INSERT INTO `ModuleSettings` (`serverId`) VALUES (?)");
                    stmt3.setString(1, guild.getId());
                    stmt3.execute();
                }
            }

            return true;

        } catch (Exception ex) {
            Utils.sendException(ex, "Unable to add new server to the database.");
            return false;
        } finally {
            try {
                conn.close();
            } catch (Exception ex) {
                Utils.sendException(ex, "Unable to close connection to database.");
            }
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
            PreparedStatement stmt = conn.prepareStatement("SELECT " + moduleName + " FROM `ModuleSettings` WHERE `serverId` = ?");
            stmt.setString(1, server);
            ResultSet resultSet = stmt.executeQuery();
            resultSet.next();
            return resultSet.getBoolean(1);

        } catch(Exception ex) {
            Utils.sendException(ex, "Unable to get individual module setting. (Module: " + moduleName + ", Server: " + server + ")");
            return false;
        }
    }

    /**
     * Toggles a module for a server, returns the new value.
     * @param modName the module to toggle.
     * @param server the server in which the module is to be toggled.
     * @return boolean.
     */
    public boolean toggleModule(String modName, String server) {
        try {
            PreparedStatement stmt = conn.prepareStatement("UPDATE `ModuleSettings` SET ? = NOT ? WHERE `serverId` = ?");
            stmt.setString(1, modName);
            stmt.setString(2, modName);
            stmt.setString(3, server);
            stmt.execute();
            return checkModuleSettings(modName, server);

        } catch(Exception ex) {
            Utils.sendException(ex, "Unable to toggle module setting. (Module: " + modName + ", Server: " + server + ")");
            return false;
        } finally {
            try {
                conn.close();
            } catch(Exception ex) {
                Utils.sendException(ex, "Unable to close connection to database.");
            }
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
            PreparedStatement stmt = conn.prepareStatement("SELECT ? FROM `ServerSettings` WHERE `serverId` = ?");
            stmt.setString(1, setting);
            stmt.setString(2, server);
            ResultSet resultSet = stmt.executeQuery();
            resultSet.next();
            return resultSet.getString(1);

        } catch(Exception ex) {
            Utils.sendException(ex, "Unable to get server setting. (ID: " + server + ")");
            return null;
        } finally {
            try {
                conn.close();
            } catch(Exception ex) {
                Utils.sendException(ex, "Unable to close connection to database.");
            }
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
            PreparedStatement stmt = conn.prepareStatement("UPDATE `ServerSettings` SET ? = ? WHERE `serverId` = ?");
            stmt.setString(1, setting);
            stmt.setString(2, value);
            stmt.setString(3, server);
            return !stmt.execute();

        } catch(Exception ex) {
            Utils.sendException(ex, "Unable to set server setting '"+ setting +"'. (" + server + ")");
            return false;
        } finally {
            try {
                conn.close();
            } catch(Exception ex) {
                Utils.sendException(ex, "Unable to close connection to database.");
            }
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
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM `ModuleBindings` WHERE `serverId` = ? AND `channelId` = ? AND `moduleName` = ?");
            stmt.setString(1, server);
            stmt.setString(2, channel);
            stmt.setString(3, moduleIn);
            ResultSet resultSet = stmt.executeQuery();

            if(!resultSet.next()) {
                conn = new DatabaseConnection().getConnection();
                PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO `ModuleBindings` VALUES (?,?,?, '1', '0')");
                stmt2.setString(1, server);
                stmt2.setString(2, channel);
                stmt2.setString(3, moduleIn);
                if(!stmt2.execute()) {
                    return 0;
                }
            }

            if(!resultSet.getBoolean("bound") && resultSet.getBoolean("excluded")) {
                conn = new DatabaseConnection().getConnection();
                PreparedStatement stmt2 = conn.prepareStatement("UPDATE `ModuleBindings` SET `bound` = '1', `excluded` = '0' WHERE `serverId` = ? AND `channelId` = ? AND `moduleName` = ?");
                stmt2.setString(1, server);
                stmt2.setString(2, channel);
                stmt2.setString(3, moduleIn);
                if(!stmt2.execute()) {
                    return 0;
                }
            }

            if(resultSet.getBoolean("bound")) {
                conn = new DatabaseConnection().getConnection();
                PreparedStatement stmt2 = conn.prepareStatement("DELETE FROM `ModuleBindings` WHERE `serverId` = ? AND `channelId` = ? AND `moduleName` = ?");
                stmt2.setString(1, server);
                stmt2.setString(2, channel);
                stmt2.setString(3, moduleIn);
                if(!stmt2.execute()) {
                    return 1;
                }
            }

            return -1;

        } catch(Exception ex) {
            ex.printStackTrace();
            Utils.sendException(ex, "Unable to bind module to channel. (Module: " + moduleIn + ", Server: " + server + ", Channel: " + channel + ")");
            return -1;
        } finally {
            try {
                conn.close();
            } catch(Exception ex) {
                Utils.sendException(ex, "Unable to close connection to database.");
            }
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
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM `ModuleBindings` WHERE `serverId` = ? AND `channelId` = ? AND `moduleName` = ?");
            stmt.setString(1, server);
            stmt.setString(2, channel);
            stmt.setString(3, moduleIn);
            ResultSet resultSet = stmt.executeQuery();

            if(!resultSet.next()) {
                conn = new DatabaseConnection().getConnection();
                PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO `ModuleBindings` VALUES (?,?,?, '0', '1')");
                stmt2.setString(1, server);
                stmt2.setString(2, channel);
                stmt2.setString(3, moduleIn);
                if(!stmt2.execute()) {
                    return 0;
                }
            }

            if(resultSet.getBoolean("bound") && !resultSet.getBoolean("excluded")) {
                conn = new DatabaseConnection().getConnection();
                PreparedStatement stmt2 = conn.prepareStatement("UPDATE `ModuleBindings` SET `bound` = '0', `excluded` = '1' WHERE `serverId` = ? AND `channelId` = ? AND `moduleName` = ?");
                stmt2.setString(1, server);
                stmt2.setString(2, channel);
                stmt2.setString(3, moduleIn);
                if(!stmt2.execute()) {
                    return 0;
                }
            }

            if(resultSet.getBoolean("excluded")) {
                conn = new DatabaseConnection().getConnection();
                PreparedStatement stmt2 = conn.prepareStatement("DELETE FROM `ModuleBindings` WHERE `serverId` = ? AND `channelId` = ? AND `moduleName` = ?");
                stmt2.setString(1, server);
                stmt2.setString(2, channel);
                stmt2.setString(3, moduleIn);
                if(!stmt2.execute()) {
                    return 1;
                }
             }

            return -1;

        } catch(Exception ex) {
            ex.printStackTrace();
            Utils.sendException(ex, "Unable to exclude module from channel. (Module: " + moduleIn + ", Server: " + server + ", Channel: " + channel + ")");
            return -1;
        } finally {
            try {
                conn.close();
            } catch(Exception ex) {
                Utils.sendException(ex, "Unable to close connection to database.");
            }
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
            ex.printStackTrace();
            Utils.sendException(ex, "Unable to return bindings/exclusions. (" + server + ")");
            return null;
        }
    }

    /**
     * Gets the bindings/exclusions for a particular server.
     * ** Doesn't close connection or resultset is lost **
     * @param server the idLong of the server.
     * @return ResultSet
     */
    public ResultSet getBindingsExclusionsServer(Connection connection, String server) {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM `ModuleBindings` WHERE `serverId` = ?");
            stmt.setString(1, server);
            return stmt.executeQuery();

        } catch(Exception ex) {
            Utils.sendException(ex, "Unable to return bindings/exclusions. (" + server + ")");
            return null;
        }
    }

    /**
     * Cleans up any server's that ask the bot to leave.
     * @param server the server's id.
     */
    public void cleanup(String server) {
        try {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM `ServerSettings` WHERE `serverId` = ?");
            stmt.setString(1, server);
            stmt.execute();

        } catch(Exception ex) {
            Utils.sendException(ex, "Unable to remove server from the database. (" + server + ")");
        } finally {
            try {
                conn.close();
            } catch(Exception ex) {
                Utils.sendException(ex, "Unable to close connection to database.");
            }
        }
    }

}
