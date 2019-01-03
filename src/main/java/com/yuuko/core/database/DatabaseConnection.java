package com.yuuko.core.database;

import com.yuuko.core.Configuration;
import com.yuuko.core.Metrics;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final BasicDataSource connectionPool = new BasicDataSource();

    public DatabaseConnection() {
        connectionPool.setDriverClassName("com.mysql.cj.jdbc.Driver");
        connectionPool.setUsername(Configuration.DATABASE_USERNAME);
        connectionPool.setPassword(Configuration.DATABASE_PASSWORD);
        connectionPool.setUrl("jdbc:mysql://" + Configuration.DATABASE_IP + "/" + Configuration.DATABASE_NAME + "?useSSL=false");
        connectionPool.setInitialSize(10);
        connectionPool.setMaxTotal(100);
        connectionPool.setMaxIdle(5);
    }

    /**
     * Gets the fresh database connection.
     * @return BasicDataSource.
     */
    public static Connection getConnection() throws SQLException {
        return connectionPool.getConnection();
    }

    /**
     * Queries the active connections and updates the cache.
     */
    public static void queryConnections() {
        Metrics.DB_POOL_IDLE = connectionPool.getNumIdle();
        Metrics.DB_POOL_ACTIVE = connectionPool.getNumActive();
    }

}
