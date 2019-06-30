package com.yuuko.core.database.function;

import com.yuuko.core.Configuration;
import com.yuuko.core.database.connection.MetricsDatabaseConnection;
import com.yuuko.core.database.connection.ProvisioningDatabaseConnection;
import com.yuuko.core.database.connection.YuukoDatabaseConnection;
import com.yuuko.core.entity.Shard;
import com.yuuko.core.metrics.MetricsManager;
import com.yuuko.core.metrics.pathway.DiscordMetrics;
import com.yuuko.core.metrics.pathway.EventMetrics;
import com.yuuko.core.metrics.pathway.SystemMetrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DatabaseFunctions {
    private static final Logger log = LoggerFactory.getLogger(DatabaseFunctions.class);
    private static final EventMetrics event = MetricsManager.getEventMetrics();
    private static final SystemMetrics system = MetricsManager.getSystemMetrics();
    private static final DiscordMetrics discord = MetricsManager.getDiscordMetrics();

    /**
     * Updates the database with the latest metrics.
     */
    public static void updateMetricsDatabase() {
        try(Connection conn = MetricsDatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO `SystemMetrics`(`shardId`, `uptime`, `memoryTotal`, `memoryUsed`) VALUES(?, ?, ?, ?)");
            PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO `EventMetrics`(`shardId`, `guildJoinEvent`, `guildLeaveEvent`, `guildMemberJoinEvent`, `guildMemberLeaveEvent`, `guildUpdateNameEvent`, `guildUpdateRegionEvent`, `guildUpdateIconEvent`, `guildUpdateSplashEvent`, `guildMessageReceivedEvent`, `guildMessageDeleteEvent`, `guildMessageReactionAddEvent`, `guildMessageReactionRemoveEvent`) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            PreparedStatement stmt3 = conn.prepareStatement("INSERT INTO `DiscordMetrics`(`shardId`, `ping`, `guildCount`, `userCount`) VALUES(?, ?, ?, ?)")) {

            int shardId = Configuration.BOT.getJDA().getShardInfo().getShardId();

            stmt.setInt(1, shardId);
            stmt.setLong(2, system.UPTIME);
            stmt.setLong(3, system.MEMORY_TOTAL);
            stmt.setLong(4, system.MEMORY_USED);
            stmt.execute();

            stmt2.setInt(1, shardId);
            stmt2.setInt(2, event.GUILD_JOIN_EVENT.get());
            stmt2.setInt(3, event.GUILD_LEAVE_EVENT.get());
            stmt2.setInt(4, event.GUILD_MEMBER_JOIN_EVENT.get());
            stmt2.setInt(5, event.GUILD_MEMBER_LEAVE_EVENT.get());
            stmt2.setInt(6, event.GUILD_UPDATE_NAME_EVENT.get());
            stmt2.setInt(7, event.GUILD_UPDATE_REGION_EVENT.get());
            stmt2.setInt(8, event.GUILD_UPDATE_ICON_EVENT.get());
            stmt2.setInt(9, event.GUILD_UPDATE_SPLASH_EVENT.get());
            stmt2.setInt(10, event.GUILD_MESSAGE_RECEIVED_EVENT.get());
            stmt2.setInt(11, event.GUILD_MESSAGE_DELETE_EVENT.get());
            stmt2.setInt(12, event.GUILD_MESSAGE_REACTION_ADD_EVENT.get());
            stmt2.setInt(13, event.GUILD_MESSAGE_REACTION_REMOVE_EVENT.get());
            stmt2.execute();

            stmt3.setInt(1, shardId);
            stmt3.setDouble(2, discord.PING.get());
            stmt3.setInt(3, discord.GUILD_COUNT);
            stmt3.setInt(4, discord.USER_COUNT);
            stmt3.execute();


        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", DatabaseFunctions.class.getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Updates the database with the latest command.
     *
     * @param guildId String
     * @param command String
     * @param executionTime double (milliseconds)
     */
    public static void updateCommandsLog(String guildId, String command, double executionTime) {
        try(Connection conn = MetricsDatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO `CommandsLog`(`shardId`, `guildId`, `command`, `executionTime`) VALUES(?, ?, ?, ?)")) {

            stmt.setInt(1, Configuration.BOT.getJDA().getShardInfo().getShardId());
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
     *
     * @param shard int
     */
    public static void truncateMetrics(int shard) {
        try(Connection conn = MetricsDatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM `SystemMetrics` WHERE shardId = ?");
            PreparedStatement stmt2 = conn.prepareStatement("DELETE FROM `EventMetrics` WHERE shardId = ?");
            PreparedStatement stmt3 = conn.prepareStatement("DELETE FROM `DiscordMetrics` WHERE shardId = ?");
            PreparedStatement stmt4 = conn.prepareStatement("DELETE FROM `CommandsLog` WHERE shardId = ?")) {

            stmt.setInt(1, shard);
            stmt.execute();

            stmt2.setInt(1, shard);
            stmt2.execute();

            stmt3.setInt(1, shard);
            stmt3.execute();

            stmt4.setInt(1, shard);
            stmt4.execute();

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", DatabaseFunctions.class.getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Truncates anything 6 hours old in the system and discord metrics. Others are kept for total values.
     */
    public static void pruneMetrics() {
        try(Connection conn = MetricsDatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM `SystemMetrics` WHERE dateInserted < DATE_SUB(NOW(), INTERVAL 6 HOUR);");
            PreparedStatement stmt2 = conn.prepareStatement("DELETE FROM `DiscordMetrics` WHERE dateInserted < DATE_SUB(NOW(), INTERVAL 6 HOUR);")) {

            stmt.execute();
            stmt2.execute();

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", DatabaseFunctions.class.getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Updates settings from channels that are deleted.
     *
     * @param setting the setting to cleanup.
     * @param guildId the guild to cleanup.
     */
    public static void cleanupSettings(String setting, String guildId) {
        try(Connection conn = YuukoDatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("UPDATE `GuildSettings` SET " + setting + " = null WHERE `guildId` = ?")){

            stmt.setString(1, guildId);
            stmt.execute();

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", DatabaseFunctions.class.getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Queries the provisioning database and supplies the next available shard ID.
     *
     * @return the next available shard ID, starting at 0.
     */
    public static int provideShardId() {
        try(Connection conn = ProvisioningDatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM `Shards` ORDER BY `shardId`");
            PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO `Shards`(`shardId`) VALUES(?)")){

            ResultSet resultSet = stmt.executeQuery();

            int shard = 0;
            while(resultSet.next()) {
                if(resultSet.getInt(1) == shard) {
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
     * Retrieves the total shard count from the database.
     *
     * @return the total shard count expected from the database.
     */
    public static int getShardCount() {
        try(Connection conn = ProvisioningDatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM `ShardConfiguration`")){

            ResultSet resultSet = stmt.executeQuery();
            if(resultSet.next()) {
                return resultSet.getInt("ShardCount");
            } else {
                return -1;
            }

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", DatabaseFunctions.class.getSimpleName(), ex.getMessage(), ex);
            return -1;
        }
    }

    /**
     * Update shard statistics such as guild count and user count.
     */
    public static void updateShardStatistics(int shard, String status, int guilds, int users, int ping) {
        try(Connection conn = ProvisioningDatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("UPDATE `Shards` SET `status` = ?, `guilds` = ?, `users` = ?, `ping` = ?, `shardAssigned` = CURRENT_TIMESTAMP  WHERE `shardId` = ?")){

            stmt.setString(1, status);
            stmt.setInt(2, guilds);
            stmt.setInt(3, users);
            stmt.setInt(4, ping);
            stmt.setInt(5, shard);
            stmt.execute();

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", DatabaseFunctions.class.getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Retrieves shard statistics from the database.
     *
     * @return ArrayList<Shard> of shard statistics.
     */
    public static ArrayList<Shard> getShardStatistics() {
        try(Connection conn = ProvisioningDatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM `Shards`")){

            ArrayList<Shard> shards = new ArrayList<>();

            ResultSet resultSet = stmt.executeQuery();
            while(resultSet.next()) {
                shards.add(new Shard(resultSet.getInt(1), resultSet.getString(2), resultSet.getInt(3), resultSet.getInt(4), resultSet.getInt(5)));
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
        try(Connection conn = ProvisioningDatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM `Shards` WHERE `shardAssigned` < DATE_SUB(NOW(), INTERVAL 31 SECOND)")) {

            stmt.execute();

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", DatabaseFunctions.class.getSimpleName(), ex.getMessage(), ex);
        }
    }
}
