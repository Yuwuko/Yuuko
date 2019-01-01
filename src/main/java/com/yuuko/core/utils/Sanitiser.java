package com.yuuko.core.utils;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public final class Sanitiser {

    /**
     * Checks a command to ensure all parameters are present.
     * @param command String[]
     * @param expectedParameters int
     * @return boolean
     */
    public static boolean checkParameters(MessageReceivedEvent e, String[] command, int expectedParameters) {
        if(expectedParameters == 0) {
            return true;
        }

        if(command.length < 2) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Missing Parameters").setDescription("Command expected **" + expectedParameters + "** or more parameters and you provided **0**.");
            MessageHandler.sendMessage(e, embed.build());
            return false;
        }

        if(expectedParameters > 1) {
            String[] commandParameters = command[1].split("\\s+", expectedParameters);
            if(commandParameters.length < expectedParameters) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Missing Parameters").setDescription("Command expected **" + expectedParameters + "** or more parameters and you provided **" + commandParameters.length + "**.");
                MessageHandler.sendMessage(e, embed.build());
                return false;
            }
        }

        return true;
    }

    /**
     * Checks to see if a string is a number or not without the whole Integer.parseInt() exception thang.
     * @param string String
     * @return boolean
     */
    public static boolean isNumber(String string) {
        String[] characters = string.split("");
        for(String character : characters){
            if(!Character.isDigit(character.charAt(0))) {
                return false;
            }
        }
        return true;
    }
}
