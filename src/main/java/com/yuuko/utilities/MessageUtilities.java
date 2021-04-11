package com.yuuko.utilities;

import com.yuuko.MessageDispatcher;
import com.yuuko.Yuuko;
import com.yuuko.events.entity.MessageEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.List;

public final class MessageUtilities {

    /**
     * Returns whether or not a user is mentioned in the message.
     * @param context {@link MessageEvent}
     * @return boolean
     */
    public static boolean checkIfUserMentioned(MessageEvent context) {
        return context.getMessage().getMentionedMembers().size() > 0;
    }

    /**
     * Returns whether or not a channel is mentioned in the message.
     * @param context {@link MessageEvent}
     * @return boolean
     */
    public static boolean checkIfChannelMentioned(MessageEvent context) {
        return context.getMessage().getMentionedChannels().size() > 0;
    }

    /**
     * Returns the first mentioned user from a given message.
     * @param context {@link MessageEvent}
     * @return {@link Member}
     */
    public static Member getMentionedMember(MessageEvent context, boolean feedback) {
        if(context.hasParameters() && context.getParameters().length() == 18 && Sanitiser.isNumeric(context.getParameters())) {
            return context.getGuild().getMemberById(context.getParameters());
        }

        List<Member> mentioned = getMutableMembersCollection(context);
        if(context.getMessage().mentionsEveryone()) {
            if(feedback) {
                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle(context.i18n("invalid_param", "message_utils"))
                        .setDescription(context.i18n("invalid_self", "message_utils"));
                MessageDispatcher.reply(context, embed.build());
            }
            return null;
        }

        if(mentioned.size() < 1) {
            if(feedback) {
                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle(context.i18n("invalid_param", "message_utils"))
                        .setDescription(context.i18n("invalid_none", "message_utils"));
                MessageDispatcher.reply(context, embed.build());
            }
            return null;
        }

        return mentioned.get(0);
    }

    /**
     * Returns a list of mentioned users from a given message.
     * @param context {@link MessageEvent}
     * @return {@link List<Member>}
     */
    public static List<Member> getMentionedMembers(MessageEvent context) {
        List<Member> mentioned = context.getMessage().getMentionedMembers();
        ArrayList<Member> modifiableMentioned = new ArrayList<>(mentioned);
        modifiableMentioned.remove(context.getGuild().getMember(Yuuko.BOT));

        if(!context.getMessage().mentionsEveryone()) {
            return modifiableMentioned;
        } else {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(context.i18n("invalid_param", "message_utils"))
                    .setDescription(context.i18n("invalid_self", "message_utils"));
            MessageDispatcher.reply(context, embed.build());
            return null;
        }
    }

    /**
     * Since JDA by default returns unmodifiable collections, we need one that is... (that also removes if the bot is mentioned!)
     * @param context {@link MessageEvent}
     * @return {@link List<Member>}
     */
    private static List<Member> getMutableMembersCollection(MessageEvent context) {
        List<Member> unmodifiableMentioned = context.getMessage().getMentionedMembers();
        ArrayList<Member> mentioned = new ArrayList<>(unmodifiableMentioned);
        mentioned.remove(context.getGuild().getMember(Yuuko.BOT));
        return mentioned;
    }

    /**
     * Returns the first mentioned channel from a given message.
     * @param context {@link MessageEvent}
     * @return {@link TextChannel}
     */
    public static TextChannel getFirstMentionedChannel(MessageEvent context) {
        List<TextChannel> mentioned = context.getMessage().getMentionedChannels();
        return (mentioned.size() > 0) ? mentioned.get(0) : null;
    }

}
