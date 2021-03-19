package com.yuuko;

import com.yuuko.events.entity.MessageEvent;
import com.yuuko.scheduler.ScheduleHandler;
import com.yuuko.scheduler.jobs.MessageDeleteJob;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.GenericGuildEvent;
import net.dv8tion.jda.api.events.message.guild.GenericGuildMessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

public final class MessageDispatcher {
    private static final Logger log = LoggerFactory.getLogger(MessageDispatcher.class);

    /**
     * Sends a message, saving those precious bytes.
     *
     * @param event {@link MessageEvent}
     * @param message {@link String}
     */
    public static boolean sendMessage(MessageEvent event, String message) {
        try {
            if(hasSendPermission(event)) {
                AtomicBoolean success = new AtomicBoolean(false);
                event.getChannel().sendMessage(message).queue(s -> success.set(true), f -> success.set(false));
                return success.get();
            }
            return false;
        } catch(Exception e) {
            log.error("An error occurred while running the {} class, message: {}", event.getClass().getSimpleName(), e.getMessage(), e);
            return false;
        }
    }

    /**
     * Sends a file via message channel.
     *
     * @param event {@link MessageEvent}
     * @param file {@link File}
     */
    public static boolean sendMessage(MessageEvent event, File file) {
        try {
            if(hasSendPermission(event)) {
                AtomicBoolean success = new AtomicBoolean(false);
                event.getChannel().sendFile(file).queue(s -> success.set(true), f -> success.set(false));
                return success.get();
            }
            return false;
        } catch(Exception e) {
            log.error("An error occurred while running the {} class, message: {}", event.getClass().getSimpleName(), e.getMessage(), e);
            return false;
        }
    }

    /**
     * Sends a file via message channel.
     *
     * @param event {@link MessageEvent}
     * @param bytes byte[]
     * @param fileName String
     */
    public static boolean sendMessage(MessageEvent event, byte[] bytes, String fileName) {
        try {
            if(hasSendPermission(event)) {
                AtomicBoolean success = new AtomicBoolean(false);
                event.getChannel().sendFile(bytes, fileName).queue(s -> success.set(true), f -> success.set(false));
                return success.get();
            }
            return false;
        } catch(Exception e) {
            log.error("An error occurred while running the {} class, message: {}", event.getClass().getSimpleName(), e.getMessage(), e);
            return false;
        }
    }

    /**
     * Sends a message to the provided channel.
     *
     * @param event {@link GenericGuildEvent}
     * @param channel {@link TextChannel}
     * @param message {@link String}
     */
    public static boolean sendMessage(GenericGuildEvent event, TextChannel channel, String message) {
        try {
            if(hasSendPermission(event, channel)) {
                AtomicBoolean success = new AtomicBoolean(false);
                channel.sendMessage(message).queue(s -> success.set(true), f -> success.set(false));
                return success.get();
            }
            return false;
        } catch(Exception e) {
            log.error("An error occurred while running the {} class, message: {}", event.getClass().getSimpleName(), e.getMessage(), e);
            return false;
        }
    }

    /**
     * Sends an embedded message.
     *
     * @param event {@link MessageEvent}
     * @param embed {@link MessageEmbed}
     */
    public static boolean sendMessage(MessageEvent event, MessageEmbed embed) {
        try {
            if(hasEmbedSendPermission(event)) {
                AtomicBoolean success = new AtomicBoolean(false);
                event.getChannel().sendMessage(embed).queue(s -> success.set(true), f -> success.set(false));
                return success.get();
            }
            return false;
        } catch(Exception e) {
            log.error("An error occurred while running the {} class, message: {}", event.getClass().getSimpleName(), e.getMessage(), e);
            return false;
        }
    }

    /**
     * Sends an embedded message to a supplied channel.
     *
     * @param event {@link MessageEvent}
     * @param channel {@link TextChannel}
     * @param embed {@link MessageEmbed}
     */
    public static boolean sendMessage(MessageEvent event, TextChannel channel, MessageEmbed embed) {
        try {
            if(hasEmbedSendPermission(event, channel)) {
                AtomicBoolean success = new AtomicBoolean(false);
                channel.sendMessage(embed).queue(s -> success.set(true), f -> success.set(false));
                return success.get();
            }
            return false;
        } catch(Exception e) {
            log.error("An error occurred while running the {} class, message: {}", event.getClass().getSimpleName(), e.getMessage(), e);
            return false;
        }
    }

    /**
     * Sends an embedded message to a supplied channel.
     *
     * @param event {@link GenericGuildEvent}
     * @param channel {@link TextChannel}
     * @param embed {@link MessageEmbed}
     */
    public static boolean sendMessage(GenericGuildEvent event, TextChannel channel, MessageEmbed embed) {
        try {
            if(hasEmbedSendPermission(event, channel)) {
                AtomicBoolean success = new AtomicBoolean(false);
                channel.sendMessage(embed).queue(s -> success.set(true), f -> success.set(false));
                return success.get();
            }
            return false;
        } catch(Exception e) {
            log.error("An error occurred while running the {} class, message: {}", event.getClass().getSimpleName(), e.getMessage(), e);
            return false;
        }
    }

    /**
     * Sends an embedded reply to the given message.
     *
     * @param event {@link MessageEvent}
     * @param embed {@link MessageEmbed}
     */
    public static boolean reply(MessageEvent event, MessageEmbed embed) {
        try {
            if(hasEmbedSendPermission(event)) {
                AtomicBoolean success = new AtomicBoolean(false);
                event.getMessage().reply(embed).queue(s -> success.set(true), f -> success.set(sendMessage(event, embed)));
            }
            return false;
        } catch(Exception e) {
            log.error("An error occurred while running the {} class, message: {}", event.getClass().getSimpleName(), e.getMessage(), e);
            return false;
        }
    }

    /**
     * Sends an embedded reply to the given message.
     *
     * @param event {@link MessageEvent}
     * @param message {@link MessageEmbed}
     */
    public static boolean reply(MessageEvent event, String message) {
        try {
            if(hasEmbedSendPermission(event)) {
                AtomicBoolean success = new AtomicBoolean(false);
                event.getMessage().reply(message).queue(s -> success.set(true), f -> success.set(sendMessage(event, message)));
            }
            return false;
        } catch(Exception e) {
            log.error("An error occurred while running the {} class, message: {}", event.getClass().getSimpleName(), e.getMessage(), e);
            return false;
        }
    }

    /**
     * Sends a message to a supplied channel to be deleted after 15 seconds.
     *
     * @param event {@link GenericGuildEvent}
     * @param channel {@link TextChannel}
     * @param message {@link MessageEmbed}
     */
    public static boolean sendTempMessage(GenericGuildEvent event, TextChannel channel, String message) {
        try {
            if(hasSendPermission(event, channel)) {
                AtomicBoolean success = new AtomicBoolean(false);
                channel.sendMessage(message).queue(s -> ScheduleHandler.registerUniqueJob(new MessageDeleteJob(s)));
            }
            return false;
        } catch(Exception e) {
            log.error("An error occurred while running the {} class, message: {}", event.getClass().getSimpleName(), e.getMessage(), e);
            return false;
        }
    }

    /**
     * Sends an embedded message to a supplied channel to be deleted after 15 seconds.
     *
     * @param event {@link GenericGuildEvent}
     * @param channel {@link TextChannel}
     * @param embed {@link MessageEmbed}
     */
    public static boolean sendTempMessage(GenericGuildEvent event, TextChannel channel, MessageEmbed embed) {
        try {
            if(hasEmbedSendPermission(event, channel)) {
                AtomicBoolean success = new AtomicBoolean(false);
                channel.sendMessage(embed).queue(s -> ScheduleHandler.registerUniqueJob(new MessageDeleteJob(s)));
            }
            return false;
        } catch(Exception e) {
            log.error("An error occurred while running the {} class, message: {}", event.getClass().getSimpleName(), e.getMessage(), e);
            return false;
        }
    }

    /**
     * Sends an embedded message to a supplied channel to be deleted after 15 seconds.
     *
     * @param event {@link GenericGuildMessageEvent}
     * @param embed {@link MessageEmbed}
     */
    public static boolean sendTempMessage(GenericGuildMessageEvent event, MessageEmbed embed) {
        try {
            if(hasEmbedSendPermission(event)) {
                AtomicBoolean success = new AtomicBoolean(false);
                event.getChannel().sendMessage(embed).queue(s -> ScheduleHandler.registerUniqueJob(new MessageDeleteJob(s)));
            }
            return false;
        } catch(Exception e) {
            log.error("An error occurred while running the {} class, message: {}", event.getClass().getSimpleName(), e.getMessage(), e);
            return false;
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
