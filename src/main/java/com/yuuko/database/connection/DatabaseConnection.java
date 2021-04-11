package com.yuuko.database.connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final Logger log = LoggerFactory.getLogger(DatabaseConnection.class);
    private static HikariDataSource connectionPool;

    /**
     * Setup database - future use for backup databases.
     */
    public static void setupDatabase() {
        HikariConfig config = new HikariConfig("./config/hikari/externaldb.properties");
        config.setConnectionInitSql("SET NAMES utf8mb4");
        connectionPool = new HikariDataSource(config);

        InputStream sqlStream = DatabaseConnection.class.getClassLoader().getResourceAsStream("db.sql");
        if(sqlStream == null) {
            log.warn("Unable to locate database startup sql script. - If the database already exists, ignore this message.");
            return;
        }

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(sqlStream));
            Connection connection = getConnection()) {

            StringBuilder string = new StringBuilder();
            reader.lines().forEach(string::append);
            String[] queries = string.toString().split(";");
            for(String query : queries) {
                connection.prepareStatement(query).execute();
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
