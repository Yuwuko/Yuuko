package com.yuuko.core.database.connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final Logger log = LoggerFactory.getLogger(DatabaseConnection.class);
    private static final HikariConfig config = new HikariConfig("./config/hikari/dbyuuko.properties");
    private static final HikariDataSource connectionPool = new HikariDataSource(config);

    public DatabaseConnection() {
        log.trace("Invoking {}", this.getClass().getName());
    }

    /**
     * Gets the fresh database connection.
     * @return Connection.
     */
    public static Connection getConnection() throws SQLException {
        return connectionPool.getConnection();
    }

}
