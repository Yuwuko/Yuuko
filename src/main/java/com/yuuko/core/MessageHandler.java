package com.yuuko.core;

import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.scheduler.ScheduleHandler;
import com.yuuko.core.scheduler.jobs.MessageDeleteJob;
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
     * @param event {@link MessageEvent}
     * @param message {@link String}
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
     * Sends a file via message channel.
     *
     * @param event {@link MessageEvent}
     * @param file {@link File}
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
     * @param event {@link MessageEvent}
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
     * Sends a message to the provided channel.
     *
     * @param event {@link GenericGuildEvent}
     * @param channel {@link TextChannel}
     * @param message {@link String}
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
     * Sends an embedded message.
     *
     * @param event {@link MessageEvent}
     * @param embed {@link MessageEmbed}
     */
    public static void sendMessage(MessageEvent event, MessageEmbed embed) {
        try {
            if(hasEmbedSendPermission(event)) {
                event.getChannel().sendMessage(embed).queue();
            }
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", event.getClass().getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Sends an embedded message to a supplied channel.
     *
     * @param event {@link MessageEvent}
     * @param channel {@link TextChannel}
     * @param embed {@link MessageEmbed}
     */
    public static void sendMessage(MessageEvent event, TextChannel channel, MessageEmbed embed) {
        try {
            if(hasEmbedSendPermission(event, channel)) {
                channel.sendMessage(embed).queue();
            }
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", event.getClass().getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Sends an embedded message to a supplied channel.
     *
     * @param event {@link GenericGuildEvent}
     * @param channel {@link TextChannel}
     * @param embed {@link MessageEmbed}
     */
    public static void sendMessage(GenericGuildEvent event, TextChannel channel, MessageEmbed embed) {
        try {
            if(hasEmbedSendPermission(event, channel)) {
                channel.sendMessage(embed).queue();
            }
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", event.getClass().getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Sends an embedded reply to the given message.
     *
     * @param event {@link MessageEvent}
     * @param embed {@link MessageEmbed}
     */
    public static void reply(MessageEvent event, MessageEmbed embed) {
        try {
            if(hasEmbedSendPermission(event)) {
                event.getMessage().reply(embed).queue(s -> {}, f -> sendMessage(event, embed));
            }
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", event.getClass().getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Sends an embedded reply to the given message.
     *
     * @param event {@link MessageEvent}
     * @param message {@link MessageEmbed}
     */
    public static void reply(MessageEvent event, String message) {
        try {
            if(hasEmbedSendPermission(event)) {
                event.getMessage().reply(message).queue(s -> {}, f -> sendMessage(event, message));
            }
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", event.getClass().getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Sends a message to a supplied channel.
     *
     * @param event {@link GenericGuildEvent}
     * @param channel {@link TextChannel}
     * @param message {@link MessageEmbed}
     */
    public static void sendTempMessage(GenericGuildEvent event, TextChannel channel, String message) {
        try {
            if(hasSendPermission(event, channel)) {
                channel.sendMessage(message).queue(s -> ScheduleHandler.registerUniqueJob(new MessageDeleteJob(s)));
            }
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", event.getClass().getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Sends an embedded message to a supplied channel.
     *
     * @param event {@link GenericGuildEvent}
     * @param channel {@link TextChannel}
     * @param embed {@link MessageEmbed}
     */
    public static void sendTempMessage(GenericGuildEvent event, TextChannel channel, MessageEmbed embed) {
        try {
            if(hasEmbedSendPermission(event, channel)) {
                channel.sendMessage(embed).queue(s -> ScheduleHandler.registerUniqueJob(new MessageDeleteJob(s)));
            }
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", event.getClass().getSimpleName(), ex.getMessage(), ex);
        }
    }

    /**
     * Checks to see if the bot has permission to write messages in the given server/channel. This prevents JDA throwing exceptions.

     * @param guild {@link Guild}
     * @param channel {@link TextChannel}
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

    /**
     * Checks to see if the bot has extended permission to write embedded messages with links in the given server/channel.
     *
     * @param guild {@link Guild}
     * @param channel {@link TextChannel}
     * @return boolean
     */
    private static boolean hasEmbedSendPermission(Guild guild, TextChannel channel) {
        return guild.getSelfMember().hasPermission(Permission.MESSAGE_READ, Permission.MESSAGE_WRITE, Permission.MESSAGE_EMBED_LINKS)
                && guild.getSelfMember().hasPermission(channel, Permission.MESSAGE_READ, Permission.MESSAGE_WRITE, Permission.MESSAGE_EMBED_LINKS);
    }

    /**
     * Auxiliary hasEmbedSendPermission flow controller
     */
    private static boolean hasEmbedSendPermission(GenericGuildMessageEvent e) {
        return hasEmbedSendPermission(e.getGuild(), e.getChannel());
    }

    /**
     * Auxiliary hasEmbedSendPermission flow controller
     */
    private static boolean hasEmbedSendPermission(GenericGuildMessageEvent e, TextChannel channel) {
        return hasEmbedSendPermission(e.getGuild(), channel);
    }

    /**
     * Auxiliary hasEmbedSendPermission flow controller
     */
    private static boolean hasEmbedSendPermission(GenericGuildEvent e, TextChannel channel) {
        return hasEmbedSendPermission(e.getGuild(), channel);
    }
}
