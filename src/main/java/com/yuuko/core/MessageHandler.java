package com.yuuko.core;

import com.yuuko.core.events.entity.MessageEvent;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.GenericGuildEvent;
import net.dv8tion.jda.api.events.message.guild.GenericGuildMessageEvent;
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
            if(hasSendPermission(event)) {
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
            if(hasSendPermission(event)) {
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
            if(hasSendPermission(event)) {
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
            if(hasSendPermission(event)) {
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
            if(hasSendPermission(event, channel)) {
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
            if(hasSendPermission(event, channel)) {
                channel.sendMessage(embed).queue();
            }
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", event.getClass().getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Sends a message to the provided channel.
     *
     * @param event GenericGuildEvent
     * @param channel TextChannel
     * @param message String
     */
    public static void sendMessage(GenericGuildEvent event, TextChannel channel, String message) {
        try {
            if(hasSendPermission(event, channel)) {
                channel.sendMessage(message).queue();
            }
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", event.getClass().getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Checks to see if the bot has permission to write messages in the given server/channel. This prevents JDA throwing exceptions.

     * @param guild Guild
     * @param channel TextChannel
     * @return boolean
     */
    private static boolean hasSendPermission(Guild guild, TextChannel channel) {
        return guild.getSelfMember().hasPermission(Permission.MESSAGE_READ, Permission.MESSAGE_WRITE)
                && guild.getSelfMember().hasPermission(channel, Permission.MESSAGE_READ, Permission.MESSAGE_WRITE);
    }

    /**
     * Main hasSendPermission flow controller
     */
    private static boolean hasSendPermission(GenericGuildMessageEvent e) {
        return hasSendPermission(e.getGuild(), e.getChannel());
    }

    /**
     * Main hasSendPermission flow controller
     */
    private static boolean hasSendPermission(GenericGuildMessageEvent e, TextChannel channel) {
        return hasSendPermission(e.getGuild(), channel);
    }

    /**
     * Main hasSendPermission flow controller
     */
    private static boolean hasSendPermission(GenericGuildEvent e, TextChannel channel) {
        return hasSendPermission(e.getGuild(), channel);
    }
}
