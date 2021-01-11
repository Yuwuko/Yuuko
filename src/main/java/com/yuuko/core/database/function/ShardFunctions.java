package com.yuuko.core.database.function;

import com.yuuko.core.database.connection.DatabaseConnection;
import com.yuuko.core.entity.Shard;
import com.yuuko.core.metrics.MetricsManager;
import com.yuuko.core.metrics.pathway.DiscordMetrics;
import net.dv8tion.jda.api.JDA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ShardFunctions {
    private static final Logger log = LoggerFactory.getLogger(ShardFunctions.class);

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
            log.error("An error occurred while running the {} class, message: {}", ShardFunctions.class.getSimpleName(), ex.getMessage(), ex);
            return -1;
        }
    }

    /**
     * Update shard statistics such as guild count.
     */
    public static void updateShardStatistics(JDA shard) {
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("UPDATE `shards` SET `status` = ?, `guilds` = ?, `gatewayPing` = ?, `restPing` = ?, `shardAssigned` = CURRENT_TIMESTAMP  WHERE `shardId` = ?")){

            DiscordMetrics metrics = MetricsManager.getDiscordMetrics(shard.getShardInfo().getShardId());
            stmt.setString(1, shard.getStatus().name());
            stmt.setInt(2, metrics.GUILD_COUNT.get());
            stmt.setInt(3, metrics.GATEWAY_PING.get());
            stmt.setInt(4, metrics.REST_PING.get());
            stmt.setInt(5, shard.getShardInfo().getShardId());
            stmt.execute();

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", ShardFunctions.class.getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Update shard status to `RESTARTING` on given shard.
     */
    public static void updateShardRestart(int shard) {
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("UPDATE `shards` SET `status` = 'RESTARTING', `guilds` = 0, `gatewayPing` = 0, `restPing` = 0, `shardAssigned` = CURRENT_TIMESTAMP WHERE `shardId` = ?")){

            stmt.setInt(1, shard);
            stmt.execute();

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", ShardFunctions.class.getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Update shard status to `SHUTDOWN` on given shard.
     */
    public static void updateShardShutdown(int shard) {
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("UPDATE `shards` SET `status` = 'SHUTDOWN', `guilds` = 0, `gatewayPing` = 0, `restPing` = 0, `shardAssigned` = '1970-01-01 00:00:01' WHERE `shardId` = ?")){

            stmt.setInt(1, shard);
            stmt.execute();

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", ShardFunctions.class.getSimpleName(), ex.getMessage(), ex);
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
            log.error("An error occurred while running the {} class, message: {}", ShardFunctions.class.getSimpleName(), ex.getMessage(), ex);
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
            log.error("An error occurred while running the {} class, message: {}", ShardFunctions.class.getSimpleName(), ex.getMessage(), ex);
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
            log.error("An error occurred while running the {} class, message: {}", ShardFunctions.class.getSimpleName(), ex.getMessage(), ex);
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
            log.error("An error occurred while running the {} class, message: {}", ShardFunctions.class.getSimpleName(), ex.getMessage(), ex);
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
            log.error("An error occurred while running the {} class, message: {}", ShardFunctions.class.getSimpleName(), ex.getMessage(), ex);
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
            log.error("An error occurred while running the {} class, message: {}", ShardFunctions.class.getSimpleName(), ex.getMessage(), ex);
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
            log.error("An error occurred while running the {} class, message: {}", ShardFunctions.class.getSimpleName(), ex.getMessage(), ex);
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
            log.error("An error occurred while running the {} class, message: {}", ShardFunctions.class.getSimpleName(), ex.getMessage(), ex);
            return false;
        }
    }
}
