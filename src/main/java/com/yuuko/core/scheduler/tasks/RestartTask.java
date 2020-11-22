package com.yuuko.core.scheduler.tasks;

import com.yuuko.core.Config;
import com.yuuko.core.database.function.DatabaseFunctions;
import com.yuuko.core.scheduler.Task;

public class RestartTask implements Task {
    @Override
    public void run() {
        Config.SHARD_MANAGER.getShards().forEach(shard -> {
            if(DatabaseFunctions.hasRestartSignal(shard.getShardInfo().getShardId())) {
                log.warn("Restart signal detected for shard {}, shutting down...", shard.getShardInfo().getShardId());
                Config.SHARD_MANAGER.restart(shard.getShardInfo().getShardId());
                DatabaseFunctions.cancelRestartSignal(shard.getShardInfo().getShardId());
                DatabaseFunctions.updateShardStatistics(shard);
            }
        });
    }
}
