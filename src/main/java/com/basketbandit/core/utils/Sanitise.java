package com.basketbandit.core.utils;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Sanitise {

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
            MessageHandler.sendMessage(e, "Command expected " + expectedParameters + " or more parameters and you provided 0.");
            return false;
        }

        if(expectedParameters > 1) {
            String[] commandParameters = command[1].split("\\s+", expectedParameters);
            if(commandParameters.length < expectedParameters) {
                MessageHandler.sendMessage(e, "Command expected " + expectedParameters + " or more parameters and you provided " + commandParameters.length + ".");
                return false;
            }
        }

        return true;
    }

}
