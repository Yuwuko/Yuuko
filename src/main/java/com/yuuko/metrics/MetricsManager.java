package com.yuuko.metrics;

import com.yuuko.Yuuko;
import com.yuuko.database.connection.DatabaseConnection;
import com.yuuko.database.function.ShardFunctions;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.metrics.pathway.AudioMetrics;
import com.yuuko.metrics.pathway.DiscordMetrics;
import com.yuuko.metrics.pathway.SystemMetrics;
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

    public static void registerShardedDiscordMetrics(int shards) {
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
                PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO `metrics_discord`(`shardId`, `gatewayPing`, `restPing`, `guildCount`, `messageEvents`) VALUES(?, ?, ?, ?, ?)");
                PreparedStatement stmt3 = conn.prepareStatement("INSERT INTO `metrics_audio`(`shardId`, `players`, `activePlayers`, `queueSize`, `trackIdMatch`, `trackIdSize`) VALUES(?, ?, ?, ?, ?, ?)")) {

                // system metrics will is the same for every shard on an instance - for this reason we only update it once (per instance instead of per shard)
                stmt.setInt(1, Yuuko.BOT.getJDA().getShardInfo().getShardId());
                stmt.setLong(2, systemMetrics.UPTIME);
                stmt.setLong(3, systemMetrics.MEMORY_TOTAL);
                stmt.setLong(4, systemMetrics.MEMORY_USED);
                stmt.execute();

                Yuuko.SHARD_MANAGER.getShards().stream().filter(shard -> shard.getStatus().name().equals("CONNECTED")).forEach(shard -> {
                    try {
                        final int shardId = shard.getShardInfo().getShardId();

                        stmt2.setInt(1, shardId);
                        stmt2.setDouble(2, shardedDiscordMetrics.get(shardId).GATEWAY_PING.get());
                        stmt2.setDouble(3, shardedDiscordMetrics.get(shardId).REST_PING.get());
                        stmt2.setInt(4, shardedDiscordMetrics.get(shardId).GUILD_COUNT.get());
                        stmt2.setInt(5, shardedDiscordMetrics.get(shardId).MESSAGE_EVENTS.get());
                        stmt2.execute();

                        stmt3.setInt(1, shardId);
                        stmt3.setInt(2, audioMetrics.PLAYERS_TOTAL.get());
                        stmt3.setInt(3, audioMetrics.PLAYERS_ACTIVE.get());
                        stmt3.setInt(4, audioMetrics.QUEUE_SIZE.get());
                        stmt3.setInt(5, audioMetrics.TRACK_ID_CACHE_HITS.get());
                        stmt3.setInt(6, audioMetrics.TRACK_ID_CACHE_SIZE.get());
                        stmt3.execute();
                    } catch(Exception e) {
                        log.error("An error occurred while updating shard metrics, class {} , message: {}", ShardFunctions.class.getSimpleName(), e.getMessage());
                    }
                });

            } catch(Exception ex) {
                log.error("There was a problem updating metrics, class: {}, message: {}", ShardFunctions.class.getSimpleName(), ex.getMessage(), ex);
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
                log.error("There was a problem updating command metrics, class: {}, message: {}", ShardFunctions.class.getSimpleName(), ex.getMessage(), ex);
            }
        }

        /**
         * Prune metrics tables for any data that is more than 6 hours old.
         */
        public static void pruneMetrics() {
            try(Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement("DELETE FROM `metrics_system` WHERE dateInserted < DATE_SUB(NOW(), INTERVAL 6 HOUR);");
                PreparedStatement stmt2 = conn.prepareStatement("DELETE FROM `metrics_discord` WHERE dateInserted < DATE_SUB(NOW(), INTERVAL 6 HOUR);");
                PreparedStatement stmt3 = conn.prepareStatement("DELETE FROM `metrics_audio` WHERE dateInserted < DATE_SUB(NOW(), INTERVAL 6 HOUR);")) {

                stmt.execute();
                stmt2.execute();
                stmt3.execute();

            } catch(Exception ex) {
                log.error("There was a problem pruning metrics, class: {}, message: {}", ShardFunctions.class.getSimpleName(), ex.getMessage(), ex);
            }
        }
    }
}
