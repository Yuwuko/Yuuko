package com.yuuko.core.database.connections;

import java.io.BufferedReader;
import java.io.FileReader;

public class MetricsDatabaseConnection extends DatabaseConnection{

    public MetricsDatabaseConnection() {
        try {
            log.trace("Invoking {}", this.getClass().getName());
            BufferedReader configuration = new BufferedReader(new FileReader("metrics_configuration.txt"));
            connectionPool.setDriverClassName("com.mysql.cj.jdbc.Driver");
            connectionPool.setUsername(configuration.readLine());
            connectionPool.setPassword(configuration.readLine());
            connectionPool.setUrl("jdbc:mysql://" + configuration.readLine() + "/" + configuration.readLine() + "?useSSL=true");
            connectionPool.setInitialSize(5);
            connectionPool.setMaxTotal(100);
            connectionPool.setMaxIdle(5);
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this.getClass().getSimpleName(), ex.getMessage(), ex);
        }
    }

}
