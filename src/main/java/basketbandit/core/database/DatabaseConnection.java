package basketbandit.core.database;

import basketbandit.core.Configuration;

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
            connection = DriverManager.getConnection("jdbc:mysql://" + Configuration.DATABASE_IP + "/" + Configuration.DATABASE_NAME + "?useSSL=false", Configuration.DATABASE_USERNAME,Configuration.DATABASE_PASSWORD);
        } catch(Exception ex) {
            System.out.println("[ERROR] Unable to connect to the database. @" + Configuration.DATABASE_IP);
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
