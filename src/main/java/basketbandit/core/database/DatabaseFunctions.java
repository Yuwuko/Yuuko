package basketbandit.core.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
            PreparedStatement stmt = conn.prepareStatement("SELECT `serverId` FROM `ServerSettings` WHERE `serverId` = '" + server + "'");
            ResultSet resultSet = stmt.executeQuery();

            if(!resultSet.next()) {
                conn = new DatabaseConnection().getConnection();
                PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO `ServerSettings` (`serverId`) VALUES ('" + server + "')");
                return stmt2.execute();
            } else {
                return false;
            }

        } catch(Exception ex) {
            System.out.println("[ERROR] Unable to add new server to the database. (ID: " + server + ")");
            return false;
        } finally {
            try {
                conn.close();
            } catch(Exception ex) {
                System.out.println("[ERROR] Unable to close connection to database.");
            }
        }
    }

    /**
     * Sets a server's command prefix.
     * @param prefix the command prefix.
     * @param server the target server.
     * @return boolean
     */
    public boolean setServerPrefix(String prefix, String server) {
        try {
            PreparedStatement stmt = conn.prepareStatement("UPDATE `ServerSettings` SET `commandPrefix` = '" + prefix + "' WHERE `serverId` = '" + server + "'");
            return stmt.execute();

        } catch(Exception ex) {
            System.out.println("[ERROR] Unable to set server prefix. (Server: " + server + ")");
            return false;
        } finally {
            try {
                conn.close();
            } catch(Exception ex) {
                System.out.println("[ERROR] Unable to close connection to database.");
            }
        }
    }

    /**
     * Returns the given server's command prefix.
     * @param server the target server.
     * @return String
     */
    public String getServerPrefix(String server) {
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT `commandPrefix` FROM `ServerSettings` WHERE `serverId` = '" + server + "'");
            ResultSet resultSet = stmt.executeQuery();
            resultSet.next();
            return resultSet.getString(1);

        } catch(Exception ex) {
            return null;
        } finally {
            try {
                conn.close();
            } catch(Exception ex) {
                System.out.println("[ERROR] Unable to close connection to database.");
            }
        }
    }

    /**
     * Retrieves all of the server settings for a server.
     * @param server the server id.
     * @return the results of the query.
     */
    public ResultSet getModuleSettings(Connection connection, String server) {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM `ModuleSettings` WHERE `serverId` = '" + server + "'");
            return stmt.executeQuery();

        } catch(Exception ex) {
            System.out.println("[ERROR] Unable to get module settings. (ID: " + server + ")");
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
            PreparedStatement stmt = conn.prepareStatement("SELECT " + moduleName + " FROM `ModuleSettings` WHERE `serverId` = '" + server + "'");
            ResultSet resultSet = stmt.executeQuery();
            resultSet.next();
            return resultSet.getBoolean(1);

        } catch(Exception ex) {
            System.out.println("[ERROR] Unable to get individual module setting. (Module: " + moduleName + ", Server: " + server + ")");
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
            PreparedStatement stmt = conn.prepareStatement("UPDATE `ModuleSettings` SET " + modName + " = NOT " + modName + " WHERE `serverId` = '" + server + "'");
            stmt.execute();
            return checkModuleSettings(modName, server);

        } catch(Exception ex) {
            System.out.println("[ERROR] Unable to toggle module setting. (Module: " + modName + ", Server: " + server + ")");
            return false;
        } finally {
            try {
                conn.close();
            } catch(Exception ex) {
                System.out.println("[ERROR] Unable to close connection to database.");
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
    public boolean addBinding(String modName, String channel, String server) {
        String moduleIn = "module" + modName;

        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM `ModuleBindings` WHERE `serverId` = '" + server + "' AND `channelId` = '" + channel + "' AND `moduleName` = '" + moduleIn + "'");
            ResultSet resultSet = stmt.executeQuery();

            if(!resultSet.next()) {
                conn = new DatabaseConnection().getConnection();
                PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO `ModuleBindings` VALUES ('" + server + "','" + channel + "','" + moduleIn + "', '1', '0')");
                return stmt2.execute();
            } else {
                conn = new DatabaseConnection().getConnection();
                PreparedStatement stmt2 = conn.prepareStatement("UPDATE `ModuleBindings` SET `bound` = '1', `excluded` = '0' WHERE `serverId` = '" + server + "' AND `channelId` = '" + channel + "' AND `moduleName` = '" + moduleIn + "'");
                return stmt2.execute();
            }

        } catch(Exception ex) {
            ex.printStackTrace();
            System.out.println("[ERROR] Unable to bind module to channel. (Module: " + moduleIn + ", Server: " + server + ", Channel: " + channel + ")");
            return false;
        } finally {
            try {
                conn.close();
            } catch(Exception ex) {
                System.out.println("[ERROR] Unable to close connection to database.");
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
    public boolean addExclusion(String modName, String channel, String server) {
        String moduleIn = "module" + modName;

        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM `ModuleBindings` WHERE `serverId` = '" + server + "' AND `channelId` = '" + channel + "' AND `moduleName` = '" + moduleIn + "'");
            ResultSet resultSet = stmt.executeQuery();

            if(!resultSet.next()) {
                conn = new DatabaseConnection().getConnection();
                PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO `ModuleBindings` VALUES ('" + server + "','" + channel + "','" + moduleIn + "', '0', '1')");
                return stmt2.execute();
            } else {
                conn = new DatabaseConnection().getConnection();
                PreparedStatement stmt2 = conn.prepareStatement("UPDATE `ModuleBindings` SET `bound` = '0', `excluded` = '1' WHERE `serverId` = '" + server + "' AND `channelId` = '" + channel + "' AND `moduleName` = '" + moduleIn + "'");
                return stmt2.execute();
            }

        } catch(Exception ex) {
            ex.printStackTrace();
            System.out.println("[ERROR] Unable to exclude module from channel. (Module: " + moduleIn + ", Server: " + server + ", Channel: " + channel + ")");
            return false;
        } finally {
            try {
                conn.close();
            } catch(Exception ex) {
                System.out.println("[ERROR] Unable to close connection to database.");
            }
        }
    }

    /**
     * Removes a binding/exclusion from a channel.
     * @param modName the name of the module.
     * @param channel the idLong of the channel.
     * @param server the idLong of the server.
     * @return boolean
     */
    public boolean removeBindingExclusion(String modName, String channel, String server) {
        String moduleIn = "module" + modName;

        try {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM `ModuleBindings` WHERE `serverId` = '" + server + "' AND `channelId` = '" + channel + "' AND `moduleName` = '" + moduleIn + "'");
            return stmt.execute();

        } catch(Exception ex) {
            System.out.println("[ERROR] Unable to unbind/include module from channel. (Module: " + moduleIn + ", Server: " + server + ")");
            return false;
        } finally {
            try {
                conn.close();
            } catch(Exception ex) {
                ex.printStackTrace();
                System.out.println("[ERROR] Unable to close connection to database.");
            }
        }
    }

    /**
     * Gets the bindings/exclusions for a particular channel.
     * @param server the idLong of the server.
     * @return ResultSet
     */
    public ResultSet getBindingsExclusionsChannel(Connection connection, String server, String modName) {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM `ModuleBindings` WHERE `serverId` = '" + server + "' AND `moduleName` = '" + modName + "'");
            //System.out.println("SELECT * FROM `ModuleBindings` WHERE `serverId` = '" + server + "' AND `moduleName` = '" + modName + "'");
            return stmt.executeQuery();

        } catch(Exception ex) {
            ex.printStackTrace();
            System.out.println("[ERROR] Unable to return bindings/exclusions. (Server: " + server + ")");
            return null;
        }
    }

    /**
     * Gets the bindings/exclusions for a particular server.
     * @param server the idLong of the server.
     * @return ResultSet
     */
    public ResultSet getBindingsExclusionsServer(Connection connection, String server) {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM `ModuleBindings` WHERE `serverId` = '" + server + "'");
            return stmt.executeQuery();

        } catch(Exception ex) {
            System.out.println("[ERROR] Unable to return bindings/exclusions. (Server: " + server + ")");
            return null;
        }
    }

    public void cleanup(String server) {
        try {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM ServerSettings WHERE serverId = '" + server + "'");
            stmt.executeQuery();

        } catch(Exception ex) {
            System.out.println("[ERROR] Unable to remove server from the database. (Server: " + server + ")");
        }
    }

}
