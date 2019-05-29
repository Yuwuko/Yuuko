package com.yuuko.core.utilities;

import com.yuuko.core.Configuration;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.events.extensions.MessageEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.guild.GenericGuildEvent;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;
import net.dv8tion.jda.core.events.message.guild.GenericGuildMessageEvent;

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
        if(e.getCommand().get(1) != null && e.getCommand().get(1).length() == 18 && Sanitiser.isNumber(e.getCommand().get(1))) {
            return e.getGuild().getMemberById(e.getCommand().get(1));
        }

        List<Member> mentioned = getMutableMembersCollection(e);

        if(!e.getMessage().mentionsEveryone()) {
            if(mentioned.size() > 0) {
                return mentioned.get(0);
            } else {
                if(feedback) {
                    EmbedBuilder embed = new EmbedBuilder().setTitle("Missing Parameters").setDescription("There were no mentioned users found.");
                    MessageHandler.sendMessage(e, embed.build());
                }
                return null;
            }
        } else {
            if(feedback) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Parameters").setDescription("You cannot do _that_ to everyone.");
                MessageHandler.sendMessage(e, embed.build());
            }
            return null;
        }
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
     * Checks to see if the bot has permission to write messages in the given server/channel. This prevents JDA throwing exceptions.

     * @param guild Guild
     * @param channel TextChannel
     * @return boolean
     */
    private static boolean hasSendPermission(Guild guild, TextChannel channel) {
        Member bot = guild.getMemberById(Configuration.BOT_ID);
        return (bot.hasPermission(Permission.MESSAGE_WRITE) && bot.hasPermission(channel, Permission.MESSAGE_WRITE));
    }

    /**
     * Main hasSendPermission flow controller
     */
    public static boolean hasSendPermission(GenericGuildMessageEvent e) {
        return hasSendPermission(e.getGuild(), e.getChannel());
    }

    /**
     * Main hasSendPermission flow controller
     */
    public static boolean hasSendPermission(GenericMessageEvent e, TextChannel channel) {
        return hasSendPermission(e.getGuild(), channel);
    }

    /**
     * Main hasSendPermission flow controller
     */
    public static boolean hasSendPermission(GenericGuildEvent e, TextChannel channel) {
        return hasSendPermission(e.getGuild(), channel);
    }

}
