// Program: BasketBandit (Discord Bot)
// Programmer: Joshua Mark Hunt
// Version: 02/05/2018 - JDK 10.0.1

package basketbandit.core.modules;

import basketbandit.core.Configuration;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.time.OffsetDateTime;
import java.util.List;

public class ModuleModeration {

    private MessageReceivedEvent e;
    private String[] command;

    /**
     * Module constructor for MessageReceivedEvents
     * @param e MessageReceivedEvent
     */
    public ModuleModeration(MessageReceivedEvent e) {
        this.e = e;
        command = e.getMessage().getContentRaw().split("\\s+", 4);

        if(command[0].toLowerCase().equals(Configuration.PREFIX + "nuke") && e.getMember().hasPermission(Permission.MESSAGE_MANAGE)) {
            commandNuke();
        } else if(command[0].toLowerCase().equals(Configuration.PREFIX + "kick") && e.getMember().hasPermission(Permission.KICK_MEMBERS)) {
            commandKick();
        } else if(command[0].toLowerCase().equals(Configuration.PREFIX + "ban") && e.getMember().hasPermission(Permission.BAN_MEMBERS)) {
            commandBan();
        } else if(command[0].toLowerCase().equals(Configuration.PREFIX + "addchannel") && e.getMember().hasPermission(Permission.MANAGE_CHANNEL)) {
            commandCreateChannel();
        } else if(command[0].toLowerCase().equals(Configuration.PREFIX + "delchannel") && e.getMember().hasPermission(Permission.MANAGE_CHANNEL)) {
            commandRemoveChannel();
        }
    }

    /**
     * Nuke command removes a set number of messages.
     */
    private void commandNuke() {
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
     * Kicks the given user from the server.
     */
    private void commandKick() {
        try {
            if(command[1].length() == 18 && Long.parseLong(command[1]) > 0) {
                if(command.length < 3) {
                    e.getGuild().getController().kick(command[1]).queue();
                } else {
                    e.getGuild().getController().kick(command[1], command[2]).queue();
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Bans the given user from the server. Command parts: ban /userID/ /lengthInDays/ /reason (optional)/
     */
    private void commandBan() {
        try {
            if(command[1].length() == 18 && Long.parseLong(command[1]+"L") > 0) {
                if(command[3].isEmpty()) {
                    e.getGuild().getController().ban(command[1], Integer.parseInt(command[2])).queue();
                } else {
                    e.getGuild().getController().ban(command[1], Integer.parseInt(command[2]), command[3]).queue();
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a new channel.
     */
    private void commandCreateChannel() {
        try {
            if(command[1].toLowerCase().equals("text")) {
                e.getGuild().getController().createTextChannel(command[2]).setNSFW(command.length > 3).queue();
            } else if(command[1].toLowerCase().equals("voice")) {
                e.getGuild().getController().createVoiceChannel(command[2]).queue();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes a channel.
     */
    private void commandRemoveChannel() {
        try {
            if(command[1].toLowerCase().equals("text") && command[2].length() == 18 && Long.parseLong(command[2]) > 0) {
                e.getGuild().getTextChannelById(command[2]).delete().queue();
            } else if(command[1].toLowerCase().equals("voice") && command[2].length() == 18 && Long.parseLong(command[2]) > 0) {
                e.getGuild().getVoiceChannelById(command[2]).delete().queue();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}