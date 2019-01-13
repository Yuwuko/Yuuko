package com.yuuko.core.utilities;

import com.yuuko.core.Cache;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public final class MessageHandler {

    private static final Logger log = LoggerFactory.getLogger(MessageHandler.class);

    /**
     * Sends a message, saving those precious bytes.
     * @param event GenericMessageEvent
     * @param message String
     */
    public static void sendMessage(GenericMessageEvent event, String message) {
        try {
            log.trace("Invoking {}#getTextChannel()#sendMessage(message)#queue()", event.getClass().getName());
            event.getTextChannel().sendMessage(message).queue();
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", event.getClass().getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Sends an embedded message.
     * @param event GenericMessageEvent
     * @param embed MessageEmbed
     */
    public static void sendMessage(GenericMessageEvent event, MessageEmbed embed) {
        try {
            log.trace("Invoking {}#getTextChannel()#sendMessage(embed)#queue()", event.getClass().getName());
            event.getTextChannel().sendMessage(embed).queue();
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", event.getClass().getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Sends a file via message channel.
     * @param event GenericMessageEvent
     * @param file File
     */
    public static void sendMessage(GenericMessageEvent event, File file) {
        try {
            log.trace("Invoking {}#getTextChannel()#sendFile(file)#queue()", event.getClass().getName());
            event.getTextChannel().sendFile(file).queue();
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", event.getClass().getSimpleName(), ex.getMessage(), ex);
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
            log.trace("Invoking {}#getChannel()#sendFile(bytes, fileName)#queue()", event.getClass().getName());
            event.getChannel().sendFile(bytes, fileName).queue();
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", event.getClass().getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Sends a message via message channel.
     * @param channel MessageChannel
     * @param message String
     */
    public static void sendMessage(MessageChannel channel, String message) {
        try {
            log.trace("Invoking {}#sendMessage(message)#queue()", channel.getClass().getName());
            channel.sendMessage(message).queue();
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", channel.getClass().getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Sends an embedded message via message channel.
     * @param channel MessageChannel
     * @param embed MessageEmbed
     */
    public static void sendMessage(MessageChannel channel, MessageEmbed embed) {
        try {
            log.trace("Invoking {}#sendMessage(embed)#queue()", channel.getClass().getName());
            channel.sendMessage(embed).queue();
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", channel.getClass().getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Sends a file via message channel.
     * @param channel MessageChannel
     * @param file File
     */
    public static void sendMessage(MessageChannel channel, File file) {
        try {
            log.trace("Invoking {}#sendFile(file)#queue()", channel.getClass().getName());
            channel.sendFile(file).queue();
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", channel.getClass().getSimpleName(), ex.getMessage(), ex);
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
            log.trace("Invoking {}#sendFile(bytes, fileName)#queue()", channel.getClass().getName());
            channel.sendFile(bytes, fileName).queue();
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", channel.getClass().getSimpleName(), ex.getMessage(), ex);
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
}
