package com.yuuko.core.database.connections;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.SQLException;

public class SettingsDatabaseConnection {
    private static final Logger log = LoggerFactory.getLogger(SettingsDatabaseConnection.class);
    private static final BasicDataSource connectionPool = new BasicDataSource();

    public SettingsDatabaseConnection() {
        try {
            log.trace("Invoking {}", this.getClass().getName());
            BufferedReader configuration = new BufferedReader(new FileReader("settings_configuration.txt"));
            connectionPool.setDriverClassName("com.mysql.cj.jdbc.Driver");
            connectionPool.setUsername(configuration.readLine());
            connectionPool.setPassword(configuration.readLine());
            connectionPool.setUrl("jdbc:mysql://" + configuration.readLine() + "/" + configuration.readLine() + "?useSSL=true");
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
