package com.yuuko.core.scheduler.tasks;

import com.yuuko.core.Configuration;
import com.yuuko.core.database.DatabaseFunctions;
import com.yuuko.core.metrics.handlers.MetricsManager;
import com.yuuko.core.scheduler.Task;
import net.dv8tion.jda.core.JDA;

public class UpdateShardStatisticsTask implements Task {

    private static JDA shard = Configuration.SHARD_MANAGER.getShardById(Configuration.SHARD_ID);

    @Override
    public void handle() {
        DatabaseFunctions.updateShardStatistics(Configuration.SHARD_ID, shard.getStatus().name(), MetricsManager.getDiscordMetrics().GUILD_COUNT, MetricsManager.getDiscordMetrics().USER_COUNT);
    }
}
