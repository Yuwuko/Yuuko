package com.yuuko.core.database.connections;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class DatabaseConnection {
    static final Logger log = LoggerFactory.getLogger(DatabaseConnection.class);
    static final BasicDataSource connectionPool = new BasicDataSource();

    /**
     * Gets the fresh database connection.
     * @return BasicDataSource.
     */
    public static Connection getConnection() throws SQLException {
        return connectionPool.getConnection();
    }

    /**
     * Gets the number of active (locked) connections to the pool.
     * @return int
     */
    public static int getLockedConnections() {
        return connectionPool.getNumActive();
    }

}
