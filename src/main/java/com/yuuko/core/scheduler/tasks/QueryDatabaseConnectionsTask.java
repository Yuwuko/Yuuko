package com.yuuko.core.scheduler.tasks;

import com.yuuko.core.database.DatabaseConnection;
import com.yuuko.core.scheduler.Task;

public class QueryDatabaseConnectionsTask implements Task {

    @Override
    public void handle() {
        DatabaseConnection.queryConnections();
    }
}
