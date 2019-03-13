package com.yuuko.core.utilities;

import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class TextUtility {

    /**
     * Replaces the last occurrence of a pattern with nothing.
     *
     * @param stringBuilder StringBuilder
     * @param pattern String
     * @return StringBuilder
     */
    public static void removeLastOccurrence(StringBuilder stringBuilder, String pattern) {
        int index = stringBuilder.lastIndexOf(pattern);
        if(index > -1) {
            stringBuilder.replace(index, index + 1, "");
        }
    }

    /**
     * Replaces the last occurrence of a pattern with nothing.
     *
     * @param stringBuffer StringBuilder
     * @param pattern String
     * @return StringBuilder
     */
    public static void removeLastOccurrence(StringBuffer stringBuffer, String pattern) {
        int index = stringBuffer.lastIndexOf(pattern);
        if(index > -1) {
            stringBuffer.replace(index, index + 1, "");
        }
    }

    /**
     * Extracts the module name from a class path.
     *
     * @param string String
     * @param shortened boolean
     * @return String
     */
    public static String extractModuleName(String string, boolean shortened, boolean lowercase) {
        String returnString = (shortened) ? string.substring(string.lastIndexOf(".") + 1).replace("Module", "") : string.substring(string.lastIndexOf(".") + 1);
        return (lowercase) ? returnString.toLowerCase() : returnString;
    }

    /**
     * Converts a string to a boolean value, essentially just converting 1, true or yes to TRUE and anything else to FALSE.
     *
     * @param value value to convert to boolean
     * @return boolean
     */
    public static boolean convertToBoolean(String value) {
        value = (value == null) ? "false" : value; // Stop null pointer exceptions.
        return value.equals("1") || value.equalsIgnoreCase("true") || value.equalsIgnoreCase("yes");
    }

    /**
     * Gets current songs timeStamp.
     *
     * @param milliseconds how many milliseconds of the song has played.
     * @return formatted timeStamp.
     */
    public static String getTimestamp(long milliseconds) {
        int seconds = (int) (milliseconds / 1000) % 60 ;
        int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
        int hours   = (int) ((milliseconds / (1000 * 60 * 60)) % 24);
        int days    = (int) ((milliseconds / (1000 * 60 * 60 * 24)));

        if(days > 0) {
            return String.format("%02d:%02d:%02d:%02d", days, hours, minutes, seconds);
        } else if(hours > 0) {
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format("%02d:%02d", minutes, seconds);
        }
    }

    /**
     * Gets verbose time string from time in milliseconds.
     *
     * @param milliseconds amount of time in milliseconds to convert.
     * @return formatted time string.
     */
    public static String getTimestampVerbose(long milliseconds) {
        int seconds = (int) (milliseconds / 1000) % 60 ;
        int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
        int hours   = (int) ((milliseconds / (1000 * 60 * 60)) % 24);
        int days    = (int) ((milliseconds / (1000 * 60 * 60 * 24)) % 7);
        int weeks   = (int) ((milliseconds / (1000 * 60 * 60 * 24 * 7)));

        if(weeks > 0) {
            return String.format("%02d weeks, %02d days, %02d hours, %02d minutes, %02d seconds.", weeks, days, hours, minutes, seconds);
        } else if(days > 0) {
            return String.format("%02d days, %02d hours, %02d minutes, %02d seconds.", days, hours, minutes, seconds);
        } else if(hours > 0) {
            return String.format("%02d hours, %02d minutes, %02d seconds.", hours, minutes, seconds);
        } else {
            return String.format("%02d minutes, %02d seconds.", minutes, seconds);
        }
    }

    /**
     * Takes a String and GuildMemberJoinEvent and resolves tokens within the string.
     *
     * @param e GuildMemberJoinEvent
     * @param message String
     * @return resolved string
     */
    public static String untokenizeString(GuildMemberJoinEvent e, String message) {
        message = message.replace("%user%", e.getMember().getAsMention());
        message = message.replace("%guild%", e.getGuild().getName());
        return message;
    }

    /**
     * Takes a String and MessageReceivedEvent and resolves tokens within the string.
     *
     * @param e MessageReceivedEvent
     * @param message String
     * @return resolved string
     */
    public static String untokenizeString(MessageReceivedEvent e, String message) {
        message = message.replace("%user%", e.getMember().getAsMention());
        message = message.replace("%guild%", e.getGuild().getName());
        return message;
    }

}
