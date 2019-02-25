package com.yuuko.core.utilities;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Arrays;

public final class Sanitiser {

    /**
     * Checks a command to ensure all parameters are present.
     *
     * @param command String[]
     * @param expectedParameters int
     * @return boolean
     */
    public static boolean checkParameters(MessageReceivedEvent e, String[] command, int expectedParameters, boolean feedback) {
        if(expectedParameters == 0) {
            return true;
        }

        if(command.length < 2) {
            if(feedback) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Missing Parameters").setDescription("Command expected **" + expectedParameters + "** or more parameters and you provided **0**.");
                MessageHandler.sendMessage(e, embed.build());
            }
            return false;
        }

        if(expectedParameters > 1) {
            String[] commandParameters = command[1].split("\\s+", expectedParameters);
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
     * @param e MessageReceivedEvent
     * @param member Member
     * @return boolean
     */
    public static boolean canInteract(MessageReceivedEvent e, Member member, String reason, boolean feedback) {
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
}
