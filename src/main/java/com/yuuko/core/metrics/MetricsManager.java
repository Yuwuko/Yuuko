package com.yuuko.core.metrics;

import com.yuuko.core.Config;
import com.yuuko.core.database.connection.DatabaseConnection;
import com.yuuko.core.database.function.ShardFunctions;
import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.metrics.pathway.AudioMetrics;
import com.yuuko.core.metrics.pathway.CacheMetrics;
import com.yuuko.core.metrics.pathway.DiscordMetrics;
import com.yuuko.core.metrics.pathway.SystemMetrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class MetricsManager {
    private static final Logger log = LoggerFactory.getLogger(MetricsManager.class);
    private static final List<DiscordMetrics> shardedDiscordMetrics = new ArrayList<>();
    private static final SystemMetrics systemMetrics = new SystemMetrics();
    private static final AudioMetrics audioMetrics = new AudioMetrics();
    private static final CacheMetrics cacheMetrics = new CacheMetrics();

    public MetricsManager(int shards) {
        for(int i = 0; i < shards; i++) {
            shardedDiscordMetrics.add(new DiscordMetrics(i));
        }
    }

    public static List<DiscordMetrics> getShardedDiscordMetrics() {
        return shardedDiscordMetrics;
    }

    public static DiscordMetrics getDiscordMetrics(int shardId) {
        return shardedDiscordMetrics.get(shardId);
    }

    public static AudioMetrics getAudioMetrics() { return audioMetrics; }

    public static SystemMetrics getSystemMetrics() {
        return systemMetrics;
    }

    public static CacheMetrics getCacheMetrics() {
        return cacheMetrics;
    }

    /**
     * Inner-class container for all database-related functions.
     */
    public static class DatabaseInterface {
        /**
         * Updates metrics tables with latest data.
         */
        public static void updateMetrics() {
            try(Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO `metrics_system`(`shardId`, `uptime`, `memoryTotal`, `memoryUsed`) VALUES(?, ?, ?, ?)");
                PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO `metrics_discord`(`shardId`, `gatewayPing`, `restPing`, `guildCount`) VALUES(?, ?, ?, ?)");
                PreparedStatement stmt3 = conn.prepareStatement("INSERT INTO `metrics_audio`(`players`, `activePlayers`, `queueSize`) VALUES(?, ?, ?)");
                PreparedStatement stmt4 = conn.prepareStatement("INSERT INTO `metrics_cache`(`trackIdMatch`, `trackIdSize`) VALUES(?, ?)")) {

                // system metrics will is the same for every shard on an instance - for this reason we only update it once (per instance instead of per shard)
                stmt.setInt(1, Config.BOT.getJDA().getShardInfo().getShardId());
                stmt.setLong(2, systemMetrics.UPTIME);
                stmt.setLong(3, systemMetrics.MEMORY_TOTAL);
                stmt.setLong(4, systemMetrics.MEMORY_USED);
                stmt.execute();

                Config.SHARD_MANAGER.getShards().stream().filter(shard -> shard.getStatus().name().equals("CONNECTED")).forEach(shard -> {
                    try {
                        stmt2.setInt(1, shard.getShardInfo().getShardId());
                        stmt2.setDouble(2, shardedDiscordMetrics.get(shard.getShardInfo().getShardId()).GATEWAY_PING.get());
                        stmt2.setDouble(3, shardedDiscordMetrics.get(shard.getShardInfo().getShardId()).REST_PING.get());
                        stmt2.setInt(4, shardedDiscordMetrics.get(shard.getShardInfo().getShardId()).GUILD_COUNT.get());
                        stmt2.execute();
                    } catch(Exception e) {
                        log.error("An error occurred while running the {} class, message: {}", ShardFunctions.class.getSimpleName(), e.getMessage(), e);
                    }
                });

                stmt3.setInt(1, audioMetrics.PLAYERS_TOTAL.get());
                stmt3.setInt(2, audioMetrics.PLAYERS_ACTIVE.get());
                stmt3.setInt(3, audioMetrics.QUEUE_SIZE.get());
                stmt3.execute();

                stmt4.setInt(1, cacheMetrics.TRACK_ID_CACHE_HITS.get());
                stmt4.setInt(2, cacheMetrics.TRACK_ID_CACHE_SIZE.get());
                stmt4.execute();

            } catch(Exception ex) {
                log.error("An error occurred while running the {} class, message: {}", ShardFunctions.class.getSimpleName(), ex.getMessage(), ex);
            }
        }

        /**
         * Update command log using given command from {@link MessageEvent} paramater.
         */
        public static void updateCommandMetric(MessageEvent e, double executionTime) {
            try(Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO `metrics_command`(`shardId`, `guildId`, `command`, `executionTime`) VALUES(?, ?, ?, ?)")) {

                stmt.setInt(1, e.getShardId());
                stmt.setString(2, e.getGuild().getId());
                stmt.setString(3, e.getCommand().getName());
                stmt.setDouble(4, executionTime);
                stmt.execute();

            } catch (Exception ex) {
                log.error("An error occurred while running the {} class, message: {}", ShardFunctions.class.getSimpleName(), ex.getMessage(), ex);
            }
        }

        /**
         * Prune metrics tables for any data that is more than 6 hours old.
         */
        public static void pruneMetrics() {
            try(Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement("DELETE FROM `metrics_system` WHERE dateInserted < DATE_SUB(NOW(), INTERVAL 6 HOUR);");
                PreparedStatement stmt2 = conn.prepareStatement("DELETE FROM `metrics_discord` WHERE dateInserted < DATE_SUB(NOW(), INTERVAL 6 HOUR);");
                PreparedStatement stmt3 = conn.prepareStatement("DELETE FROM `metrics_audio` WHERE dateInserted < DATE_SUB(NOW(), INTERVAL 6 HOUR);");
                PreparedStatement stmt4 = conn.prepareStatement("DELETE FROM `metrics_cache` WHERE dateInserted < DATE_SUB(NOW(), INTERVAL 6 HOUR);")) {

                stmt.execute();
                stmt2.execute();
                stmt3.execute();
                stmt4.execute();

            } catch(Exception ex) {
                log.error("An error occurred while running the {} class, message: {}", ShardFunctions.class.getSimpleName(), ex.getMessage(), ex);
            }
        }
    }
}
