package com.yuuko.core.database.function;

import com.yuuko.core.Config;
import com.yuuko.core.database.connection.DatabaseConnection;
import com.yuuko.core.entity.Shard;
import com.yuuko.core.metrics.MetricsManager;
import com.yuuko.core.metrics.pathway.AudioMetrics;
import com.yuuko.core.metrics.pathway.CacheMetrics;
import com.yuuko.core.metrics.pathway.DiscordMetrics;
import com.yuuko.core.metrics.pathway.SystemMetrics;
import net.dv8tion.jda.api.JDA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DatabaseFunctions {
    private static final Logger log = LoggerFactory.getLogger(DatabaseFunctions.class);
    private static final SystemMetrics system = MetricsManager.getSystemMetrics();
    private static final ArrayList<DiscordMetrics> discord = MetricsManager.getDiscordMetricsList();
    private static final AudioMetrics audio = MetricsManager.getAudioMetrics();
    private static final CacheMetrics cache = MetricsManager.getCacheMetrics();

    /**
     * Updates the database with the latest metrics.
     */
    public static void updateMetricsDatabase() {
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO `system_metrics`(`shardId`, `uptime`, `memoryTotal`, `memoryUsed`) VALUES(?, ?, ?, ?)");
            PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO `discord_metrics`(`shardId`, `gatewayPing`, `restPing`, `guildCount`) VALUES(?, ?, ?, ?)");
            PreparedStatement stmt3 = conn.prepareStatement("INSERT INTO `audio_metrics`(`players`, `activePlayers`, `queueSize`) VALUES(?, ?, ?)");
            PreparedStatement stmt4 = conn.prepareStatement("INSERT INTO `cache_metrics`(`trackIdMatch`, `trackIdSize`) VALUES(?, ?)")) {

            // system metrics will is the same for every shard on an instance - for this reason we only update it once (per instance instead of per shard)
            stmt.setInt(1, Config.BOT.getJDA().getShardInfo().getShardId());
            stmt.setLong(2, system.UPTIME);
            stmt.setLong(3, system.MEMORY_TOTAL);
            stmt.setLong(4, system.MEMORY_USED);
            stmt.execute();

            Config.SHARD_MANAGER.getShards().stream().filter(shard -> shard.getStatus().name().equals("CONNECTED")).forEach(shard -> {
                try {
                    stmt2.setInt(1, shard.getShardInfo().getShardId());
                    stmt2.setDouble(2, discord.get(shard.getShardInfo().getShardId()).GATEWAY_PING.get());
                    stmt2.setDouble(3, discord.get(shard.getShardInfo().getShardId()).REST_PING.get());
                    stmt2.setInt(4, discord.get(shard.getShardInfo().getShardId()).GUILD_COUNT.get());
                    stmt2.execute();
                } catch(Exception e) {
                    log.error("An error occurred while running the {} class, message: {}", DatabaseFunctions.class.getSimpleName(), e.getMessage(), e);
                }
            });

            stmt3.setInt(1, audio.PLAYERS_TOTAL.get());
            stmt3.setInt(2, audio.PLAYERS_ACTIVE.get());
            stmt3.setInt(3, audio.QUEUE_SIZE.get());
            stmt3.execute();

            stmt4.setInt(1, cache.TRACK_ID_CACHE_HITS.get());
            stmt4.setInt(2, cache.TRACK_ID_CACHE_SIZE.get());
            stmt4.execute();

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", DatabaseFunctions.class.getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Updates the database with the latest command.
     * @param guildId String
     * @param command String
     * @param executionTime double (milliseconds)
     */
    public static void updateCommandLog(String guildId, String command, double executionTime) {
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO `command_log`(`shardId`, `guildId`, `command`, `executionTime`) VALUES(?, ?, ?, ?)")) {

            stmt.setInt(1, Config.BOT.getJDA().getShardInfo().getShardId());
            stmt.setString(2, guildId);
            stmt.setString(3, command);
            stmt.setDouble(4, executionTime);
            stmt.execute();

        } catch (Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", DatabaseFunctions.class.getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Truncates the metrics database. (This happens when the bot is first loaded.)
     * @param shardId int
     */
    public static void truncateMetrics(int shardId) {
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM `system_metrics` WHERE shardId = ?");
            PreparedStatement stmt2 = conn.prepareStatement("DELETE FROM `discord_metrics` WHERE shardId = ?");
            PreparedStatement stmt3 = conn.prepareStatement("DELETE FROM `audio_metrics`");
            PreparedStatement stmt4 = conn.prepareStatement("DELETE FROM `command_log` WHERE shardId = ?");
            PreparedStatement stmt5 = conn.prepareStatement("DELETE FROM `cache_metrics`")) {

            stmt.setInt(1, shardId);
            stmt.execute();

            stmt2.setInt(1, shardId);
            stmt2.execute();

            stmt3.execute();

            stmt4.setInt(1, shardId);
            stmt4.execute();

            stmt5.execute();

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", DatabaseFunctions.class.getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Truncates anything 6 hours old in the system and discord metrics. Others are kept for total values.
     */
    public static void pruneMetrics() {
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM `system_metrics` WHERE dateInserted < DATE_SUB(NOW(), INTERVAL 6 HOUR);");
            PreparedStatement stmt2 = conn.prepareStatement("DELETE FROM `discord_metrics` WHERE dateInserted < DATE_SUB(NOW(), INTERVAL 6 HOUR);");
            PreparedStatement stmt3 = conn.prepareStatement("DELETE FROM `audio_metrics` WHERE dateInserted < DATE_SUB(NOW(), INTERVAL 6 HOUR);");
            PreparedStatement stmt4 = conn.prepareStatement("DELETE FROM `cache_metrics` WHERE dateInserted < DATE_SUB(NOW(), INTERVAL 6 HOUR);")) {

            stmt.execute();
            stmt2.execute();
            stmt3.execute();
            stmt4.execute();

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", DatabaseFunctions.class.getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Updates settings from channels that are deleted.
     * @param setting the setting to cleanup.
     * @param guildId the guild to cleanup.
     */
    public static void cleanupSettings(String setting, String guildId) {
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("UPDATE `guilds_settings` SET " + setting + " = null WHERE `guildId` = ?")){

            stmt.setString(1, guildId);
            stmt.execute();

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", DatabaseFunctions.class.getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Queries the provisioning database and supplies the next available shard ID.
     * @return the next available shard ID, starting at 0.
     */
    public static int provideShardId() {
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM `shards` ORDER BY `shardId`");
            PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO `shards`(`shardId`) VALUES(?)")){

            // prune expired shards now for extra assurance - in case UpdateShardsTask is between runs.
            pruneExpiredShards();
            ResultSet resultSet = stmt.executeQuery();

            int shard = 0;
            while(resultSet.next()) {
                if(resultSet.getInt("shardId") == shard) {
                    shard++;
                }
            }

            stmt2.setInt(1, shard);
            stmt2.execute();

            return shard;

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", DatabaseFunctions.class.getSimpleName(), ex.getMessage(), ex);
            return -1;
        }
    }

    /**
     * Update shard statistics such as guild count.
     */
    public static void updateShardStatistics(JDA shard) {
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("UPDATE `shards` SET `status` = ?, `guilds` = ?, `gatewayPing` = ?, `restPing` = ?, `shardAssigned` = CURRENT_TIMESTAMP  WHERE `shardId` = ?")){

            stmt.setString(1, shard.getStatus().name());
            stmt.setInt(2, discord.get(shard.getShardInfo().getShardId()).GUILD_COUNT.get());
            stmt.setInt(3, discord.get(shard.getShardInfo().getShardId()).GATEWAY_PING.get());
            stmt.setInt(4, discord.get(shard.getShardInfo().getShardId()).REST_PING.get());
            stmt.setInt(5, shard.getShardInfo().getShardId());
            stmt.execute();

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", DatabaseFunctions.class.getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Update shard status to `RESTARTING` on given shard.
     */
    public static void updateShardRestart(int shard) {
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("UPDATE `shards` SET `status` = ?, `guilds` = ?, `gatewayPing` = ?, `restPing` = ?, `shardAssigned` = CURRENT_TIMESTAMP WHERE `shardId` = ?")){

            stmt.setString(1, "STARTING");
            stmt.setInt(2, 0);
            stmt.setInt(3, 0);
            stmt.setInt(4, 0);
            stmt.setInt(5, shard);
            stmt.execute();

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", DatabaseFunctions.class.getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Update shard status to `SHUTDOWN` on given shard.
     */
    public static void updateShardShutdown(int shard) {
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("UPDATE `shards` SET `status` = ?, `guilds` = ?, `gatewayPing` = ?, `restPing` = ?, `shardAssigned` = '1970-01-01 00:00:01' WHERE `shardId` = ?")){

            stmt.setString(1, "SHUTDOWN");
            stmt.setInt(2, 0);
            stmt.setInt(3, 0);
            stmt.setInt(4, 0);
            stmt.setInt(5, shard);
            stmt.execute();

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", DatabaseFunctions.class.getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Retrieves shard statistics from the database.
     * @return ArrayList<Shard> of shard statistics.
     */
    public static ArrayList<Shard> getShardStatistics() {
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM `shards`")){

            ArrayList<Shard> shards = new ArrayList<>();

            ResultSet resultSet = stmt.executeQuery();
            while(resultSet.next()) {
                shards.add(new Shard(resultSet.getInt("shardId"), resultSet.getString("status"), resultSet.getInt("guilds"), resultSet.getInt("gatewayPing"), resultSet.getInt("restPing")));
            }

            return shards;

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", DatabaseFunctions.class.getSimpleName(), ex.getMessage(), ex);
            return new ArrayList<>();
        }
    }

    /**
     * Clears the previsioning database of expired IDs (Older than 35 seconds).
     */
    public static void pruneExpiredShards() {
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM `shards` WHERE `shardAssigned` < DATE_SUB(NOW(), INTERVAL 35 SECOND) OR (`status` = 'SHUTTING_DOWN') OR (`status` = 'SHUTDOWN')")) {

            stmt.execute();

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", DatabaseFunctions.class.getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Set shard signal value to 1 (shutdown)
     * @param shardId int
     */
    public static void triggerShutdownSignal(int shardId) {
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("UPDATE `shards` SET `shutdownSignal` = 1 WHERE `shardId` = ?")) {

            stmt.setInt(1, shardId);
            stmt.execute();

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", DatabaseFunctions.class.getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Set shard signal value to 0
     * @param shardId int
     */
    public static void cancelShutdownSignal(int shardId) {
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("UPDATE `shards` SET `shutdownSignal` = 0 WHERE `shardId` = ?")) {

            stmt.setInt(1, shardId);
            stmt.execute();

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", DatabaseFunctions.class.getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Returns state of shutdown signal for given shard.
     * @param shardId int
     * @return boolean
     */
    public static boolean hasShutdownSignal(int shardId) {
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT `shutdownSignal` FROM `shards` WHERE `shardId` = ?")) {

            stmt.setInt(1, shardId);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()) {
                return rs.getBoolean("shutdownSignal");
            }

            return false;

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", DatabaseFunctions.class.getSimpleName(), ex.getMessage(), ex);
            return false;
        }
    }

    /**
     * Set shard signal value to 1 (restart)
     * @param shardId int
     */
    public static void triggerRestartSignal(int shardId) {
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("UPDATE `shards` SET `restartSignal` = 1 WHERE `shardId` = ?")) {

            stmt.setInt(1, shardId);
            stmt.execute();

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", DatabaseFunctions.class.getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Set shard signal value to 0
     * @param shardId int
     */
    public static void cancelRestartSignal(int shardId) {
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("UPDATE `shards` SET `restartSignal` = 0 WHERE `shardId` = ?")) {

            stmt.setInt(1, shardId);
            stmt.execute();

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", DatabaseFunctions.class.getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Returns state of restart signal for given shard.
     * @param shardId int
     * @return boolean
     */
    public static boolean hasRestartSignal(int shardId) {
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT `restartSignal` FROM `shards` WHERE `shardId` = ?")) {

            stmt.setInt(1, shardId);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()) {
                return rs.getBoolean("restartSignal");
            }

            return false;

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", DatabaseFunctions.class.getSimpleName(), ex.getMessage(), ex);
            return false;
        }
    }
}
