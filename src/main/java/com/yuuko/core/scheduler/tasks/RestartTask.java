package com.yuuko.core.scheduler.tasks;

import com.yuuko.core.Config;
import com.yuuko.core.database.function.DatabaseFunctions;
import com.yuuko.core.scheduler.Task;

public class RestartTask implements Task {
    @Override
    public void run() {
        // Use shard SHARD_IDS here because JDA removes JDA instance from SHARD_MANAGER after it has shutdown.
        Config.SHARD_IDS.forEach(shard -> {
            if(DatabaseFunctions.hasRestartSignal(shard)) {
                log.warn("Restart signal detected for shard {}, restarting...", shard);
                Config.SHARD_MANAGER.restart(shard);
                DatabaseFunctions.cancelRestartSignal(shard);
                DatabaseFunctions.updateShardRestart(shard);
            }
        });
    }
}
