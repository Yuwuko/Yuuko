package com.yuuko.core.scheduler.tasks;

import com.yuuko.core.Config;
import com.yuuko.core.database.function.DatabaseFunctions;
import com.yuuko.core.scheduler.Task;

public class ShutdownTask implements Task {
    @Override
    public void run() {
        Config.SHARD_MANAGER.getShards().forEach(shard -> {
            if(DatabaseFunctions.hasShutdownSignal(shard.getShardInfo().getShardId())) {
                log.warn("Shutdown signal detected for shard {}, shutting down...", shard.getShardInfo().getShardId());
                Config.SHARD_MANAGER.shutdown(shard.getShardInfo().getShardId());
                DatabaseFunctions.cancelShutdownSignal(shard.getShardInfo().getShardId());
                DatabaseFunctions.updateShardStatistics(shard);
            }
        });
    }
}
