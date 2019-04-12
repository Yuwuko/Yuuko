package com.yuuko.core.database.connections;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.SQLException;

public class ProvisioningDatabaseConnection {
    private static final Logger log = LoggerFactory.getLogger(ProvisioningDatabaseConnection.class);
    private static final BasicDataSource connectionPool = new BasicDataSource();

    public ProvisioningDatabaseConnection() {
        try {
            log.trace("Invoking {}", this.getClass().getName());
            BufferedReader configuration = new BufferedReader(new FileReader("./config/provisioning_configuration.txt"));
            connectionPool.setDriverClassName("com.mysql.cj.jdbc.Driver");
            connectionPool.setUsername(configuration.readLine());
            connectionPool.setPassword(configuration.readLine());
            connectionPool.setUrl("jdbc:mysql://" + configuration.readLine() + "/" + configuration.readLine() + "?useSSL=true&serverTimezone=UTC");
            connectionPool.setInitialSize(1);
            connectionPool.setMaxTotal(10);
            connectionPool.setMaxIdle(2);
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
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

