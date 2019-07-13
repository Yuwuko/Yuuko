package com.yuuko.core.utilities;

import com.yuuko.core.MessageHandler;
import com.yuuko.core.events.entity.MessageEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;

import java.time.LocalDateTime;
import java.util.Arrays;

public final class Sanitiser {

    /**
     * Checks a command to ensure all parameters are present.
     *
     * @param expectedParameters int
     * @return boolean
     */
    public static boolean checkParameters(MessageEvent e, int expectedParameters, boolean feedback) {
        if(expectedParameters == 0) {
            return true;
        }

        if(!e.hasParameters()) {
            if(feedback) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Missing Parameters").setDescription("Command expected **" + expectedParameters + "** or more parameters and you provided **0**.");
                MessageHandler.sendMessage(e, embed.build());
            }
            return false;
        }

        if(expectedParameters > 1) {
            String[] commandParameters = e.getParameters().split("\\s+", expectedParameters);
            if(commandParameters.length < expectedParameters) {
                if(feedback) {
                    EmbedBuilder embed = new EmbedBuilder().setTitle("Missing Parameters").setDescription("Command expected **" + expectedParameters + "** or more parameters and you provided **" + commandParameters.length + "**.");
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
