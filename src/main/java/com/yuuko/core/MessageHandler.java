package com.yuuko.core;

import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.metrics.MetricsManager;
import com.yuuko.core.utilities.MessageUtilities;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.guild.GenericGuildEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public final class MessageHandler {
    private static final Logger log = LoggerFactory.getLogger(MessageHandler.class);

    /**
     * Sends a message, saving those precious bytes.
     *
     * @param event GenericMessageEvent
     * @param message String
     */
    public static void sendMessage(MessageEvent event, String message) {
        try {
            if(MessageUtilities.hasSendPermission(event)) {
                log.trace("Invoking {}#getChannel()#sendMessage(message)#queue()", event.getClass().getName());
                MetricsManager.getEventMetrics().OUTPUTS.getAndIncrement();
                event.getChannel().sendMessage(message).queue();
            }
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", event.getClass().getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Sends an embedded message.
     *
     * @param event GenericMessageEvent
     * @param embed MessageEmbed
     */
    public static void sendMessage(MessageEvent event, MessageEmbed embed) {
        try {
            if(MessageUtilities.hasSendPermission(event)) {
                log.trace("Invoking {}#getChannel()#sendMessage(embed)#queue()", event.getClass().getName());
                MetricsManager.getEventMetrics().OUTPUTS.getAndIncrement();
                event.getChannel().sendMessage(embed).queue();
            }
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", event.getClass().getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Sends a file via message channel.
     *
     * @param event GenericMessageEvent
     * @param file File
     */
    public static void sendMessage(MessageEvent event, File file) {
        try {
            if(MessageUtilities.hasSendPermission(event)) {
                log.trace("Invoking {}#getChannel()#sendFile(file)#queue()", event.getClass().getName());
                MetricsManager.getEventMetrics().OUTPUTS.getAndIncrement();
                event.getChannel().sendFile(file).queue();
            }
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", event.getClass().getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Sends a file via message channel.
     *
     * @param event GenericMessageEvent
     * @param bytes byte[]
     * @param fileName String
     */
    public static void sendMessage(MessageEvent event, byte[] bytes, String fileName) {
        try {
            if(MessageUtilities.hasSendPermission(event)) {
                log.trace("Invoking {}#getChannel()#sendFile(bytes, fileName)#queue()", event.getClass().getName());
                MetricsManager.getEventMetrics().OUTPUTS.getAndIncrement();
                event.getChannel().sendFile(bytes, fileName).queue();
            }
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", event.getClass().getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Sends an embedded message to a supplied channel.
     *
     * @param event GenericMessageEvent
     * @param channel TextChannel
     * @param embed MessageEmbed
     */
    public static void sendMessage(MessageEvent event, TextChannel channel, MessageEmbed embed) {
        try {
            if(MessageUtilities.hasSendPermission(event, channel)) {
                log.trace("Invoking {}#getChannel()#sendFile(bytes, fileName)#queue()", event.getClass().getName());
                MetricsManager.getEventMetrics().OUTPUTS.getAndIncrement();
                channel.sendMessage(embed).queue();
            }
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", event.getClass().getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Sends an embedded message to a supplied channel.
     *
     * @param event GenericGuildEvent
     * @param channel TextChannel
     * @param embed MessageEmbed
     */
    public static void sendMessage(GenericGuildEvent event, TextChannel channel, MessageEmbed embed) {
        try {
            if(MessageUtilities.hasSendPermission(event, channel)) {
                log.trace("Invoking {}#getChannel()#sendFile(bytes, fileName)#queue()", event.getClass().getName());
                MetricsManager.getEventMetrics().OUTPUTS.getAndIncrement();
                channel.sendMessage(embed).queue();
            }
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", event.getClass().getSimpleName(), ex.getMessage(), ex);
        }
    }

}
