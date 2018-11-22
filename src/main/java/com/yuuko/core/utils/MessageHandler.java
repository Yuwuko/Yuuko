package com.yuuko.core.utils;

import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;

import java.io.File;

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

}
