// Program: BasketBandit (Discord Bot)
// Programmer: Joshua Mark Hunt
// Version: 02/05/2018 - JDK 10.0.1

package basketbandit.core.module;

import basketbandit.core.Configuration;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionRemoveEvent;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

class ModuleUtility {

    private MessageReceivedEvent e;
    private String[] command;

    /**
     * Module constructor for MessageReceivedEvents
     * @param e MessageReceivedEvent
     */
    ModuleUtility(MessageReceivedEvent e) {
        this.e = e;
        command = e.getMessage().getContentRaw().split("\\s+", 2);

        if(command[0].toLowerCase().equals(Configuration.PREFIX + "user")) {
            commandUser();
        } else if(command[0].toLowerCase().equals(Configuration.PREFIX + "server")) {
            commandServer();
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
     * Info command will return information about the given user.
     */
    private void commandUser() {
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
            String roleString = "";

            for(Role role : infoRoles) {
                roleString += role.getName() + ", ";
            }

            if(!roleString.equals("")) {
                int index = roleString.lastIndexOf(", ");
                roleString = new StringBuilder(roleString).replace(index, index + 1, "").toString();
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
                                    "Roles                         ::  " + roleString + "\n"
                    )
                    .setFooter("Version: " + Configuration.VERSION + ", Information requested by " + e.getMember().getEffectiveName(), e.getGuild().getMemberById(Configuration.BOT_ID).getUser().getAvatarUrl());
        }

        if(commandInfo != null) {
            e.getTextChannel().sendMessage(commandInfo.build()).queue();
        } else {
            e.getTextChannel().sendMessage("Sorry... I couldn't find that user... <:SagiriIzumi:420417275359657986>").queue();
        }
    }

    /**
     * Info command will return information about the server.
     */
    private void commandServer() {
        Guild server = e.getGuild();

        EmbedBuilder commandInfo = new EmbedBuilder()
                .setColor(Color.RED)
                .setTitle("Server information for: " + server.getName(), null)
                .setThumbnail(server.getIconUrl())
                .setDescription(
                        "Server Owner                 ::  " + server.getOwner().getUser().getName() + "#" + server.getOwner().getUser().getDiscriminator() + " (" + server.getOwner().getEffectiveName() + ")" + "\n" +
                        "Server ID                         ::  " + server.getIdLong() + "\n" +
                        "Server Created               ::  " + server.getCreationTime().toLocalDateTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy, hh:mma")) + "\n" +
                        "Server Region                 ::  " + server.getRegion().getName() + "\n" +
                        "Total Users                     ::  " + server.getMembers().size() + "\n" +
                        "Total Text Channels     \u200a::  " + server.getTextChannels().size() + "\n" +
                        "Total Voice Channels   ::  " + server.getVoiceChannels().size() + "\n" +
                        "Total Roles                      ::  " + server.getRoles().size() + "\n"
                )
                .setFooter("Version: " + Configuration.VERSION, e.getGuild().getMemberById(Configuration.BOT_ID).getUser().getAvatarUrl());

        if(commandInfo != null) {
            e.getTextChannel().sendMessage(commandInfo.build()).queue();
        } else {
            e.getTextChannel().sendMessage("Sorry... Something went wrong! :(").queue();
        }
    }

}