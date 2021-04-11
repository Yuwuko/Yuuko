package com.yuuko.utilities;

import com.yuuko.MessageDispatcher;
import com.yuuko.events.entity.MessageEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

import java.sql.Timestamp;
import java.util.Arrays;

public final class Sanitiser {

    /**
     * Checks method that allows for fail-fast before any further database calls are made,
     * preventing access to the CommandExecutor class where nulls will throw exceptions.
     * @param context {@link MessageEvent}
     * @return boolean
     */
    public static boolean checks(MessageEvent context) {
        // Is the member null? (this doesn't happen but is a nullable field according to JDA documentation)
        if(context.getMember() == null) {
            return false;
        }

        // Checks if command is null at this point because the next checks require that it does.
        if(context.getCommand() == null) {
            return false;
        }

        // Is the module accessed the developer module and is the commander not me?
        if(context.getModule().getName().equals("developer") && context.getMember().getIdLong() != 215161101460045834L) {
            return false;
        }

        // Does the command contain the minimum number of parameters?
        return meetsParameterMinimum(context, true);
    }

    /**
     * Checks a command to ensure all parameters are present.
     * @param context {@link MessageEvent}
     * @param feedback boolean
     * @return boolean
     */
    public static boolean meetsParameterMinimum(MessageEvent context, boolean feedback, int... override) {
        int minimumParameters = context.getCommand().getMinimumParameters();

        if(override != null && override.length > 0) {
            minimumParameters = override[0];
        }

        if(minimumParameters > 0 && !context.hasParameters()) {
            if(feedback) {
                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle(context.i18n("missing_param", "sanitiser"))
                        .setDescription(context.i18n("missing_count_zero", "sanitiser"));
                MessageDispatcher.reply(context, embed.build());
            }
            return false;
        }

        if(minimumParameters > 1) {
            String[] commandParameters = context.getParameters().split("\\s+", minimumParameters);
            if(commandParameters.length < minimumParameters) {
                if(feedback) {
                    EmbedBuilder embed = new EmbedBuilder()
                            .setTitle(context.i18n("missing_param", "sanitiser"))
                            .setDescription(context.i18n("missing_count_num", "sanitiser").formatted(minimumParameters, commandParameters.length));
                    MessageDispatcher.reply(context, embed.build());
                }
                return false;
            }
        }

        return true;
    }

    /**
     * Checks to see if the command executor can interact with the command target.
     * @param context {@link MessageEvent}
     * @param member {@link Member}
     * @param reason String
     * @param feedback boolean
     * @return boolean
     */
    public static boolean canInteract(MessageEvent context, Member member, String reason, boolean feedback) {
        if(!context.getGuild().getSelfMember().canInteract(member)) {
            if(feedback) {
                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle(context.i18n("invalid_target", "sanitiser"))
                        .setDescription(context.i18n("invalid_bot", "sanitiser"));
                MessageDispatcher.reply(context, embed.build());
            }
            return false;
        }

        if(!context.getMember().canInteract(member)) {
            if(feedback) {
                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle(context.i18n("invalid_target", "sanitiser"))
                        .setDescription(context.i18n("invalid_user", "sanitiser").formatted(reason));
                MessageDispatcher.reply(context, embed.build());
            }
            return false;
        }

        return true;
    }

    /**
     * Checks to see if a string is a number or not without the whole Integer.parseInt() exception thang.
     * @param string {@link String}
     * @return boolean
     */
    public static boolean isNumeric(String string) {
        return Arrays.stream(string.split("")).allMatch(character -> Character.isDigit(character.charAt(0)));
    }

    /**
     * Checks if the given string is a valid boolean, more-so, is that boolean true?
     * @param string String
     * @return boolean
     */
    public static boolean isBooleanTrue(String string) {
        return (string.equalsIgnoreCase("true") || string.equalsIgnoreCase("yes") || string.equalsIgnoreCase("1"));
    }

    /**
     * Checks to see if the given timestamp is valid. I know using try/catch is bad practice in this case but whatcha gonna do?
     * @param string {@link String}
     * @return boolean
     */
    public static boolean isTimestamp(String string) {
        try {
            Timestamp.valueOf(string);
            return true;
        } catch(IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Takes a string and removes all special characters and replaces spaces with %20s.
     * @param string {@link String}
     * @param encodeBlank boolean.
     * @return String
     */
    public static String scrub(String string, boolean encodeBlank) {
        string = string.replaceAll("[!@#$%^&*(),.?\":{}|<>]", "");
        return encodeBlank ? string.replace(" ", "%20") : string;
    }
}
