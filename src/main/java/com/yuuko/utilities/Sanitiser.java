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
     *
     * @param event {@link MessageEvent}
     * @return boolean
     */
    public static boolean checks(MessageEvent event) {
        // Is the member null? (this doesn't happen but is a nullable field according to JDA documentation)
        if(event.getMember() == null) {
            return false;
        }

        // Checks if command is null at this point because the next checks require that it does.
        if(event.getCommand() == null) {
            return false;
        }

        // Is the module accessed the developer module and is the commander not me?
        if(event.getModule().getName().equals("developer") && event.getMember().getIdLong() != 215161101460045834L) {
            return false;
        }

        // Does the command contain the minimum number of parameters?
        return meetsParameterMinimum(event, true);
    }

    /**
     * Checks a command to ensure all parameters are present.
     *
     * @param e {@link MessageEvent}
     * @param feedback boolean
     * @return boolean
     */
    public static boolean meetsParameterMinimum(MessageEvent e, boolean feedback, int... override) {
        int minimumParameters = e.getCommand().getMinimumParameters();

        if(override != null && override.length > 0) {
            minimumParameters = override[0];
        }

        if(minimumParameters > 0 && !e.hasParameters()) {
            if(feedback) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Missing Parameters").setDescription("Command expected **" + minimumParameters + "** or more parameters and you provided **0**.");
                MessageDispatcher.reply(e, embed.build());
            }
            return false;
        }

        if(minimumParameters > 1) {
            String[] commandParameters = e.getParameters().split("\\s+", minimumParameters);
            if(commandParameters.length < minimumParameters) {
                if(feedback) {
                    EmbedBuilder embed = new EmbedBuilder().setTitle("Missing Parameters").setDescription("Command expected **" + minimumParameters + "** or more parameters and you provided **" + commandParameters.length + "**.");
                    MessageDispatcher.reply(e, embed.build());
                }
                return false;
            }
        }

        return true;
    }

    /**
     * Checks to see if the command executor can interact with the command target.
     *
     * @param e {@link MessageEvent}
     * @param member {@link Member}
     * @return boolean
     */
    public static boolean canInteract(MessageEvent e, Member member, String reason, boolean feedback) {
        if(!e.getGuild().getSelfMember().canInteract(member)) {
            if(feedback) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Target").setDescription("I cannot interact with that user because they have an equal or a higher role in the hierarchy to me.");
                MessageDispatcher.reply(e, embed.build());
            }
            return false;
        }

        if(!e.getMember().canInteract(member)) {
            if(feedback) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Target").setDescription("You cannot `" + reason + "` someone with a higher or equal role in the hierarchy to yourself.");
                MessageDispatcher.reply(e, embed.build());
            }
            return false;
        }

        return true;
    }

    /**
     * Checks to see if a string is a number or not without the whole Integer.parseInt() exception thang.
     *
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
     *
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
     *
     * @return String
     */
    public static String scrub(String string, boolean encodeBlank) {
        string = string.replaceAll("[!@#$%^&*(),.?\":{}|<>]", "");
        return encodeBlank ? string.replace(" ", "%20") : string;
    }
}
