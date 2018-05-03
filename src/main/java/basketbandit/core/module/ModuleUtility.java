// Program: BasketBandit (Discord Bot)
// Programmer: Joshua Mark Hunt
// Version: 02/05/2018 - JDK 10.0.1

package basketbandit.core.module;

import basketbandit.core.Configuration;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionRemoveEvent;

import java.awt.*;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

class ModuleUtility {

    private MessageReceivedEvent e;

    /**
     * Module constructor for MessageReceivedEvents
     * @param e MessageReceivedEvent
     */
    ModuleUtility(MessageReceivedEvent e) {
        this.e = e;
        String[] command = e.getMessage().getContentRaw().split("\\s+", 2);

        if(command[0].toLowerCase().equals(Configuration.PREFIX + "nuke") && e.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            commandNuke(command);
        } else if(command[0].toLowerCase().equals(Configuration.PREFIX + "info")) {
            commandInfo(command);
        }
    }

    /**
     * Module constructor for MessageReactionAddEvents
     * @param e MessageReactionAddEvent
     */
    ModuleUtility(MessageReactionAddEvent e) {
        if(e.getReaction().getReactionEmote().getName().equals("\uD83D\uDCCC")) {
            Message message = e.getTextChannel().getMessageById(e.getMessageId()).complete();
            message.pin().queue();
        }
    }

    /**
     * Module constructor for MessageReactionRemoveEvents
     * @param e MessageReactionRemoveEvent
     */
    ModuleUtility(MessageReactionRemoveEvent e) {
        Message message = e.getTextChannel().getMessageById(e.getMessageId()).complete();

        if(e.getReactionEmote().getName().equals("\uD83D\uDCCC")) {
            for(MessageReaction emote: message.getReactions()) {
                if(emote.getReactionEmote().getName().equals("\uD83D\uDCCC")) {
                    return;
                }
            }
            message.unpin().queue();
        }
    }

    /**
     * Nuke command removes a set number of messages.
     * @param command the original command.
     */
    private void commandNuke(String[] command) {
        try {
            List<Message> nukeList = e.getTextChannel().getHistory().retrievePast(Integer.parseInt(command[1])).complete();
            if(Integer.parseInt(command[1]) < 2) {
                e.getTextChannel().deleteMessageById(nukeList.get(0).getId()).complete();
            } else {
                // If a message in the nuke list is older than 2 weeks it can't be mass deleted, so recursion will need to take place.
                for(Message message: nukeList) {
                    if(message.getCreationTime().isBefore(OffsetDateTime.now().minusWeeks(2))) {
                        message.delete().complete();
                    }
                }
                e.getGuild().getTextChannelById(e.getTextChannel().getId()).deleteMessages(nukeList).complete();
            }
        } catch(Exception e) {
            // Do nothing.
        }
    }

    /**
     * Info command will return information about the given user.
     * @param command the original command.
     */
    private void commandInfo(String[] command) {
        Member infoMember = null;
        EmbedBuilder commandInfo = null;

        for(Member member : e.getGuild().getMembers()) {
            if(member.getEffectiveName().toLowerCase().equals(command[1].toLowerCase()) || member.getEffectiveName().toLowerCase().contains(command[1].toLowerCase())) {
                infoMember = member;
                break;
            }
        }

        if(infoMember != null) {
            // Gets user's roles, replaces the last comma with nothing.
            List<Role> infoRoles = infoMember.getRoles();
            String infoString = "";

            for(Role role : infoRoles) {
                infoString += role.getName() + ", ";
            }

            if(!infoString.equals("")) {
                int index = infoString.lastIndexOf(", ");
                infoString = new StringBuilder(infoString).replace(index, index + 1, "").toString();
            }

            commandInfo = new EmbedBuilder()
                    .setColor(Color.RED)
                    .setAuthor("User information about " + infoMember.getEffectiveName() + ",", null, infoMember.getUser().getAvatarUrl())
                    .setTitle("User is currently "+infoMember.getOnlineStatus())
                    .setDescription(
                            "Username                ::  " + infoMember.getUser().getName() + "#" + infoMember.getUser().getDiscriminator() + "\n" +
                                    "UserID                      ::  " + infoMember.getUser().getIdLong() + "\n" +
                                    "Account Created   \u200a\u200a::  " + infoMember.getUser().getCreationTime().format(DateTimeFormatter.ofPattern("d MMM yyyy  hh:mma")) + "\n" +
                                    "Joined Server          \u200a\u200a::  " + infoMember.getJoinDate().format(DateTimeFormatter.ofPattern("d MMM yyyy  hh:mma")) + "\n" +
                                    "Mutual Servers       ::  " + infoMember.getUser().getMutualGuilds().size() + "\n" +
                                    "Roles                         ::  " + infoString + "\n"
                    )
                    .setFooter("Version: " + Configuration.VERSION + ", Information requested by " + e.getMember().getEffectiveName(), e.getGuild().getMemberById(Configuration.BOT_ID).getUser().getAvatarUrl());
        }

        if(commandInfo != null) {
            e.getTextChannel().sendMessage(commandInfo.build()).queue();
        } else {
            e.getTextChannel().sendMessage("Sorry... I couldn't find that user... <:SagiriIzumi:420417275359657986>").queue();
        }
    }

}