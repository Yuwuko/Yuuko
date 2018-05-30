package basketbandit.core.database;

import basketbandit.core.Configuration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DatabaseFunctions {

    private Connection conn;

    public DatabaseFunctions() {
        conn = new DatabaseConnection().getConnection();
    }

    /**
     * Initial setup for the database. (Dev only)
     */
    public boolean setupDatabase() {
        try {
            PreparedStatement stmt = conn.prepareStatement("CREATE TABLE `ServerSettings` (`id` INT(9) NOT NULL AUTO_INCREMENT, `serverid` VARCHAR(18) NOT NULL, `modModeration` BOOLEAN NOT NULL DEFAULT '1', `modAudio` BOOLEAN NOT NULL DEFAULT '1', `modCustom` BOOLEAN NOT NULL DEFAULT '1', `modUtility` BOOLEAN NOT NULL DEFAULT '1', `modTransport` BOOLEAN NOT NULL DEFAULT '1', `modLogging` BOOLEAN NOT NULL DEFAULT '0', `modMath` BOOLEAN NOT NULL DEFAULT '0',`modFun` BOOLEAN NOT NULL DEFAULT '0', `modGame` BOOLEAN NOT NULL DEFAULT '0', PRIMARY KEY (`id`,`serverid`)) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;");
            return stmt.execute();

        } catch(Exception ex) {
            System.out.println("[ERROR] Unable to initiate database. @" + Configuration.DATABASE_IP);
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
     * Adds a new server to the database and initialises it's settings.
     * @param server the server to add.
     * @return if the add was successful.
     */
    public boolean addNewServer(String server) {
        try {
            if(server.length() != 18) {
                return false;
            }

            PreparedStatement stmt = conn.prepareStatement("SELECT `id` FROM `ServerSettings` WHERE `serverid` = '" + server + "'");
            ResultSet resultSet = stmt.executeQuery();

            if(!resultSet.next()) {
                conn = new DatabaseConnection().getConnection();
                PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO `ServerSettings` (`serverid`) VALUES ('" + server + "')");
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
     * Retrieves all of the server settings for a server.
     * @param server the server id.
     * @return the results of the query.
     */
    public ResultSet getModuleSettings(String server) {
        try {
            if(server.length() != 18) {
                return null;
            }

            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM `ServerSettings` WHERE `serverid` = '" + server + "'");
            return stmt.executeQuery();

        } catch(Exception ex) {
            System.out.println("[ERROR] Unable to get module settings. (ID: " + server + ")");
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
     * Checks to see if a module is active before parsing a command.
     * @param modName the name of the module.
     * @return (boolean) if the module is active or not.
     */
    public boolean checkModuleSettings(String modName, String server) {
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT " + modName + " FROM `ServerSettings` WHERE `serverid` = '" + server + "'");
            ResultSet resultSet = stmt.executeQuery();
            resultSet.next();
            return resultSet.getBoolean(1);

        } catch(Exception ex) {
            System.out.println("[ERROR] Unable to get individual module setting. (Module: " + modName + ", Server: " + server + ")");
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
     * Toggles a module for a server, returns the new value.
     * @param modName the module to toggle
     * @param server the server in which the module is to be toggled
     * @return the new value of the setting
     */
    public boolean toggleModule(String modName, String server) {
        try {
            if(server.length() != 18) {
                return false;
            }

            PreparedStatement stmt = conn.prepareStatement("UPDATE `ServerSettings` SET " + modName + " = NOT " + modName + " WHERE `serverid` = '" + server + "'");
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

}
