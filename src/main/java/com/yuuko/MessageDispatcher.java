package com.yuuko;

import com.yuuko.events.entity.MessageEvent;
import com.yuuko.scheduler.ScheduleHandler;
import com.yuuko.scheduler.jobs.MessageDeleteJob;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.GenericGuildEvent;
import net.dv8tion.jda.api.events.message.guild.GenericGuildMessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public final class MessageDispatcher {
    private static final Logger log = LoggerFactory.getLogger(MessageDispatcher.class);
    private static final Permission[] sendMessagePermissions = {Permission.MESSAGE_READ, Permission.MESSAGE_WRITE};
    private static final Permission[] sendEmbedPermissions = {Permission.MESSAGE_READ, Permission.MESSAGE_WRITE, Permission.MESSAGE_EMBED_LINKS};

    /**
     * Sends a message, saving those precious bytes.
     * @param event {@link MessageEvent}
     * @param message {@link String}
     */
    public static void sendMessage(MessageEvent event, String message) {
        try {
            if(hasSendPermission(event)) {
                event.getChannel().sendMessage(message).queue();
            }
        } catch(Exception e) {
            log.error("An error occurred while running the {} class, message: {}", event.getClass().getSimpleName(), e.getMessage(), e);
        }
    }

    /**
     * Sends a file via message channel.
     * @param event {@link MessageEvent}
     * @param file {@link File}
     */
    public static void sendMessage(MessageEvent event, File file) {
        try {
            if(hasSendPermission(event)) {
                event.getChannel().sendFile(file).queue();
            }
        } catch(Exception e) {
            log.error("An error occurred while running the {} class, message: {}", event.getClass().getSimpleName(), e.getMessage(), e);
        }
    }

    /**
     * Sends a file via message channel.
     * @param event {@link MessageEvent}
     * @param bytes byte[]
     * @param fileName String
     */
    public static void sendMessage(MessageEvent event, byte[] bytes, String fileName) {
        try {
            if(hasSendPermission(event)) {
                event.getChannel().sendFile(bytes, fileName).queue();
            }
        } catch(Exception e) {
            log.error("An error occurred while running the {} class, message: {}", event.getClass().getSimpleName(), e.getMessage(), e);
        }
    }

    /**
     * Sends a message to the provided channel.
     * @param event {@link GenericGuildEvent}
     * @param channel {@link TextChannel}
     * @param message {@link String}
     */
    public static void sendMessage(GenericGuildEvent event, TextChannel channel, String message) {
        try {
            if(hasSendPermission(event, channel)) {
                channel.sendMessage(message).queue();
            }
        } catch(Exception e) {
            log.error("An error occurred while running the {} class, message: {}", event.getClass().getSimpleName(), e.getMessage(), e);
        }
    }

    /**
     * Sends an embedded message.
     * @param event {@link MessageEvent}
     * @param embed {@link MessageEmbed}
     */
    public static void sendMessage(MessageEvent event, MessageEmbed embed) {
        try {
            if(hasEmbedPermission(event)) {
                event.getChannel().sendMessage(embed).queue();
            }
        } catch(Exception e) {
            log.error("An error occurred while running the {} class, message: {}", event.getClass().getSimpleName(), e.getMessage(), e);
        }
    }

    /**
     * Sends an embedded message to a supplied channel.
     * @param event {@link MessageEvent}
     * @param channel {@link TextChannel}
     * @param embed {@link MessageEmbed}
     */
    public static void sendMessage(MessageEvent event, TextChannel channel, MessageEmbed embed) {
        try {
            if(hasEmbedPermission(event, channel)) {
                channel.sendMessage(embed).queue();
            }
        } catch(Exception e) {
            log.error("An error occurred while running the {} class, message: {}", event.getClass().getSimpleName(), e.getMessage(), e);
        }
    }

    /**
     * Sends an embedded message to a supplied channel.
     * @param event {@link GenericGuildEvent}
     * @param channel {@link TextChannel}
     * @param embed {@link MessageEmbed}
     */
    public static void sendMessage(GenericGuildEvent event, TextChannel channel, MessageEmbed embed) {
        try {
            if(hasEmbedPermission(event, channel)) {
                channel.sendMessage(embed).queue();
            }
        } catch(Exception e) {
            log.error("An error occurred while running the {} class, message: {}", event.getClass().getSimpleName(), e.getMessage(), e);
        }
    }

    /**
     * Sends an embedded reply to the given message.
     * @param event {@link MessageEvent}
     * @param embed {@link MessageEmbed}
     */
    public static void reply(MessageEvent event, MessageEmbed embed) {
        try {
            if(hasEmbedPermission(event)) {
                event.getMessage().reply(embed).queue(s -> {}, f -> sendMessage(event, embed));
            }
        } catch(Exception e) {
            log.error("An error occurred while running the {} class, message: {}", event.getClass().getSimpleName(), e.getMessage(), e);
        }
    }

    /**
     * Sends an embedded reply to the given message.
     * @param event {@link MessageEvent}
     * @param message {@link MessageEmbed}
     */
    public static void reply(MessageEvent event, String message) {
        try {
            if(hasEmbedPermission(event)) {
                event.getMessage().reply(message).queue(s -> {}, f -> sendMessage(event, message));
            }
        } catch(Exception e) {
            log.error("An error occurred while running the {} class, message: {}", event.getClass().getSimpleName(), e.getMessage(), e);
        }
    }

    /**
     * Sends a message to a supplied channel to be deleted after 15 seconds.
     * @param event {@link GenericGuildEvent}
     * @param channel {@link TextChannel}
     * @param message {@link MessageEmbed}
     */
    public static void sendTempMessage(GenericGuildEvent event, TextChannel channel, String message) {
        try {
            if(hasSendPermission(event, channel)) {
                channel.sendMessage(message).queue(s -> ScheduleHandler.registerUniqueJob(new MessageDeleteJob(s)));
            }
        } catch(Exception e) {
            log.error("An error occurred while running the {} class, message: {}", event.getClass().getSimpleName(), e.getMessage(), e);
        }
    }

    /**
     * Sends an embedded message to a supplied channel to be deleted after 15 seconds.
     * @param event {@link GenericGuildEvent}
     * @param channel {@link TextChannel}
     * @param embed {@link MessageEmbed}
     */
    public static void sendTempMessage(GenericGuildEvent event, TextChannel channel, MessageEmbed embed) {
        try {
            if(hasEmbedPermission(event, channel)) {
                channel.sendMessage(embed).queue(s -> ScheduleHandler.registerUniqueJob(new MessageDeleteJob(s)));
            }
        } catch(Exception e) {
            log.error("An error occurred while running the {} class, message: {}", event.getClass().getSimpleName(), e.getMessage(), e);
        }
    }

    /**
     * Sends an embedded message to a supplied channel to be deleted after 15 seconds.
     * @param event {@link GenericGuildMessageEvent}
     * @param embed {@link MessageEmbed}
     */
    public static void sendTempMessage(GenericGuildMessageEvent event, MessageEmbed embed) {
        try {
            if(hasEmbedPermission(event)) {
                event.getChannel().sendMessage(embed).queue(s -> ScheduleHandler.registerUniqueJob(new MessageDeleteJob(s)));
            }
        } catch(Exception e) {
            log.error("An error occurred while running the {} class, message: {}", event.getClass().getSimpleName(), e.getMessage(), e);
        }
    }

    /**
     * Checks to see if the bot has permission to write messages in the given server/channel. This prevents JDA throwing exceptions.
     * @param guild {@link Guild}
     * @param channel {@link TextChannel}
     * @return boolean
     */
    private static boolean hasSendPermission(Guild guild, TextChannel channel) {
        Member bot = guild.getSelfMember();
        return channel != null
                && ((bot.hasPermission(sendMessagePermissions) && bot.hasPermission(channel, sendMessagePermissions))
                || (!bot.hasPermission(sendMessagePermissions) && bot.hasPermission(channel, sendMessagePermissions)));
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
    private static boolean hasEmbedPermission(Guild guild, TextChannel channel) {
        Member bot = guild.getSelfMember();
        return channel != null
                && ((bot.hasPermission(sendEmbedPermissions) && bot.hasPermission(channel, sendEmbedPermissions))
                || (!bot.hasPermission(sendEmbedPermissions) && bot.hasPermission(channel, sendEmbedPermissions)));
    }

    /**
     * Auxiliary hasEmbedPermission flow controller
     */
    private static boolean hasEmbedPermission(GenericGuildMessageEvent e) {
        return hasEmbedPermission(e.getGuild(), e.getChannel());
    }

    /**
     * Auxiliary hasEmbedPermission flow controller
     */
    private static boolean hasEmbedPermission(GenericGuildMessageEvent e, TextChannel channel) {
        return hasEmbedPermission(e.getGuild(), channel);
    }

    /**
     * Auxiliary hasEmbedPermission flow controller
     */
    private static boolean hasEmbedPermission(GenericGuildEvent e, TextChannel channel) {
        return hasEmbedPermission(e.getGuild(), channel);
    }
}
