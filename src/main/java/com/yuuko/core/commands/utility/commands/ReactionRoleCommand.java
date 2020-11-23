package com.yuuko.core.commands.utility.commands;

import com.yuuko.core.Config;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.database.connection.DatabaseConnection;
import com.yuuko.core.events.entity.MessageEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.react.GenericGuildMessageReactionEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;

public class ReactionRoleCommand extends Command {

    public ReactionRoleCommand() {
        super("reactrole", Config.MODULES.get("utility"), 2, -1L, Arrays.asList("-reactrole <message_id> clear", "-reactrole <message_id> <:emote:>", "-reactrole <message_id> <:emote:> <@role>"), false, Arrays.asList(Permission.MANAGE_ROLES, Permission.MESSAGE_HISTORY));
    }

    @Override
    public void onCommand(MessageEvent e) {
        final String[] params = e.getParameters().split("\\s+");
        final Role highestSelfRole = e.getGuild().getSelfMember().getRoles().get(0); // Role list is ordered from highest to lowest
        final Role role = (e.getMessage().getMentionedRoles().size() > 0) ? e.getMessage().getMentionedRoles().get(0) : null;
        final String emote = (e.getMessage().getEmotes().size() > 0) ? e.getMessage().getEmotes().get(0).getName() + ":" + e.getMessage().getEmotes().get(0).getId() : params[1];

        if(params[0].length() < 18) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Missing Message").setDescription("Input did not match any known message id.");
            MessageHandler.reply(e, embed.build());
            return;
        }

        // removes all react roles from message
        if(params[1].equalsIgnoreCase("clear")) {
            DatabaseInterface.removeReactionRole(e.getMessage().getId());
            return;
        }

        // uses consumer .queue() instead of .complete() since .complete() will throw an exception rather than return null :)))
        e.getChannel().retrieveMessageById(params[0]).queue(message -> {
            if(role == null) {
                message.getReactions().stream().filter(m -> m.getReactionEmote().getAsReactionCode().equals(emote)).forEach(reaction -> {
                    reaction.removeReaction().queue(
                            s -> {
                                DatabaseInterface.removeReactionRole(message, emote);
                                EmbedBuilder embed = new EmbedBuilder().setTitle("Success").setDescription("Successfully removed reaction role " + emote + " from message " + message + ".");
                                MessageHandler.reply(e, embed.build());
                            },
                            f -> {
                                EmbedBuilder embed = new EmbedBuilder().setTitle("Failure").setDescription("Unable to remove the reaction from the selected message.");
                                MessageHandler.reply(e, embed.build());
                            });
                });
                return;
            }

            // check if the role exists, is available for use.
            if(!e.getGuild().getRoleCache().asList().contains(role)) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Role").setDescription("This role is unavailable for use in a reaction role. Make sure that you are using roles from this server.");
                MessageHandler.reply(e, embed.build());
                return;
            }

            // checks if role is lower in the hierarchy than bots highest role.
            if(role.getPositionRaw() >= highestSelfRole.getPositionRaw()) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Role").setDescription("I cannot assign roles that are higher than or equal to my highest role in the hierarchy.");
                MessageHandler.reply(e, embed.build());
                return;
            }

            message.addReaction(emote).queue(
                    s -> {
                        if(DatabaseInterface.addReactionRole(e.getGuild(), message.getId(), emote, role)) {
                            EmbedBuilder embed = new EmbedBuilder().setTitle("Success").setDescription("Successfully paired emote " + emote + " to role " + role.getAsMention() + " for message " + message + ".");
                            MessageHandler.reply(e, embed.build());
                        } else {
                            EmbedBuilder embed = new EmbedBuilder().setTitle("Already Exists").setDescription("A reaction role using this message and emote combination already exists.");
                            MessageHandler.reply(e, embed.build());
                        }},
                    f -> {
                        EmbedBuilder embed = new EmbedBuilder().setTitle("Failure").setDescription("Unable to add a reaction to the selected message.");
                        MessageHandler.reply(e, embed.build());
                    });

        }, failure -> {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Failure").setDescription("Unable to find message with given id, either deleted or isn't in this channel.");
            MessageHandler.reply(e, embed.build());
        });
    }

    /**
     * Process GenericGuildMessageReactionEvent events to apply or remove roles from users.
     *
     * @param e GenericGuildMessageReactionEvent
     */
    public static void processReaction(GenericGuildMessageReactionEvent e) {
        final String message = e.getMessageId();
        final String emote = e.getReactionEmote().getAsReactionCode();
        final String roleId = DatabaseInterface.selectReactionRole(message, emote);

        if(roleId == null) {
            return;
        }

        final Role role = e.getGuild().getRoleById(roleId);

        if(role == null) {
            return;
        }

        if(e instanceof GuildMessageReactionAddEvent) {
            e.getGuild().addRoleToMember(e.getMember(), role).queue();
        } else {
            e.getGuild().removeRoleFromMember(e.getMember(), role).queue();
        }
    }

    public static class DatabaseInterface {
        /**
         * Selects a reaction role to the respective database table and returns if the operation was successful. (String)
         *
         * @param message message the reaction role is attached to.
         * @param emote   the emote the reaction role is invoked by.
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
                log.error("An error occurred while running the {} class, message: {}", ReactionRoleCommand.DatabaseInterface.class.getSimpleName(), ex.getMessage(), ex);
                return null;
            }
        }

        /**
         * Adds a reaction role to the database and returns if the operation was successful.
         *
         * @param guild   guild the reaction role is attached to.
         * @param message message the reaction role is attached to.
         * @param emote   the emote the reaction role is invoked by.
         * @param role    the role that the reaction role will give to the user.
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
                log.error("An error occurred while running the {} class, message: {}", ReactionRoleCommand.DatabaseInterface.class.getSimpleName(), ex.getMessage(), ex);
                return false;
            }
        }

        /**
         * Removes a reaction role from the database and returns if the operation was successful.
         *
         * @param message message the reaction role is attached to.
         * @param emote   the emote the reaction role is invoked by.
         * @return boolean if the operation was successful.
         */
        public static void removeReactionRole(Message message, String emote) {
            try(Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement("DELETE FROM `reaction_roles` WHERE `messageId` = ? AND `emoteId` = ?")) {

                stmt.setString(1, message.getId());
                stmt.setString(2, emote);
                stmt.execute();

            } catch(Exception ex) {
                log.error("An error occurred while running the {} class, message: {}", ReactionRoleCommand.DatabaseInterface.class.getSimpleName(), ex.getMessage(), ex);
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
                log.error("An error occurred while running the {} class, message: {}", ReactionRoleCommand.DatabaseInterface.class.getSimpleName(), ex.getMessage(), ex);
            }
        }
    }
}
