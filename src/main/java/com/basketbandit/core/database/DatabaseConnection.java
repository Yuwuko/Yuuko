package com.basketbandit.core.database;

import com.basketbandit.core.Configuration;
import com.basketbandit.core.utils.Utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

    private Connection connection;

    /**
     * Database constructor.
     */
    public DatabaseConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + Configuration.DATABASE_IP + "/" + Configuration.DATABASE_NAME + "?useSSL=false", Configuration.DATABASE_USERNAME, Configuration.DATABASE_PASSWORD);
        } catch(Exception ex) {
            Utils.consoleOutput("[ERROR] Unable to connect to the database. @" + Configuration.DATABASE_IP);
        }
    }

    /**
     * Gets the fresh database connection.
     * @return connection.
     */
    public Connection getConnection() {
        return connection;
    }

}
