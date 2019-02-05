package com.yuuko.core.commands.core.settings;

import com.yuuko.core.database.DatabaseFunctions;
import com.yuuko.core.utilities.MessageHandler;
import com.yuuko.core.utilities.MessageUtility;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.time.Instant;

public class CommandLogSetting {

    public CommandLogSetting(MessageReceivedEvent e, String value) {
        executeCommand(e, value);
    }

    private void executeCommand(MessageReceivedEvent e, String value) {
        if(value.equalsIgnoreCase("setup")) {
            if(e.getGuild().getSelfMember().hasPermission(Permission.MANAGE_CHANNEL, Permission.MANAGE_PERMISSIONS)) {
                setupCommandLog(e);
            } else {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Missing Permission").setDescription("I require the **Manage Channel** and **Manage Permissions** permissions to setup the command log automatically.");
                MessageHandler.sendMessage(e, embed.build());
            }
            return;
        }

        TextChannel channel = MessageUtility.getFirstMentionedChannel(e);
        if(channel != null) {
            if(DatabaseFunctions.setGuildSettings("commandLog", channel.getId(), e.getGuild().getId())) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Command Log").setDescription("The command log has been set to " + channel.getAsMention() + ".");
                MessageHandler.sendMessage(e, embed.build());
            }
        } else {
            if(DatabaseFunctions.setGuildSettings("commandLog", null, e.getGuild().getId())) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Command Log").setDescription("The command log has been unset, deactivating the log.");
                MessageHandler.sendMessage(e, embed.build());
            }
        }
    }

    private void setupCommandLog(MessageReceivedEvent e) {
        try {
            e.getGuild().getController().createTextChannel("command-log").queue(channel -> {
                TextChannel textChannel = (TextChannel)channel;
                channel.createPermissionOverride(e.getGuild().getSelfMember()).setAllow(Permission.MESSAGE_WRITE).queue();
                if(DatabaseFunctions.setGuildSettings("commandLog", channel.getId(), e.getGuild().getId())) {
                    EmbedBuilder embed = new EmbedBuilder().setTitle("Command Log").setDescription("The " + textChannel.getAsMention() + " channel has been setup correctly.");
                    MessageHandler.sendMessage(e, embed.build());
                }
            });
        } catch(Exception ex) {
            //
        }
    }

    /**
     * Executes the logging feature of the bot.
     * @param e MessageReceivedEvent
     * @param executionTimeMs long
     */
    public static void executeLogging(MessageReceivedEvent e, long executionTimeMs) {
        TextChannel log = e.getGuild().getTextChannelById(DatabaseFunctions.getGuildSetting("commandLog", e.getGuild().getId()));
        if(log != null) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("Command")
                    .setThumbnail(e.getAuthor().getAvatarUrl())
                    .addField("User", e.getAuthor().getName() + "#" + e.getAuthor().getDiscriminator(), true)
                    .addField("Command", e.getMessage().getContentDisplay(), true)
                    .addField("Channel", e.getMessage().getTextChannel().getAsMention(), true)
                    .addField("Execution Time", executionTimeMs + "ms", true)
                    .setTimestamp(Instant.now());
            MessageHandler.sendMessage(log, embed.build());
        }
    }
}
