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
import java.util.Collection;

public final class MessageDispatcher {
    private static final Logger log = LoggerFactory.getLogger(MessageDispatcher.class);
    private static final Permission[] sendMessagePermissions = {Permission.MESSAGE_READ, Permission.MESSAGE_WRITE};
    private static final Permission[] sendEmbedPermissions = {Permission.MESSAGE_READ, Permission.MESSAGE_WRITE, Permission.MESSAGE_EMBED_LINKS};

    /**
     * Sends a message, saving those precious bytes.
     * @param context {@link MessageEvent}
     * @param message {@link String}
     */
    public static void sendMessage(MessageEvent context, String message) {
        try {
            if(hasSendPermission(context)) {
                context.getChannel().sendMessage(message).queue();
            }
        } catch(Exception e) {
            log.error("An error occurred while running the {} class, message: {}", context.getClass().getSimpleName(), e.getMessage(), e);
        }
    }

    /**
     * Sends a file via message channel.
     * @param context {@link MessageEvent}
     * @param file {@link File}
     */
    public static void sendMessage(MessageEvent context, File file) {
        try {
            if(hasSendPermission(context)) {
                context.getChannel().sendFile(file).queue();
            }
        } catch(Exception e) {
            log.error("An error occurred while running the {} class, message: {}", context.getClass().getSimpleName(), e.getMessage(), e);
        }
    }

    /**
     * Sends a file via message channel.
     * @param context {@link MessageEvent}
     * @param bytes byte[]
     * @param fileName String
     */
    public static void sendMessage(MessageEvent context, byte[] bytes, String fileName) {
        try {
            if(hasSendPermission(context)) {
                context.getChannel().sendFile(bytes, fileName).queue();
            }
        } catch(Exception e) {
            log.error("An error occurred while running the {} class, message: {}", context.getClass().getSimpleName(), e.getMessage(), e);
        }
    }

    /**
     * Sends a message to the provided channel.
     * @param context {@link GenericGuildEvent}
     * @param channel {@link TextChannel}
     * @param message {@link String}
     */
    public static void sendMessage(GenericGuildEvent context, TextChannel channel, String message) {
        try {
            if(hasSendPermission(context, channel)) {
                channel.sendMessage(message).queue();
            }
        } catch(Exception e) {
            log.error("An error occurred while running the {} class, message: {}", context.getClass().getSimpleName(), e.getMessage(), e);
        }
    }

    /**
     * Sends an embedded message.
     * @param context {@link MessageEvent}
     * @param embed {@link MessageEmbed}
     */
    public static void sendMessage(MessageEvent context, MessageEmbed embed) {
        try {
            if(hasEmbedPermission(context)) {
                context.getChannel().sendMessageEmbeds(embed).queue();
            }
        } catch(Exception e) {
            log.error("An error occurred while running the {} class, message: {}", context.getClass().getSimpleName(), e.getMessage(), e);
        }
    }

    /**
     * Sends an embedded message. (using a collection of embeds)
     * @param context {@link MessageEvent}
     * @param embeds {@link MessageEmbed}
     */
    public static void sendMessage(MessageEvent context, Collection<MessageEmbed> embeds) {
        try {
            if(hasEmbedPermission(context)) {
                context.getChannel().sendMessageEmbeds(embeds).queue();
            }
        } catch(Exception e) {
            log.error("An error occurred while running the {} class, message: {}", context.getClass().getSimpleName(), e.getMessage(), e);
        }
    }

    /**
     * Sends an embedded message to a supplied channel.
     * @param context {@link MessageEvent}
     * @param channel {@link TextChannel}
     * @param embed {@link MessageEmbed}
     */
    public static void sendMessage(MessageEvent context, TextChannel channel, MessageEmbed embed) {
        try {
            if(hasEmbedPermission(context, channel)) {
                channel.sendMessageEmbeds(embed).queue();
            }
        } catch(Exception e) {
            log.error("An error occurred while running the {} class, message: {}", context.getClass().getSimpleName(), e.getMessage(), e);
        }
    }

    /**
     * Sends an embedded message to a supplied channel.
     * @param context {@link GenericGuildEvent}
     * @param channel {@link TextChannel}
     * @param embed {@link MessageEmbed}
     */
    public static void sendMessage(GenericGuildEvent context, TextChannel channel, MessageEmbed embed) {
        try {
            if(hasEmbedPermission(context, channel)) {
                channel.sendMessageEmbeds(embed).queue();
            }
        } catch(Exception e) {
            log.error("An error occurred while running the {} class, message: {}", context.getClass().getSimpleName(), e.getMessage(), e);
        }
    }

    /**
     * Sends an embedded reply to the given message.
     * @param context {@link MessageEvent}
     * @param embed {@link MessageEmbed}
     */
    public static void reply(MessageEvent context, MessageEmbed embed) {
        try {
            if(hasEmbedPermission(context)) {
                context.getMessage().replyEmbeds(embed).queue(s -> {}, f -> sendMessage(context, embed));
            }
        } catch(Exception e) {
            log.error("An error occurred while running the {} class, message: {}", context.getClass().getSimpleName(), e.getMessage(), e);
        }
    }

    /**
     * Sends an embedded reply to the given message. (using a collection of embeds)
     * @param context {@link MessageEvent}
     * @param embeds {@link MessageEmbed}
     */
    public static void reply(MessageEvent context, Collection<MessageEmbed> embeds) {
        try {
            if(hasEmbedPermission(context)) {
                context.getMessage().replyEmbeds(embeds).queue(s -> {}, f -> sendMessage(context, embeds));
            }
        } catch(Exception e) {
            log.error("An error occurred while running the {} class, message: {}", context.getClass().getSimpleName(), e.getMessage(), e);
        }
    }

    /**
     * Sends an embedded reply to the given message.
     * @param context {@link MessageEvent}
     * @param message {@link MessageEmbed}
     */
    public static void reply(MessageEvent context, String message) {
        try {
            if(hasEmbedPermission(context)) {
                context.getMessage().reply(message).queue(s -> {}, f -> sendMessage(context, message));
            }
        } catch(Exception e) {
            log.error("An error occurred while running the {} class, message: {}", context.getClass().getSimpleName(), e.getMessage(), e);
        }
    }

    /**
     * Sends a message to a supplied channel to be deleted after 15 seconds.
     * @param context {@link GenericGuildEvent}
     * @param channel {@link TextChannel}
     * @param message {@link MessageEmbed}
     */
    public static void sendTempMessage(GenericGuildEvent context, TextChannel channel, String message) {
        try {
            if(hasSendPermission(context, channel)) {
                channel.sendMessage(message).queue(s -> ScheduleHandler.registerUniqueJob(new MessageDeleteJob(s)));
            }
        } catch(Exception e) {
            log.error("An error occurred while running the {} class, message: {}", context.getClass().getSimpleName(), e.getMessage(), e);
        }
    }

    /**
     * Sends an embedded message to a supplied channel to be deleted after 15 seconds.
     * @param context {@link GenericGuildEvent}
     * @param channel {@link TextChannel}
     * @param embed {@link MessageEmbed}
     */
    public static void sendTempMessage(GenericGuildEvent context, TextChannel channel, MessageEmbed embed) {
        try {
            if(hasEmbedPermission(context, channel)) {
                channel.sendMessageEmbeds(embed).queue(s -> ScheduleHandler.registerUniqueJob(new MessageDeleteJob(s)));
            }
        } catch(Exception e) {
            log.error("An error occurred while running the {} class, message: {}", context.getClass().getSimpleName(), e.getMessage(), e);
        }
    }

    /**
     * Sends an embedded message to a supplied channel to be deleted after 15 seconds.
     * @param context {@link GenericGuildMessageEvent}
     * @param embed {@link MessageEmbed}
     */
    public static void sendTempMessage(GenericGuildMessageEvent context, MessageEmbed embed) {
        try {
            if(hasEmbedPermission(context)) {
                context.getChannel().sendMessageEmbeds(embed).queue(s -> ScheduleHandler.registerUniqueJob(new MessageDeleteJob(s)));
            }
        } catch(Exception e) {
            log.error("An error occurred while running the {} class, message: {}", context.getClass().getSimpleName(), e.getMessage(), e);
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
    private static boolean hasSendPermission(GenericGuildMessageEvent event) {
        return hasSendPermission(event.getGuild(), event.getChannel());
    }

    /**
     * Main hasSendPermission flow controller
     */
    private static boolean hasSendPermission(GenericGuildMessageEvent event, TextChannel channel) {
        return hasSendPermission(event.getGuild(), channel);
    }

    /**
     * Main hasSendPermission flow controller
     */
    private static boolean hasSendPermission(GenericGuildEvent event, TextChannel channel) {
        return hasSendPermission(event.getGuild(), channel);
    }

    /**
     * Checks to see if the bot has extended permission to write embedded messages with links in the given server/channel.
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
    private static boolean hasEmbedPermission(GenericGuildMessageEvent event) {
        return hasEmbedPermission(event.getGuild(), event.getChannel());
    }

    /**
     * Auxiliary hasEmbedPermission flow controller
     */
    private static boolean hasEmbedPermission(GenericGuildMessageEvent event, TextChannel channel) {
        return hasEmbedPermission(event.getGuild(), channel);
    }

    /**
     * Auxiliary hasEmbedPermission flow controller
     */
    private static boolean hasEmbedPermission(GenericGuildEvent event, TextChannel channel) {
        return hasEmbedPermission(event.getGuild(), channel);
    }
}
