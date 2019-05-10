package com.yuuko.core.database;

import com.yuuko.core.database.connections.SettingsDatabaseConnection;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class ReactionRoleFunctions {

    private static final Logger log = LoggerFactory.getLogger(ReactionRoleFunctions.class);

    /**
     * Checks if a reaction role exists.
     *
     * @param message message the reaction role is attached to.
     * @param emote the emote the reaction role is invoked by.
     * @return boolean if the reaction role exists.
     */
    public static boolean hasReactionRole(Message message, Emote emote) {
        try(Connection conn = SettingsDatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM `ReactionRoles` WHERE `messageId` = ? AND `emoteId` = ?")) {

            stmt.setString(1, message.getId());
            stmt.setString(2, emote.getId());

            return stmt.executeQuery().next();

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", DatabaseFunctions.class.getSimpleName(), ex.getMessage(), ex);
            return false;
        }
    }

    /**
     * Selects a reaction role to the respective database table and returns if the operation was successful.
     *
     * @param message message the reaction role is attached to.
     * @param emote the emote the reaction role is invoked by.
     * @return boolean if the operation was successful.
     */
    public static String selectReactionRole(Message message, Emote emote) {
        try(Connection conn = SettingsDatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT `roleId` FROM `ReactionRoles` WHERE `messageId` = ? AND `emoteId` = ?")) {

            stmt.setString(1, message.getId());
            stmt.setString(2, emote.getId());

            return stmt.executeQuery().getString(3);

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", DatabaseFunctions.class.getSimpleName(), ex.getMessage(), ex);
            return null;
        }
    }

    /**
     * Adds a reaction role to the database and returns if the operation was successful.
     *
     * @param guild guild the reaction role is attached to.
     * @param message message the reaction role is attached to.
     * @param emote the emote the reaction role is invoked by.
     * @param role the role that the reaction role will give to the user.
     * @return boolean if the operation was successful.
     */
    public static boolean addReactionRole(Guild guild, Message message, Emote emote, Role role) {
        try(Connection conn = SettingsDatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO `ReactionRoles` (`guildId`, `messageId`, `emoteId`, `roleId`) VALUES (?, ?, ?, ?)")) {

            stmt.setString(1, guild.getId());
            stmt.setString(2, message.getId());
            stmt.setString(3, emote.getId());
            stmt.setString(4, role.getId());

            return stmt.execute();

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", DatabaseFunctions.class.getSimpleName(), ex.getMessage(), ex);
            return false;
        }
    }

    /**
     * Removes a reaction role from the database and returns if the operation was successful.
     *
     * @param message message the reaction role is attached to.
     * @param emote the emote the reaction role is invoked by.
     * @return boolean if the operation was successful.
     */
    public static boolean removeReactionRole(Message message, Emote emote) {
        try(Connection conn = SettingsDatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM `ReactionRoles` WHERE `messageId` = ? AND `emoteId` = ?")) {

            stmt.setString(1, message.getId());
            stmt.setString(2, emote.getId());

            return stmt.execute();

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", DatabaseFunctions.class.getSimpleName(), ex.getMessage(), ex);
            return false;
        }
    }
}
