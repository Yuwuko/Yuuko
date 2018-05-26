package basketbandit.core;

import org.h2.jdbcx.JdbcDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Database {

    private Connection connection;

    /**
     * Database constructor.
     */
    public Database() {
        try {
            JdbcDataSource ds = new JdbcDataSource();
            ds.setURL("jdbc:h2:~" + Configuration.DATABASE_URL);
            ds.setUser("admin");
            ds.setPassword("password");

            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection ("jdbc:h2:~" + Configuration.DATABASE_URL + ";mode=mysql", "admin","password");

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Initial setup for the database. (Dev only)
     */
    public boolean setupDatabase() {
        Connection conn;
        PreparedStatement stmt;

        try {
            conn = connection;
            stmt = conn.prepareStatement(
                "CREATE TABLE `Settings` (" +
                "`id` INT(9) NOT NULL AUTO_INCREMENT,\n" +
                "`server` varchar(18) NOT NULL,\n" +
                "`modDev` BOOLEAN NOT NULL DEFAULT '0',\n" +
                "`modModeration` BOOLEAN NOT NULL DEFAULT '0',\n" +
                "`modMusic` BOOLEAN NOT NULL DEFAULT '0',\n" +
                "`modCustom` BOOLEAN NOT NULL DEFAULT '1',\n" +
                "`modUtility` BOOLEAN NOT NULL DEFAULT '1',\n" +
                "`modTransport` BOOLEAN NOT NULL DEFAULT '0',\n" +
                "`modLogging` BOOLEAN NOT NULL DEFAULT '0',\n" +
                "`modMath` BOOLEAN NOT NULL DEFAULT '0',\n" +
                "`modFun` BOOLEAN NOT NULL DEFAULT '0',\n" +
                "`modGame` BOOLEAN NOT NULL DEFAULT '0',\n" +
                "PRIMARY KEY (`id`,`server`)\n" +
                ");" +

                "CREATE TABLE `CustomCommands` (\n" +
                "`id` INT(9) NOT NULL AUTO_INCREMENT,\n" +
                "`server` varchar(18) NOT NULL,\n" +
                "`commandName` varchar(10) NOT NULL,\n" +
                "`commandContents` varchar(2000) NOT NULL,\n" +
                "`commandAuthor` varchar(18) NOT NULL, \n" +
                "PRIMARY KEY (`id`)\n" +
                ");\n" +

                "ALTER TABLE `CustomCommands` ADD CONSTRAINT `CustomCommands_fk0` FOREIGN KEY (`server`) REFERENCES `Settings`(`server`) ON DELETE CASCADE;"
            );
            return stmt.execute();

        } catch(Exception e) {
            System.out.println("[ERROR] Unable to initiate database.");
            return false;
        }
    }

    /**
     * Adds a new server to the database and initialises it's settings.
     * @param server the server to add.
     * @return if the add was successful.
     */
    public boolean addNewServer(String server) {
        Connection conn;
        PreparedStatement stmt;
        ResultSet rs;

        try {
            conn = connection;
            stmt = conn.prepareStatement("SELECT `id` FROM `Settings` WHERE `server` = '" + server + "'");
            rs = stmt.executeQuery();

            if(!rs.next()) {
                stmt = conn.prepareStatement("INSERT INTO `Settings` (`server`) VALUES ('" + server + "')");
                return stmt.execute();
            } else {
                return false;
            }

        } catch(Exception e) {
            System.out.println("[ERROR] Unable to add new server to the database. (ID: " + server + ")");
        }

        return true;
    }

    /**
     * Retrieves all of the server settings for a server.
     * @param server the server id.
     * @return the results of the query.
     */
    public ResultSet getModuleSettings(String server) {
        Connection conn;
        PreparedStatement stmt;
        ResultSet rs;

        try {
            conn = connection;
            stmt = conn.prepareStatement("SELECT * FROM `Settings` WHERE `server` = '" + server + "'");
            rs = stmt.executeQuery();
            return rs;

        } catch(Exception e) {
            System.out.println("[ERROR] Unable to get module settings. (ID: " + server + ")");
        }

        return null;
    }

    /**
     * Checks to see if a module is active before parsing a command.
     * @param modName the name of the module.
     * @return (boolean) if the module is active or not.
     */
    public boolean checkModuleSettings(String modName, String server) {
        Connection conn;
        PreparedStatement stmt;
        ResultSet rs;

        try {
            conn = connection;
            stmt = conn.prepareStatement("SELECT " + modName + " FROM `Settings` WHERE `server` = '" + server + "'");
            rs = stmt.executeQuery();
            rs.next();
            return rs.getBoolean(1);

        } catch(Exception e) {
            System.out.println("[ERROR] Unable to get individual module setting. (Module: " + modName + ", Server: " + server + ")");
        }

        return false;
    }

    /**
     * Toggles a module for a server, returns the new value.
     * @param modName the module to toggle
     * @param server the server in which the module is to be toggled
     * @return the new value of the setting
     */
    public boolean toggleModule(String modName, String server) {
        Connection conn;
        PreparedStatement stmt;
        boolean res;

        try {
            conn = connection;
            stmt = conn.prepareStatement("UPDATE `Settings` SET " + modName + " = NOT " + modName);
            stmt.execute();
            res = checkModuleSettings(modName, server);
            return res;

        } catch(Exception e) {
            System.out.println("[ERROR] Unable to toggle module setting. (Module: " + modName + ", Server: " + server + ")");
        }

        return false;
    }

    /**
     * Adds a custom command to the database. (Custom modules must be unique.)
     * @param commandName the command name.
     * @param commandContents the contents of the command.
     * @param server the server the command is on.
     * @param commandAuthor the author of the command.
     * @return if the command was added successfully.
     */
    public boolean addCustomCommand(String commandName, String commandContents, String server, String commandAuthor) {
        Connection conn;
        PreparedStatement stmt;
        ResultSet rs;

        try {
            conn = connection;
            stmt = conn.prepareStatement("SELECT commandContents FROM `CustomCommands` WHERE `commandName` = '" + commandName + "' AND `server` = '" + server + "'");
            rs = stmt.executeQuery();

            if(rs.next()) {
                return false;
            }
            stmt = conn.prepareStatement("INSERT INTO `CustomCommands` (`server`, `commandName`, `commandContents`, `commandAuthor`) VALUES ('" + server + "', '" + commandName + "', '" + commandContents + "', '" + commandAuthor + "')");
            return stmt.execute();

        } catch(Exception e) {
            System.out.println("[ERROR] Unable to create new custom command. (Command: " + commandName + ", Server: " + server + ")");
            return false;
        }

    }

    /**
     * Removes a custom command.
     * @param commandName name of the command.
     * @param server name of the server.
     * @return if the removal was successful.
     */
    public boolean removeCustomCommand(String commandName, String server) {
        Connection conn;
        PreparedStatement stmt;

        try {
            conn = connection;
            stmt = conn.prepareStatement("DELETE FROM `CustomCommands` WHERE `commandName` = '" + commandName + "' AND `server` = '" + server + "'");
            return stmt.execute();

        } catch(Exception e) {
            System.out.println("[ERROR] Unable to remove custom command. (Command: " + commandName + ", Server: " + server + ")");
            return false;
        }

    }

    /**
     * Returns the contents of a custom command.
     * @param commandName the command name.
     * @param server the server the command is on.
     * @return the command contents.
     */
    public String getCustomCommand(String commandName, String server) {
        Connection conn;
        PreparedStatement stmt;
        ResultSet rs;
        String res;

        try {
            conn = connection;
            stmt = conn.prepareStatement("SELECT commandContents FROM `CustomCommands` WHERE `commandName` = '" + commandName + "' AND `server` = '" + server + "'");
            rs = stmt.executeQuery();

            if(rs.next()) {
                res = rs.getNString(1);
                return res;
            } else {
                return null;
            }
        } catch(Exception e) {
            System.out.println("[ERROR] Unable to retrieve custom command. (Command: " + commandName + ", Server: " + server + ")");
            return null;
        }

    }

}
