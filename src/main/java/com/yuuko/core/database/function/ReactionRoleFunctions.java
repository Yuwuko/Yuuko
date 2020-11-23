package com.yuuko.core.database.function;

import com.yuuko.core.database.connection.DatabaseConnection;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ReactionRoleFunctions {
    private static final Logger log = LoggerFactory.getLogger(ReactionRoleFunctions.class);

    /**
     * Selects a reaction role to the respective database table and returns if the operation was successful. (String)
     * @param message message the reaction role is attached to.
     * @param emote the emote the reaction role is invoked by.
     * @return boolean if the operation was successful.
     */
    public static String selectReactionRole(String message, String emote) {
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT `roleId` FROM `reaction_roles` WHERE `messageId` = ? AND `emoteId` = ?")) {

            stmt.setString(1, message);
            stmt.setString(2, emote);

            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                return rs.getString(1);
            }

            return null;

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", DatabaseFunctions.class.getSimpleName(), ex.getMessage(), ex);
            return null;
        }
    }

    /**
     * Adds a reaction role to the database and returns if the operation was successful.
     * @param guild guild the reaction role is attached to.
     * @param message message the reaction role is attached to.
     * @param emote the emote the reaction role is invoked by.
     * @param role the role that the reaction role will give to the user.
     * @return boolean if the operation was successful.
     */
    public static boolean addReactionRole(Guild guild, String message, String emote, Role role) {
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO `reaction_roles` (`guildId`, `messageId`, `emoteId`, `roleId`) VALUES (?, ?, ?, ?)")) {

            stmt.setString(1, guild.getId());
            stmt.setString(2, message);
            stmt.setString(3, emote);
            stmt.setString(4, role.getId());

            return !stmt.execute();

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", DatabaseFunctions.class.getSimpleName(), ex.getMessage(), ex);
            return false;
        }
    }

    /**
     * Removes a reaction role from the database and returns if the operation was successful.
     * @param message message the reaction role is attached to.
     * @param emote the emote the reaction role is invoked by.
     * @return boolean if the operation was successful.
     */
    public static void removeReactionRole(Message message, String emote) {
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM `reaction_roles` WHERE `messageId` = ? AND `emoteId` = ?")) {

            stmt.setString(1, message.getId());
            stmt.setString(2, emote);
            stmt.execute();

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", DatabaseFunctions.class.getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Removes all reaction roles from the given message.
     * @param message message id.
     */
    public static void removeReactionRole(String message) {
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM `reaction_roles` WHERE `messageId` = ?")) {

            stmt.setString(1, message);
            stmt.execute();

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", DatabaseFunctions.class.getSimpleName(), ex.getMessage(), ex);
        }
    }
}
