// Program: BasketBandit (Discord Bot)
// Programmer: Joshua Mark Hunt
// Version: 02/05/2018 - JDK 10.0.1

package basketbandit.core.module;

import org.h2.jdbcx.JdbcDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Database {

    private Connection connection;

    /**
     * Database constructor.
     */
    Database() {
        try {
            JdbcDataSource ds = new JdbcDataSource();
            ds.setURL("jdbc:h2:˜/Dropbox/GitHub/BasketBandit/database/dbBasketBandit");
            ds.setUser("admin");
            ds.setPassword("password");

            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection ("jdbc:h2:~/Dropbox/GitHub/BasketBandit/database/dbBasketBandit;mode=mysql", "admin","password");

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Initial setup for the database. (Dev only)
     */
    void setupDatabase() {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(
                    "CREATE TABLE `Settings` (\n" +
                    "\t`id` INT(9) NOT NULL AUTO_INCREMENT,\n" +
                    "\t`server` varchar(18) NOT NULL,\n" +
                    "\t`modDev` BOOLEAN NOT NULL DEFAULT '0',\n" +
                    "\t`modModeration` BOOLEAN NOT NULL DEFAULT '0',\n" +
                    "\t`modMusic` BOOLEAN NOT NULL DEFAULT '0',\n" +
                    "\t`modCustom` BOOLEAN NOT NULL DEFAULT '1',\n" +
                    "\t`modUtility` BOOLEAN NOT NULL DEFAULT '1',\n" +
                    "\t`modLogging` BOOLEAN NOT NULL DEFAULT '0',\n" +
                    "\t`modMath` BOOLEAN NOT NULL DEFAULT '0',\n" +
                    "\t`modFun` BOOLEAN NOT NULL DEFAULT '0',\n" +
                    "\t`modRuneScape` BOOLEAN NOT NULL DEFAULT '0',\n" +
                    "\tPRIMARY KEY (`id`,`server`)\n" +
                    ");\n" +
                    "\n" +
                    "CREATE TABLE `CustomCommands` (\n" +
                    "\t`id` INT(9) NOT NULL AUTO_INCREMENT,\n" +
                    "\t`server` varchar(18) NOT NULL,\n" +
                    "\t`commandName` varchar(10) NOT NULL,\n" +
                    "\t`commandContents` varchar(2000) NOT NULL,\n" +
                    "\t`commandAuthor` varchar(18) NOT NULL, \n" +
                    "\tPRIMARY KEY (`id`)\n" +
                    ");\n" +
                    "\n" +
                    "ALTER TABLE `CustomCommands` ADD CONSTRAINT `CustomCommands_fk0` FOREIGN KEY (`server`) REFERENCES `Settings`(`server`) ON DELETE CASCADE;"
            );
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a new server to the database and initialises it's settings.
     * @param server the server to add.
     * @return if the add was successful.
     */
    boolean addNewServer(String server) {
        try {
            Statement statementReturn = connection.createStatement();
            ResultSet resultSet = statementReturn.executeQuery("SELECT id FROM `Settings` WHERE server = " + server);

            if(!resultSet.next()) {
                Statement statementLogic = connection.createStatement();
                statementLogic.executeUpdate("INSERT INTO `Settings` (server) VALUES (" + server + ")");
                return true;
            } else {
                return false;
            }

        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves all of the server settings for a server.
     * @param server the server id.
     * @return the results of the query.
     */
    ResultSet getModuleSettings(String server) {
        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery("SELECT * FROM `Settings` WHERE server = " + server);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Checks to see if a module is active before parsing a command.
     * @param modName the name of the module.
     * @return (boolean) if the module is active or not.
     */
    boolean checkModuleSettings(String modName, String server) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT " + modName + " FROM `Settings` WHERE server = " + server);

            resultSet.next();
            return resultSet.getBoolean(1);

        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Toggles a module for a server, returns the new value.
     * @param modName the module to toggle
     * @param server the server in which the module is to be toggled
     * @return the new value of the setting
     */
    boolean toggleModule(String modName, String server) {
        try {
            Statement statementLogic = connection.createStatement();
            statementLogic.executeUpdate("UPDATE `Settings` SET " + modName + " = NOT " + modName);

            Statement statementReturn = connection.createStatement();
            ResultSet resultSet = statementReturn.executeQuery("SELECT " + modName + " FROM `Settings` WHERE server = '" + server + "'");

            resultSet.next();
            return resultSet.getBoolean(1);

        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Adds a custom command to the database. (Custom commands must be unique.)
     * @param commandName the command name.
     * @param commandContents the contents of the command.
     * @param server the server the command is on.
     * @param commandAuthor the author of the command.
     * @return if the command was added successfully.
     */
    boolean addCustomCommand(String commandName, String commandContents, String server, String commandAuthor) {
        try {
            Statement statementCheck = connection.createStatement();
            ResultSet resultSet = statementCheck.executeQuery("SELECT commandContents FROM `CustomCommands` WHERE commandName = '" + commandName + "' AND server = '" + server + "'");
            if(resultSet.next()) {
                return false;
            }

            Statement statement = connection.createStatement();
            statement.executeUpdate("INSERT INTO `CustomCommands` (server, commandName, commandContents, commandAuthor) VALUES ('" + server + "', '" + commandName + "', '" + commandContents + "', '" + commandAuthor + "')");
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Removes a custom command.
     * @param commandName name of the command.
     * @param server name of the server.
     * @return if the removal was successful.
     */
    boolean removeCustomCommand(String commandName, String server) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM `CustomCommands` WHERE commandName = '" + commandName + "' AND server = '" + server + "'");
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Returns the contents of a custom command.
     * @param commandName the command name.
     * @param server the server the command is on.
     * @return the command contents.
     */
    String getCustomCommand(String commandName, String server) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT commandContents FROM `CustomCommands` WHERE commandName = '" + commandName + "' AND server = '" + server + "'");

            if(resultSet.next()) {
                return resultSet.getNString(1);
            } else {
                return "This command does not exist.";
            }
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
