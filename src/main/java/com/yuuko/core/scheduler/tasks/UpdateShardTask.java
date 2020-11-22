package com.yuuko.core.scheduler.tasks;

import com.yuuko.core.Config;
import com.yuuko.core.database.function.DatabaseFunctions;
import com.yuuko.core.scheduler.Task;

public class UpdateShardTask implements Task {

    @Override
    public void run() {
        Config.SHARD_MANAGER.getShards().forEach(DatabaseFunctions::updateShardStatistics);
    }
}
