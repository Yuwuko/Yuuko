package com.yuuko.core.database.connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;

public final class DatabaseConnection {
    private static final Logger log = LoggerFactory.getLogger(DatabaseConnection.class);
    private static HikariDataSource connectionPool;

    /**
     * Setup database - future use for backup databases.
     */
    public static void setupDatabase() {
        HikariConfig config = new HikariConfig("./config/hikari/externaldb.properties");
        connectionPool = new HikariDataSource(config);

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(DatabaseConnection.class.getClassLoader().getResourceAsStream("db.sql")))) {
            StringBuilder string = new StringBuilder();
            reader.lines().forEach(string::append);
            String[] queries = string.toString().split(";");
            for(String query : queries) {
                Connection connection = getConnection();
                connection.prepareStatement(query).execute();
                connection.close();
            }
        } catch(Exception e) {
            log.error("There was a problem running the startup sql database scripts, message: {}", e.getMessage());
        }
    }

    /**
     * Get a fresh database connection.
     * @return Connection.
     */
    public static Connection getConnection() throws SQLException {
        return connectionPool.getConnection();
    }

}
