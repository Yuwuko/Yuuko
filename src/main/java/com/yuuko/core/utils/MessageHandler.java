package com.yuuko.core.utils;

import com.yuuko.core.Cache;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.io.File;
import java.time.Instant;

public final class MessageHandler {

    /**
     * Sends a message, saving those precious bytes.
     * @param event GenericMessageEvent
     * @param message String
     */
    public static void sendMessage(GenericMessageEvent event, String message) {
        try {
            event.getTextChannel().sendMessage(message).queue();
        } catch(Exception ex) {
            //
        }
    }

    /**
     * Sends an embedded message.
     * @param event GenericMessageEvent
     * @param message MessageEmbed
     */
    public static void sendMessage(GenericMessageEvent event, MessageEmbed message) {
        try {
            event.getTextChannel().sendMessage(message).queue();
        } catch(Exception ex) {
            //
        }
    }

    /**
     * Sends a file via message channel.
     * @param event GenericMessageEvent
     * @param file File
     */
    public static void sendMessage(GenericMessageEvent event, File file) {
        try {
            event.getTextChannel().sendFile(file).queue();
        } catch(Exception ex) {
            //
        }
    }

    /**
     * Sends a file via message channel.
     * @param event GenericMessageEvent
     * @param bytes byte[]
     * @param fileName String
     */
    public static void sendMessage(GenericMessageEvent event, byte[] bytes, String fileName) {
        try {
            event.getChannel().sendFile(bytes, fileName).queue();
        } catch(Exception ex) {
            //
        }
    }

    /**
     * Sends a message via message channel.
     * @param channel MessageChannel
     * @param message String
     */
    public static void sendMessage(MessageChannel channel, String message) {
        try {
            channel.sendMessage(message).queue();
        } catch(Exception ex) {
            //
        }
    }

    /**
     * Sends an embedded message via message channel.
     * @param channel MessageChannel
     * @param message MessageEmbed
     */
    public static void sendMessage(MessageChannel channel, MessageEmbed message) {
        try {
            channel.sendMessage(message).queue();
        } catch(Exception ex) {
            //
        }
    }

    /**
     * Sends a file via message channel.
     * @param channel MessageChannel
     * @param file File
     */
    public static void sendMessage(MessageChannel channel, File file) {
        try {
            channel.sendFile(file).queue();
        } catch(Exception ex) {
            //
        }
    }

    /**
     * Sends a file via message channel.
     * @param channel MessageChannel
     * @param bytes byte[]
     * @param fileName String
     */
    public static void sendMessage(MessageChannel channel, byte[] bytes, String fileName) {
        try {
            channel.sendFile(bytes, fileName).queue();
        } catch(Exception ex) {
            //
        }
    }

    /**
     * Sends an exception to the support server's exception channel.
     * @param ex Exception
     * @param command String
     */
    public static void sendException(Exception ex, String command) {
        try {
            MessageChannel channel = Cache.JDA.getTextChannelById(520158641484201994L);

            StringBuilder traceString = new StringBuilder();
            for(StackTraceElement trace: ex.getStackTrace()) {
                traceString.append(trace.toString());
                traceString.append("\n");
            }

            channel.sendMessage(command + "\n`" + traceString.toString() + "`").queue();
        } catch(Exception exc) {
            //
        }
    }

    public static void sendCommand(MessageReceivedEvent e, long executionTimeMs) {
        try {
            Cache.JDA.getTextChannelById("526328163580772352").sendMessage("```" + Instant.now().toString() + " >> " + e.getMessage().getContentDisplay() + " >> " + e.getMessage().getGuild().getName() + " >> Execution time: " + executionTimeMs + "ms```").queue();
        } catch(Exception ex) {
            //
        }
    }
}
