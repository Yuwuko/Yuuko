package com.yuuko.core.utilities;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.List;

public class MessageUtility {

    /**
     * Returns the first mentioned user from a given message.
     * @param e MessageReceivedEvent
     * @return Member
     */
    public static Member getFirstMentionedMember(MessageReceivedEvent e) {
        List<Member> mentioned = e.getMessage().getMentionedMembers();

        if(!e.getMessage().mentionsEveryone()) {
            if(mentioned.size() > 0) {
                return mentioned.get(0);
            } else {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Missing Parameters").setDescription("There were no mentioned users found.");
                MessageHandler.sendMessage(e, embed.build());
                return null;
            }
        } else {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Parameters").setDescription("You cannot do _that_ to everyone.");
            MessageHandler.sendMessage(e, embed.build());
            return null;
        }
    }

    public static boolean checkIfUserMentioned(MessageReceivedEvent e) {
        return !e.getMessage().getMentionedMembers().isEmpty();
    }

    /**
     * Returns a list of mentioned users from a given message.
     * @param e MessageReceivedEvent
     * @return List<Member>
     */
    public static List<Member> getMentionedMembers(MessageReceivedEvent e) {
        if(!e.getMessage().mentionsEveryone()) {
            return e.getMessage().getMentionedMembers();
        } else {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Parameters").setDescription("You cannot do _that_ to everyone.");
            MessageHandler.sendMessage(e, embed.build());
            return null;
        }
    }

}
