package com.yuuko.core.scheduler.tasks;

import com.yuuko.core.Configuration;
import com.yuuko.core.database.DatabaseFunctions;
import com.yuuko.core.metrics.DiscordMetrics;
import com.yuuko.core.metrics.handlers.MetricsManager;
import com.yuuko.core.scheduler.Task;
import net.dv8tion.jda.core.JDA;

public class UpdateShardTask implements Task {
    private static JDA shard = Configuration.SHARD_MANAGER.getShardById(Configuration.SHARD_ID);

    @Override
    public void handle() {
        DiscordMetrics metrics = MetricsManager.getDiscordMetrics();
        DatabaseFunctions.updateShardStatistics(Configuration.SHARD_ID, shard.getStatus().name(), metrics.GUILD_COUNT, metrics.USER_COUNT, metrics.CHANNEL_COUNT, (int)metrics.PING.get());
    }
}
