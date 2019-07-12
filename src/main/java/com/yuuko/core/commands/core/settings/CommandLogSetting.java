package com.yuuko.core.commands.core.settings;

import com.yuuko.core.Configuration;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.database.function.GuildFunctions;
import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.utilities.MessageUtilities;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;

public class CommandLogSetting extends Setting {

    public CommandLogSetting(MessageEvent e) {
        onCommand(e);
    }

    protected void onCommand(MessageEvent e) {
        String[] parameters = e.getParameters().split("\\s+", 2);

        if(parameters[1].equalsIgnoreCase("setup")) {
            if(e.getGuild().getSelfMember().hasPermission(Permission.MANAGE_CHANNEL, Permission.MANAGE_PERMISSIONS)) {
                setup(e);
            } else {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Missing Permission").setDescription("I require the **Manage Channel** and **Manage Permissions** permissions to setup the command log automatically.");
                MessageHandler.sendMessage(e, embed.build());
            }
            return;
        }

        TextChannel channel = MessageUtilities.getFirstMentionedChannel(e);
        if(channel != null) {
            if(GuildFunctions.setGuildSettings("comlog", channel.getId(), e.getGuild().getId())) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Command Log").setDescription("The command log has been set to " + channel.getAsMention() + ".");
                MessageHandler.sendMessage(e, embed.build());
            }
        } else {
            if(GuildFunctions.setGuildSettings("comlog", null, e.getGuild().getId())) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Command Log").setDescription("The command log has been unset, deactivating the log.");
                MessageHandler.sendMessage(e, embed.build());
            }
        }
    }

    /**
     * Sets up the com-log channel.
     *
     * @param e MessageEvent
     */
    private void setup(MessageEvent e) {
        try {
            e.getGuild().createTextChannel("com-log").queue(channel -> {
                channel.createPermissionOverride(e.getGuild().getSelfMember()).setAllow(Permission.MESSAGE_WRITE).queue();
                if(GuildFunctions.setGuildSettings("comlog", channel.getId(), e.getGuild().getId())) {
                    EmbedBuilder embed = new EmbedBuilder().setTitle("Command Log").setDescription("The " + channel.getAsMention() + " channel has been setup correctly.");
                    MessageHandler.sendMessage(e, embed.build());
                }
            });
        } catch(Exception ex) {
            //
        }
    }

    /**
     * Executes the command logging function if it is enabled.
     *
     * @param e MessageEvent
     * @param executionTimeMs long
     */
    public static void execute(MessageEvent e, double executionTimeMs) {
        String channelId = GuildFunctions.getGuildSetting("comlog", e.getGuild().getId());
        if(channelId != null) {
            TextChannel log = e.getGuild().getTextChannelById(channelId);
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("Command")
                    .setThumbnail(e.getAuthor().getAvatarUrl())
                    .addField("User", e.getAuthor().getName() + "#" + e.getAuthor().getDiscriminator(), true)
                    .addField("Command", e.getMessage().getContentDisplay(), true)
                    .addField("Channel", e.getMessage().getTextChannel().getAsMention(), true)
                    .addField("Execution Time", new BigDecimal(executionTimeMs).setScale(2, RoundingMode.HALF_UP) + "ms", true)
                    .setFooter(Configuration.STANDARD_STRINGS.get(0), Configuration.BOT.getAvatarUrl())
                    .setTimestamp(Instant.now());
            MessageHandler.sendMessage(e, log, embed.build());
        }
    }
}
