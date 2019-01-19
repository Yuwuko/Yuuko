package com.yuuko.core.utilities;

import com.yuuko.core.Cache;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

public class MessageUtility {

    /**
     * Returns whether or not a user is mentioned in the message.
     * @param e MessageReceivedEvent
     * @return boolean
     */
    public static boolean checkIfUserMentioned(MessageReceivedEvent e) {
        return getMutableMembersCollection(e).size() > 0;
    }

    /**
     * Returns whether or not a channel is mentioned in the message.
     * @param e MessageReceivedEvent
     * @return boolean
     */
    public static boolean checkIfChannelMentioned(MessageReceivedEvent e) {
        return e.getMessage().getMentionedChannels().size() > 0;
    }

    /**
     * Returns the first mentioned user from a given message.
     * @param e MessageReceivedEvent
     * @return Member
     */
    public static Member getFirstMentionedMember(MessageReceivedEvent e) {
        List<Member> mentioned = getMutableMembersCollection(e);

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

    /**
     * Returns the first mentioned channel from a given message.
     * @param e MessageReceivedEvent
     * @return TextChannel
     */
    public static TextChannel getFirstMentionedChannel(MessageReceivedEvent e) {
        List<TextChannel> mentioned = e.getMessage().getMentionedChannels();
        return (mentioned.size() > 0) ? mentioned.get(0) : null;
    }

    /**
     * Returns a list of mentioned users from a given message.
     * @param e MessageReceivedEvent
     * @return List<Member>
     */
    public static List<Member> getMentionedMembers(MessageReceivedEvent e) {
        List<Member> mentioned = e.getMessage().getMentionedMembers();
        ArrayList<Member> modifiableMentioned = new ArrayList<>(mentioned);
        modifiableMentioned.remove(e.getGuild().getMember(Cache.BOT));

        if(!e.getMessage().mentionsEveryone()) {
            return modifiableMentioned;
        } else {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Parameters").setDescription("You cannot do _that_ to everyone.");
            MessageHandler.sendMessage(e, embed.build());
            return null;
        }
    }

    /**
     * Since JDA by default returns unmodifiable collections, we need one that is... (that also removes if the bot is mentioned!)
     * @param e MessageReceivedEvent
     * @return List<Member>
     */
    private static List<Member> getMutableMembersCollection(MessageReceivedEvent e) {
        List<Member> unmodifiableMentioned = e.getMessage().getMentionedMembers();
        ArrayList<Member> mentioned = new ArrayList<>(unmodifiableMentioned);
        mentioned.remove(e.getGuild().getMember(Cache.BOT));
        return mentioned;
    }

}
