package com.yuuko.scheduler.tasks;

import com.yuuko.Yuuko;
import com.yuuko.database.function.ShardFunctions;
import com.yuuko.scheduler.Task;

public class ShutdownTask implements Task {
    @Override
    public void run() {
        // Use shard SHARD_IDS here because JDA removes JDA instance from SHARD_MANAGER after it has shutdown.
        Yuuko.SHARD_IDS.forEach(shard -> {
            if(ShardFunctions.hasShutdownSignal(shard)) {
                log.warn("Shutdown signal detected for shard {}, shutting down...", shard);
                Yuuko.SHARD_MANAGER.shutdown(shard);
                ShardFunctions.cancelShutdownSignal(shard);
                ShardFunctions.updateShardShutdown(shard);
            }
        });
    }
}
