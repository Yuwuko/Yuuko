package com.yuuko.core.utilities;

import com.yuuko.core.Configuration;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.events.entity.MessageEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.ArrayList;
import java.util.List;

public final class MessageUtilities {

    /**
     * Returns whether or not a user is mentioned in the message.
     *
     * @param e MessageEvent
     * @return boolean
     */
    public static boolean checkIfUserMentioned(MessageEvent e) {
        return getMutableMembersCollection(e).size() > 0;
    }

    /**
     * Returns whether or not a channel is mentioned in the message.
     *
     * @param e MessageEvent
     * @return boolean
     */
    public static boolean checkIfChannelMentioned(MessageEvent e) {
        return e.getMessage().getMentionedChannels().size() > 0;
    }

    /**
     * Returns the first mentioned user from a given message.
     *
     * @param e MessageEvent
     * @return Member
     */
    public static Member getMentionedMember(MessageEvent e, boolean feedback) {
        if(e.hasParameters() && e.getParameters().length() == 18 && Sanitiser.isNumber(e.getParameters())) {
            return e.getGuild().getMemberById(e.getParameters());
        }

        List<Member> mentioned = getMutableMembersCollection(e);
        if(e.getMessage().mentionsEveryone()) {
            if(feedback) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Parameters").setDescription("You cannot do _that_ to everyone.");
                MessageHandler.sendMessage(e, embed.build());
            }
            return null;
        }

        if(mentioned.size() < 1) {
            if(feedback) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Missing Parameters").setDescription("There were no mentioned users found.");
                MessageHandler.sendMessage(e, embed.build());
            }
            return null;
        }

        return mentioned.get(0);
    }

    /**
     * Returns a list of mentioned users from a given message.
     *
     * @param e MessageEvent
     * @return List<Member>
     */
    public static List<Member> getMentionedMembers(MessageEvent e) {
        List<Member> mentioned = e.getMessage().getMentionedMembers();
        ArrayList<Member> modifiableMentioned = new ArrayList<>(mentioned);
        modifiableMentioned.remove(e.getGuild().getMember(Configuration.BOT));

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
     *
     * @param e MessageEvent
     * @return List<Member>
     */
    private static List<Member> getMutableMembersCollection(MessageEvent e) {
        List<Member> unmodifiableMentioned = e.getMessage().getMentionedMembers();
        ArrayList<Member> mentioned = new ArrayList<>(unmodifiableMentioned);
        mentioned.remove(e.getGuild().getMember(Configuration.BOT));
        return mentioned;
    }

    /**
     * Returns the first mentioned channel from a given message.
     *
     * @param e MessageEvent
     * @return TextChannel
     */
    public static TextChannel getFirstMentionedChannel(MessageEvent e) {
        List<TextChannel> mentioned = e.getMessage().getMentionedChannels();
        return (mentioned.size() > 0) ? mentioned.get(0) : null;
    }

}
