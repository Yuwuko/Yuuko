package com.yuuko.core.database.connections;

import com.yuuko.core.Configuration;
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final Logger log = LoggerFactory.getLogger(DatabaseConnection.class);
    private static final BasicDataSource connectionPool = new BasicDataSource();

    public DatabaseConnection() {
        try {
            log.trace("Invoking {}", this.getClass().getName());
            connectionPool.setDriverClassName("com.mysql.cj.jdbc.Driver");
            connectionPool.setUsername(Configuration.DATABASE_USERNAME);
            connectionPool.setPassword(Configuration.DATABASE_PASSWORD);
            connectionPool.setUrl("jdbc:mysql://" + Configuration.DATABASE_IP + "/" + Configuration.DATABASE_NAME + "?useSSL=false");
            connectionPool.setInitialSize(10);
            connectionPool.setMaxTotal(100);
            connectionPool.setMaxIdle(5);
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this.getClass().getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Gets the fresh database connection.
     * @return BasicDataSource.
     */
    public static Connection getConnection() throws SQLException {
        return connectionPool.getConnection();
    }

}
