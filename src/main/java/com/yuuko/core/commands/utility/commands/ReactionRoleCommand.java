package com.yuuko.core.commands.utility.commands;

import com.yuuko.core.Configuration;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.database.function.ReactionRoleFunctions;
import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.utilities.Sanitiser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.react.GenericGuildMessageReactionEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

import java.util.Arrays;
import java.util.HashMap;

public class ReactionRoleCommand extends Command {
    private static final HashMap<String, String> selectedMessages = new HashMap<>();
    private MessageEvent e;
    private String[] parameters;
    private String action;
    private String selectedMessageId;
    private Role highestSelfRole;
    private Emote emote;
    private Message message;

    public ReactionRoleCommand() {
        super("reactrole", Configuration.MODULES.get("utility"), 1, Arrays.asList("-reactrole select", "-reactrole select <Message ID>", "-reactrole add <:emote:> <@role>", "-reactrole rem <:emote:>"), false, Arrays.asList(Permission.MANAGE_ROLES, Permission.MESSAGE_HISTORY));
    }

    @Override
    public void onCommand(MessageEvent e) {
        this.parameters = e.getParameters().split("\\s+");
        this.highestSelfRole = e.getGuild().getSelfMember().getRoles().get(0); // Role list is ordered from highest to lowest
        this.action = parameters[0].toLowerCase();
        this.selectedMessageId = selectedMessages.get(e.getAuthor().getId());

        if(action.equals("select")) {
            selectMessage();
            return;
        }

        // null check for selectedMessage
        if(!selectedMessages.containsKey(e.getAuthor().getId())) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("No Selection").setDescription("You need to select a message using `" + e.getPrefix() + "reactrole select`, or `" + e.getPrefix() + "reactrole select <Message ID>` before you can add or remove a reaction role from it.");
            MessageHandler.sendMessage(e, embed.build());
            return;
        }

        message = e.getChannel().retrieveMessageById(selectedMessageId).complete();
        emote = (e.getMessage().getEmotes().size() > 0) ? e.getMessage().getEmotes().get(0) : null; // assign emote here because both add/removeReactionRole methods use it

        // emote null check.
        if(emote == null) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("No Emote").setDescription("I couldn't detect any tagged custom emotes in the command...");
            MessageHandler.sendMessage(e, embed.build());
            return;
        }

        // make sure the emote both exists and is available on the server.
        if(!e.getGuild().getEmoteCache().asList().contains(emote)) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Emote").setDescription("This emote is unavailable for use in a reaction role. Make sure that you are using emotes from this server.");
            MessageHandler.sendMessage(e, embed.build());
            return;
        }

        switch(action) {
            case "add" -> addReactionRole();
            case "rem" -> removeReactionRole();
        }
    }

    /**
     * Selects a message for use in a future reaction role action.
     */
    private void selectMessage() {
        if(parameters.length < 2) {
            selectedMessageId = e.getChannel().getHistoryBefore(e.getChannel().getLatestMessageId(), 1).complete().getRetrievedHistory().get(0).getId();
        } else {
            if(!Sanitiser.isNumber(parameters[1])) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Input").setDescription("**" + parameters[1] + "** isn't a valid message id.");
                MessageHandler.sendMessage(e, embed.build());
                return;
            }
            selectedMessageId = e.getChannel().retrieveMessageById(parameters[1]).complete().getId();
        }

        selectedMessages.remove(e.getAuthor().getId());
        selectedMessages.put(e.getAuthor().getId(), selectedMessageId);

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Reaction Role")
                .setDescription(e.getChannel().retrieveMessageById(selectedMessageId).complete().toString() + " has been selected.")
                .addField("Options", e.getPrefix() + "reactrole add <:emote:> <@role>\n" + e.getPrefix() + "reactrole rem <:emote:> <@role>", true);
        MessageHandler.sendMessage(e, embed.build());
    }

    /**
     * Adds a reaction role to the selected message
     */
    private void addReactionRole() {
        final Role role = (e.getMessage().getMentionedRoles().size() > 0) ? e.getMessage().getMentionedRoles().get(0) : e.getGuild().getRoleById((parameters.length > 2) ? parameters[2] : "0");

        // role null and action check.
        if(role == null) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("No Role").setDescription("I couldn't detect any tagged roles in the command...");
            MessageHandler.sendMessage(e, embed.build());
            return;
        }

        // check if the role exists, is available for use.
        if(!e.getGuild().getRoleCache().asList().contains(role)) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Role").setDescription("This role is unavailable for use in a reaction role. Make sure that you are using roles from this server.");
            MessageHandler.sendMessage(e, embed.build());
            return;
        }

        // checks if role is lower in the hierarchy than Yuuko's highest role.
        if(role.getPositionRaw() >= highestSelfRole.getPositionRaw()) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Role").setDescription("I cannot assign roles that are higher than or equal to my highest role in the hierarchy.");
            MessageHandler.sendMessage(e, embed.build());
            return;
        }

        message.addReaction(emote).queue(
                s -> {
                    if(ReactionRoleFunctions.addReactionRole(e.getGuild(), message, emote, role)) {
                        EmbedBuilder embed = new EmbedBuilder().setTitle("Success").setDescription("I was successfully able to pair emote " + emote.getAsMention() + " to role " + role.getAsMention() + " for message " + message + ".");
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
    }

    /**
     * Removes a reaction role from the selected message.
     */
    private void removeReactionRole() {
        if(!ReactionRoleFunctions.hasReactionRole(message, emote)) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("No Reaction Role").setDescription("I couldn't find any reaction roles to remove associated with message " + message + " and emote " + emote.getAsMention() + "... Doing nothing.");
            MessageHandler.sendMessage(e, embed.build());
            return;
        }

        for(MessageReaction messageReaction: message.getReactions()) {
            if(messageReaction.getReactionEmote().getEmote().equals(emote)) {
                messageReaction.removeReaction().queue(
                        s -> {
                            ReactionRoleFunctions.removeReactionRole(message, emote);
                            EmbedBuilder embed = new EmbedBuilder().setTitle("Success").setDescription("I was successfully able to remove reaction role " + emote.getAsMention() + " from message " + message + ".");
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

    /**
     * Processes GenericGuildMessageReactionEvent events to apply or remove roles from users.
     *
     * @param e GenericGuildMessageReactionEvent
     */
    public static void processReaction(GenericGuildMessageReactionEvent e) {
        final String message = e.getMessageId();
        final Emote emote = e.getReactionEmote().getEmote();

        if(!ReactionRoleFunctions.hasReactionRole(message, emote.getId())) {
            return;
        }

        // Warning says argument may be null, but the above check ensures it isn't.
        final Role role = e.getGuild().getRoleById(ReactionRoleFunctions.selectReactionRole(message, emote.getId()));

        if(role == null) {
            return;
        }

        if(e instanceof GuildMessageReactionAddEvent) {
            e.getGuild().addRoleToMember(e.getMember(), role).queue();
        } else {
            e.getGuild().removeRoleFromMember(e.getMember(), role).queue();
        }
    }
}
