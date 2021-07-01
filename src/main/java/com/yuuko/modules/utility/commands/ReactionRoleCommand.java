package com.yuuko.modules.utility.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.database.connection.DatabaseConnection;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.modules.Command;
import com.yuuko.utilities.Sanitiser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Emote;
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
        super("reactrole", Arrays.asList("-reactrole <message_id> clear", "-reactrole <message_id> <:emote:>", "-reactrole <message_id> <:emote:> <@role>"), Arrays.asList(Permission.MANAGE_ROLES, Permission.MESSAGE_HISTORY), 2);
    }

    @Override
    public void onCommand(MessageEvent context) {
        final String[] params = context.getParameters().split("\\s+");
        final Role highestSelfRole = context.getGuild().getSelfMember().getRoles().get(0); // Role list is ordered from highest to lowest
        final Role role = (context.getMessage().getMentionedRoles().size() > 0) ? context.getMessage().getMentionedRoles().get(0) : null;
        final String emote = (context.getMessage().getEmotes().size() > 0) ? context.getMessage().getEmotes().get(0).getName() + ":" + context.getMessage().getEmotes().get(0).getId() : params[1];

        if(params[0].length() < 18 || params[0].length() > 20 || !Sanitiser.isNumeric(params[0])) {
            EmbedBuilder embed = new EmbedBuilder().setTitle(context.i18n("invalid_input")).setDescription(context.i18n("invalid_input_desc"));
            MessageDispatcher.reply(context, embed.build());
            return;
        }

        // removes all react roles from message
        if(params[1].equalsIgnoreCase("clear")) {
            DatabaseInterface.removeReactionRole(context.getMessage().getId());
            return;
        }

        // uses consumer .queue() instead of .complete() since .complete() will throw an exception rather than return null :)))
        context.getChannel().retrieveMessageById(params[0]).queue(message -> {
            if(role == null) {
                message.getReactions().stream()
                        .filter(m -> m.getReactionEmote().getAsReactionCode().equals(emote))
                        .forEach(reaction -> reaction.removeReaction().queue(
                        s -> {
                            DatabaseInterface.removeReactionRole(message, emote);
                            EmbedBuilder embed = new EmbedBuilder()
                                    .setTitle(context.i18n("success"))
                                    .setDescription(context.i18n("removed").formatted(emote, message));
                            MessageDispatcher.reply(context, embed.build());
                        },
                        f -> {
                            EmbedBuilder embed = new EmbedBuilder()
                                    .setTitle(context.i18n("failure"))
                                    .setDescription(context.i18n("removed_fail"));
                            MessageDispatcher.reply(context, embed.build());
                        }));
                return;
            }

            // check if the role exists, is available for use.
            if(!context.getGuild().getRoleCache().asList().contains(role)) {
                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle(context.i18n("invalid_input"))
                        .setDescription(context.i18n("role_unavailable"));
                MessageDispatcher.reply(context, embed.build());
                return;
            }

            // checks if role is lower in the hierarchy than bots highest role.
            if(role.getPositionRaw() >= highestSelfRole.getPositionRaw()) {
                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle(context.i18n("invalid_input"))
                        .setDescription(context.i18n("role_higher"));
                MessageDispatcher.reply(context, embed.build());
                return;
            }

            message.addReaction(emote).queue(
                    s -> {
                        if(DatabaseInterface.addReactionRole(context.getGuild(), message.getId(), emote, role)) {
                            EmbedBuilder embed = new EmbedBuilder()
                                    .setTitle(context.i18n("success"))
                                    .setDescription(context.i18n("success_desc").formatted(emote, role.getAsMention(), message));
                            MessageDispatcher.reply(context, embed.build());
                        } else {
                            EmbedBuilder embed = new EmbedBuilder()
                                    .setTitle(context.i18n("exists"))
                                    .setDescription(context.i18n("exists_desc"));
                            MessageDispatcher.reply(context, embed.build());
                        }},
                    f -> {
                        EmbedBuilder embed = new EmbedBuilder()
                                .setTitle(context.i18n("failure"))
                                .setDescription(context.i18n("failure_desc"));
                        MessageDispatcher.reply(context, embed.build());
                    });

        }, failure -> {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(context.i18n("failure"))
                    .setDescription(context.i18n("failure_missing"));
            MessageDispatcher.reply(context, embed.build());
        });
    }

    /**
     * Process GenericGuildMessageReactionEvent events to apply or remove roles from users.
     * @param e GenericGuildMessageReactionEvent
     */
    public static void processReaction(GenericGuildMessageReactionEvent e) {
        final String message = e.getMessageId();
        final String emote = e.getReactionEmote().getAsReactionCode();
        final String roleId = DatabaseInterface.selectReactionRole(message, emote);
        final Role role;

        if(roleId == null || (role = e.getGuild().getRoleById(roleId)) == null) {
            return;
        }

        if(e instanceof GuildMessageReactionAddEvent) {
            e.getGuild().addRoleToMember(e.getUserId(), role).queue();
        } else {
            e.getGuild().removeRoleFromMember(e.getUserId(), role).queue();
        }
    }

    /**
     * Inner-class container for all database-related functions.
     */
    public static class DatabaseInterface {
        /**
         * Selects a reaction role to the respective database table and returns if the operation was successful. (String)
         * @param message message the reaction role is attached to.
         * @param emote emote the reaction role is invoked by.
         * @return boolean if the operation was successful.
         */
        public static String selectReactionRole(String message, String emote) {
            try(Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement("SELECT `roleId` FROM `guilds_reaction_roles` WHERE `messageId` = ? AND `emoteId` = ?")) {

                stmt.setString(1, message);
                stmt.setString(2, emote);

                ResultSet rs = stmt.executeQuery();
                if(rs.next()) {
                    return rs.getString(1);
                }

                return null;

            } catch(Exception e) {
                log.error("An error occurred while running the {} class, message: {}", ReactionRoleCommand.DatabaseInterface.class.getSimpleName(), e.getMessage(), e);
                return null;
            }
        }

        /**
         * Adds a reaction role to the database and returns if the operation was successful.
         * @param guild {@link Guild} the reaction role is attached to.
         * @param message message the reaction role is attached to.
         * @param emote the emote the reaction role is invoked by.
         * @param role {@link Role} that the reaction role will give to the user.
         * @return boolean if the operation was successful.
         */
        public static boolean addReactionRole(Guild guild, String message, String emote, Role role) {
            try(Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO `guilds_reaction_roles` (`guildId`, `messageId`, `emoteId`, `roleId`) VALUES (?, ?, ?, ?)")) {

                stmt.setString(1, guild.getId());
                stmt.setString(2, message);
                stmt.setString(3, emote);
                stmt.setString(4, role.getId());

                return !stmt.execute();

            } catch(Exception e) {
                log.error("An error occurred while running the {} class, message: {}", ReactionRoleCommand.DatabaseInterface.class.getSimpleName(), e.getMessage(), e);
                return false;
            }
        }

        /**
         * Updates a reaction role emote name.
         * @param guild {@link Guild}
         * @param oldEmote string of old emote name.
         * @param newEmote string of new emote name.
         */
        public static void updateReactionRole(Guild guild, String oldEmote, String newEmote) {
            try(Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement("UPDATE `guilds_reaction_roles` SET `emoteId` = ? WHERE `guildId` = ? AND `emoteId` = ?")) {

                stmt.setString(1, newEmote);
                stmt.setString(2, guild.getId());
                stmt.setString(3, oldEmote);

                stmt.execute();

            } catch(Exception e) {
                log.error("An error occurred while running the {} class, message: {}", ReactionRoleCommand.DatabaseInterface.class.getSimpleName(), e.getMessage(), e);
            }
        }

        /**
         * Removes a reaction role from the database and returns if the operation was successful.
         * @param message {@link Message} the reaction role is attached to.
         * @param emote the emote the reaction role is invoked by.
         * @return boolean if the operation was successful.
         */
        public static void removeReactionRole(Message message, String emote) {
            try(Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement("DELETE FROM `guilds_reaction_roles` WHERE `messageId` = ? AND `emoteId` = ?")) {

                stmt.setString(1, message.getId());
                stmt.setString(2, emote);
                stmt.execute();

            } catch(Exception e) {
                log.error("An error occurred while running the {} class, message: {}", ReactionRoleCommand.DatabaseInterface.class.getSimpleName(), e.getMessage(), e);
            }
        }

        /**
         * Removes all reaction roles from the given messageId.
         * @param messageId messageId.
         */
        public static void removeReactionRole(String messageId) {
            try(Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement("DELETE FROM `guilds_reaction_roles` WHERE `messageId` = ?")) {

                stmt.setString(1, messageId);
                stmt.execute();

            } catch(Exception e) {
                log.error("An error occurred while running the {} class, message: {}", ReactionRoleCommand.DatabaseInterface.class.getSimpleName(), e.getMessage(), e);
            }
        }

        /**
         * Removes all reaction roles from the given emote.
         * @param emote {@link Emote}.
         */
        public static void removeReactionRole(Emote emote) {
            try(Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement("DELETE FROM `guilds_reaction_roles` WHERE `emoteId` = ?")) {

                stmt.setString(1, emote.getName() + ":" + emote.getId());
                stmt.execute();

            } catch(Exception e) {
                log.error("An error occurred while running the {} class, message: {}", ReactionRoleCommand.DatabaseInterface.class.getSimpleName(), e.getMessage(), e);
            }
        }

        /**
         * Removes all reaction roles from the given role.
         * @param role {@link Role}.
         */
        public static void removeReactionRole(Role role) {
            try(Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement("DELETE FROM `guilds_reaction_roles` WHERE `roleId` = ?")) {

                stmt.setString(1, role.getId());
                stmt.execute();

            } catch(Exception e) {
                log.error("An error occurred while running the {} class, message: {}", ReactionRoleCommand.DatabaseInterface.class.getSimpleName(), e.getMessage(), e);
            }
        }
    }
}
