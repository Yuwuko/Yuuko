package com.yuuko.core.scheduler.tasks;

import com.yuuko.core.Configuration;
import com.yuuko.core.database.DatabaseFunctions;
import com.yuuko.core.scheduler.Task;

public class RenewShardTask implements Task {

    @Override
    public void handle() {
        DatabaseFunctions.renewShardId(Configuration.SHARD_ID);
    }
}
