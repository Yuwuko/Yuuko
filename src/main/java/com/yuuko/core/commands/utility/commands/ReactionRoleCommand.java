package com.yuuko.core.commands.utility.commands;

import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.utility.UtilityModule;
import com.yuuko.core.events.extensions.MessageEvent;
import com.yuuko.core.utilities.Sanitiser;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Role;

import java.util.HashMap;

public class ReactionRoleCommand extends Command {

    private static final HashMap<String, Message> selectedMessages = new HashMap<>();

    public ReactionRoleCommand() {
        super("reactrole", UtilityModule.class, 1, new String[]{"-reactrole select", "-reactrole select <Message ID>", "-reactrole add <:emote:> <@role>", "-reactrole rem <:emote:> <@role>"}, false, new Permission[]{Permission.MANAGE_ROLES});
    }

    @Override
    public void onCommand(MessageEvent e) {
        final String channelId = e.getTextChannel().getId();
        String[] parameters = e.getCommand()[1].split("\\s+");
        Message selectedMessage = selectedMessages.get(e.getChannel().getId());


        if(parameters.length < 2) {
            selectedMessage = e.getTextChannel().getHistoryBefore(e.getTextChannel().getLatestMessageId(), 10).complete().getRetrievedHistory().get(0);
            selectedMessages.remove(channelId);
            selectedMessages.put(channelId, selectedMessage);

            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("Reaction Role")
                    .setDescription(selectedMessage.toString() + " has been selected.")
                    .addField("Options", e.getPrefix() + "reactrole add <:emote:> <@role>\n" + e.getPrefix() + "reactrole rem <:emote:> <@role>", true);
            MessageHandler.sendMessage(e, embed.build());
            return;
        }

        if(parameters.length < 3) {
            if(!Sanitiser.isNumber(parameters[1])) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Input").setDescription("**" + parameters[1] + "** isn't a valid message id.");
                MessageHandler.sendMessage(e, embed.build());
                return;
            }

            selectedMessage = e.getTextChannel().getMessageById(parameters[1]).complete();
            selectedMessages.remove(channelId);
            selectedMessages.put(channelId, selectedMessage);

            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("Reaction Role")
                    .setDescription(selectedMessage.toString() + " has been selected.")
                    .addField("Options", e.getPrefix() + "reactrole add <:emote:> <@role>\n" + e.getPrefix() + "reactrole rem <:emote:> <@role>", true);
            MessageHandler.sendMessage(e, embed.build());
            return;
        }

        if(!selectedMessages.containsKey(e.getChannel().getId())) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("No Selection").setDescription("You need to select a message using `" + e.getPrefix() + "reactrole select`, or `" + e.getPrefix() + "reactrole select <Message ID>` before you can add or remove a reaction role from it.");
            MessageHandler.sendMessage(e, embed.build());
            return;
        }

        Emote emote = (e.getMessage().getEmotes().size() > 0) ? e.getMessage().getEmotes().get(0) : null;
        Role role = (e.getMessage().getMentionedRoles().size() > 0) ? e.getMessage().getMentionedRoles().get(0) : null;

        if(emote != null && role != null && selectedMessage != null) {
            selectedMessage.addReaction(emote).queue();
        }
    }
}
