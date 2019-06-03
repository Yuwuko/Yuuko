package com.yuuko.core.commands.utility.commands;

import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.utility.UtilityModule;
import com.yuuko.core.database.function.ReactionRoleFunctions;
import com.yuuko.core.events.extensions.MessageEvent;
import com.yuuko.core.utilities.Sanitiser;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageReaction;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.guild.react.GenericGuildMessageReactionEvent;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionAddEvent;

import java.util.Arrays;
import java.util.HashMap;

public class ReactionRoleCommand extends Command {

    private static final HashMap<String, String> selectedMessages = new HashMap<>();

    public ReactionRoleCommand() {
        super("reactrole", UtilityModule.class, 1, Arrays.asList("-reactrole select", "-reactrole select <Message ID>", "-reactrole add <:emote:> <@role>", "-reactrole rem <:emote:>"), false, Arrays.asList(Permission.MANAGE_ROLES, Permission.MESSAGE_HISTORY));
    }

    @Override
    public void onCommand(MessageEvent e) {
        final String[] parameters = e.getCommand().get(1).split("\\s+");
        final Role highestSelfRole = e.getGuild().getSelfMember().getRoles().get(0);
        final String action = parameters[0].toLowerCase();
        String selectedMessageId = selectedMessages.get(e.getAuthor().getId());

        // Params length less than 2 are considered `select` for latest message.
        if(parameters.length < 2 && action.equals("select")) {
            selectedMessageId = e.getChannel().getHistoryBefore(e.getChannel().getLatestMessageId(), 1).complete().getRetrievedHistory().get(0).getId();
            selectedMessages.remove(e.getAuthor().getId());
            selectedMessages.put(e.getAuthor().getId(), selectedMessageId);

            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("Reaction Role")
                    .setDescription(e.getChannel().getMessageById(selectedMessageId).complete().toString() + " has been selected.")
                    .addField("Options", e.getPrefix() + "reactrole add <:emote:> <@role>\n" + e.getPrefix() + "reactrole rem <:emote:> <@role>", true);
            MessageHandler.sendMessage(e, embed.build());
            return;
        }

        // Params length less than 3 (but greater than 1) are considered `select` but with given message id.
        if(parameters.length < 3 && action.equals("select")) {
            if(!Sanitiser.isNumber(parameters[1])) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Input").setDescription("**" + parameters[1] + "** isn't a valid message id.");
                MessageHandler.sendMessage(e, embed.build());
                return;
            }

            selectedMessageId = e.getChannel().getMessageById(parameters[1]).complete().getId();
            selectedMessages.remove(e.getAuthor().getId());
            selectedMessages.put(e.getAuthor().getId(), selectedMessageId);

            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("Reaction Role")
                    .setDescription(e.getChannel().getMessageById(selectedMessageId).complete().toString() + " has been selected.")
                    .addField("Options", e.getPrefix() + "reactrole add <:emote:> <@role>\n" + e.getPrefix() + "reactrole rem <:emote:> <@role>", true);
            MessageHandler.sendMessage(e, embed.build());
            return;
        }

        // Equivalent to a null check for selectedMessage
        if(!selectedMessages.containsKey(e.getAuthor().getId())) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("No Selection").setDescription("You need to select a message using `" + e.getPrefix() + "reactrole select`, or `" + e.getPrefix() + "reactrole select <Message ID>` before you can add or remove a reaction role from it.");
            MessageHandler.sendMessage(e, embed.build());
            return;
        }

        final Message finalMessage = e.getChannel().getMessageById(selectedMessageId).complete();
        final Emote emote = (e.getMessage().getEmotes().size() > 0) ? e.getMessage().getEmotes().get(0) : null;
        final Role role = (e.getMessage().getMentionedRoles().size() > 0) ? e.getMessage().getMentionedRoles().get(0) : e.getGuild().getRoleById((parameters.length > 2) ? parameters[2] : "0");

        // Emote null check.
        if(emote == null) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("No Emote").setDescription("I couldn't detect any tagged custom emotes in the command...");
            MessageHandler.sendMessage(e, embed.build());
            return;
        }

        // Role null and action check.
        if(!action.equals("rem") && role == null) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("No Role").setDescription("I couldn't detect any tagged roles in the command...");
            MessageHandler.sendMessage(e, embed.build());
            return;
        }

        // Checks to make sure the emote both exists and is available on the server.
        if(!e.getGuild().getEmoteCache().asList().contains(emote)) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Emote").setDescription("This emote is unavailable for use in a reaction role. Make sure that you are using emotes from this server.");
            MessageHandler.sendMessage(e, embed.build());
            return;
        }

        // Checks if the action is not `rem` before making role specific checks.
        if(!action.equals("rem")) {
            // Checks if the role exists, is available for use.
            if(!e.getGuild().getRoleCache().asList().contains(role)) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Role").setDescription("This role is unavailable for use in a reaction role. Make sure that you are using roles from this server.");
                MessageHandler.sendMessage(e, embed.build());
                return;
            }

            // Checks if role is lower in the hierarchy than Yuuko's highest role.
            if(role.getPositionRaw() >= highestSelfRole.getPositionRaw()) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Role").setDescription("I cannot assign roles that are higher than or equal to my highest role in the hierarchy.");
                MessageHandler.sendMessage(e, embed.build());
                return;
            }
        }

        if(action.equals("add")) {
            finalMessage.addReaction(emote).queue(
                    s -> {
                        if(ReactionRoleFunctions.addReactionRole(e.getGuild(), finalMessage, emote, role)) {
                            EmbedBuilder embed = new EmbedBuilder().setTitle("Success").setDescription("I was successfully able to pair emote " + emote.getAsMention() + " to role " + role.getAsMention() + " for message " + finalMessage + ".");
                            MessageHandler.sendMessage(e, embed.build());
                        } else {
                            EmbedBuilder embed = new EmbedBuilder().setTitle("Exists").setDescription("A reaction role using this message and emote combination already exists.");
                            MessageHandler.sendMessage(e, embed.build());
                        }
                    },
                    f -> {
                        EmbedBuilder embed = new EmbedBuilder().setTitle("Error").setDescription("I was unable to add a reaction to the selected message.");
                        MessageHandler.sendMessage(e, embed.build());
                    });
        } else if(action.equals("rem")) {
            if(!ReactionRoleFunctions.hasReactionRole(finalMessage, emote)) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("No Reaction Role").setDescription("I couldn't find any reaction roles to remove associated with message " + finalMessage + " and emote " + emote.getAsMention() + "... Doing nothing.");
                MessageHandler.sendMessage(e, embed.build());
                return;
            }

            for(MessageReaction messageReaction: finalMessage.getReactions()) {
                if(messageReaction.getReactionEmote().getEmote().equals(emote)) {
                    messageReaction.removeReaction().queue(
                            s -> {
                                ReactionRoleFunctions.removeReactionRole(finalMessage, emote);
                                EmbedBuilder embed = new EmbedBuilder().setTitle("Success").setDescription("I was successfully able to remove reaction role " + emote.getAsMention() + " from message " + finalMessage + ".");
                                MessageHandler.sendMessage(e, embed.build());
                            },
                            f -> {
                                EmbedBuilder embed = new EmbedBuilder().setTitle("Error").setDescription("I was unable to remove the reaction from the selected message.");
                                MessageHandler.sendMessage(e, embed.build());
                            });
                    return;
                }
            }
        }
    }

    /**
     * Processes GenericGuildMessageReactionEvent events to apply or remove roles from users.
     *
     * @param e GenericGuildMessageReactionEvent
     */
    public static void processReaction(GenericGuildMessageReactionEvent e) {
        final Emote emote = e.getReactionEmote().getEmote();

        e.getChannel().getMessageById(e.getMessageId()).queue(message -> {
            if(message == null || emote == null || !ReactionRoleFunctions.hasReactionRole(message, emote)) {
                return;
            }

            final Role role = e.getGuild().getRoleById(ReactionRoleFunctions.selectReactionRole(message, emote));
            if(e instanceof GuildMessageReactionAddEvent) {
                e.getGuild().getController().addSingleRoleToMember(e.getMember(), role).queue();
            } else {
                e.getGuild().getController().removeSingleRoleFromMember(e.getMember(), role).queue();
            }
        });
    }
}
