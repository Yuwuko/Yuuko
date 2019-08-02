package com.yuuko.core.utilities;

import com.yuuko.core.MessageHandler;
import com.yuuko.core.events.entity.MessageEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

import java.time.LocalDateTime;
import java.util.Arrays;

public final class Sanitiser {

    /**
     * Checks method that allows for fail-fast before any further database calls are made,
     * preventing access to the CommandExecutor class where nulls will throw exceptions.
     *
     * @param event MessageEvent
     * @return boolean
     */
    public static boolean checks(MessageEvent event) {
        // Is the member null? (this doesn't happen but is a nullable field according to JDA documentation)
        if(event.getMember() == null) {
            return false;
        }

        // Is the module accessed the developer module and is the commander not me?
        if(event.getModule().getName().equals("developer") && event.getMember().getIdLong() != 215161101460045834L) {
            return false;
        }

        // Does the command contain the minimum number of parameters?
        if(!meetsParameterMinimum(event, true)) {
            return false;
        }

        if(event.getPrefix() == null) {
            return false;
        }

        return event.getCommand() != null;

    }

    /**
     * Checks a command to ensure all parameters are present.
     *
     * @param e MessageEvent
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
                MessageHandler.sendMessage(e, embed.build());
            }
            return false;
        }

        if(minimumParameters > 1) {
            String[] commandParameters = e.getParameters().split("\\s+", minimumParameters);
            if(commandParameters.length < minimumParameters) {
                if(feedback) {
                    EmbedBuilder embed = new EmbedBuilder().setTitle("Missing Parameters").setDescription("Command expected **" + minimumParameters + "** or more parameters and you provided **" + commandParameters.length + "**.");
                    MessageHandler.sendMessage(e, embed.build());
                }
                return false;
            }
        }

        return true;
    }

    /**
     * Checks to see if the command executor can interact with the command target.
     *
     * @param e MessageEvent
     * @param member Member
     * @return boolean
     */
    public static boolean canInteract(MessageEvent e, Member member, String reason, boolean feedback) {
        if(!e.getGuild().getSelfMember().canInteract(member)) {
            if(feedback) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Target").setDescription("I cannot interact with that user because they have an equal or a higher role in the hierarchy to me.");
                MessageHandler.sendMessage(e, embed.build());
            }
            return false;
        }

        if(!e.getMember().canInteract(member)) {
            if(feedback) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Invalid Target").setDescription("You cannot `" + reason + "` someone with a higher or equal role in the hierarchy to yourself.");
                MessageHandler.sendMessage(e, embed.build());
            }
            return false;
        }

        return true;
    }

    /**
     * Checks to see if a string is a number or not without the whole Integer.parseInt() exception thang.
     *
     * @param string String
     * @return boolean
     */
    public static boolean isNumber(String string) {
        return Arrays.stream(string.split("")).allMatch(character -> Character.isDigit(character.charAt(0)));
    }

    /**
     * Checks to see if the given date is a valid date of the format dd/MM/yyyy.
     *
     * @param string date string.
     * @return boolean
     */
    public static boolean isDate(String string) {
        String[] strings = string.split("/");

        if(string.length() < 3) {
            return false;
        }

        for(String stringy : strings) {
            if(!isNumber(stringy)) {
                return false;
            }
        }

        int month = Integer.parseInt(strings[1]);

        if(month <= 12) {
            if(month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
                return Integer.parseInt(strings[0]) <= 31;
            } else if(month == 2) {
                if(LocalDateTime.now().getYear() % 4 != 0) { // Leap Year
                    return Integer.parseInt(strings[0]) <= 28;
                } else {
                    return Integer.parseInt(strings[0]) <= 29;
                }
            } else {
                return Integer.parseInt(strings[0]) <= 30;
            }
        } else {
            return false;
        }
    }

    /**
     * Takes a string and removes all special characters and replaces spaces with %20s.
     * @param string url string.
     * @param encodeBlank boolean.
     *
     * @return String
     */
    public static String scrubString(String string, boolean encodeBlank) {
        string = string.replaceAll("[!@#$%^&*(),.?\":{}|<>]", "");

        return encodeBlank ? string.replace(" ", "%20") : string;
    }
}
