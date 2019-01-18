package com.yuuko.core.database.connections;

import com.yuuko.core.Configuration;

public class SettingsDatabaseConnection extends DatabaseConnection {

    public SettingsDatabaseConnection() {
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

}
