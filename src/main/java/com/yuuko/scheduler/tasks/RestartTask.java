package com.yuuko.scheduler.tasks;

import com.yuuko.Yuuko;
import com.yuuko.database.function.ShardFunctions;
import com.yuuko.scheduler.Task;

public class RestartTask implements Task {
    @Override
    public void run() {
        // Use shard SHARD_IDS here because JDA removes JDA instance from SHARD_MANAGER after it has shutdown.
        Yuuko.SHARD_IDS.forEach(shard -> {
            if(ShardFunctions.hasRestartSignal(shard)) {
                log.warn("Restart signal detected for shard {}, restarting...", shard);
                Yuuko.SHARD_MANAGER.restart(shard);
                ShardFunctions.cancelRestartSignal(shard);
                ShardFunctions.updateShardRestart(shard);
            }
        });
    }
}
