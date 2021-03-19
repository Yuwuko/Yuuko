package com.yuuko.scheduler.tasks;

import com.yuuko.Yuuko;
import com.yuuko.database.function.ShardFunctions;
import com.yuuko.scheduler.Task;

public class UpdateShardTask implements Task {

    @Override
    public void run() {
        Yuuko.SHARD_MANAGER.getShards().forEach(ShardFunctions::updateShardStatistics);
    }
}
