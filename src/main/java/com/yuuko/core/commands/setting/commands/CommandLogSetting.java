package com.yuuko.core.commands.setting.commands;

import com.yuuko.core.Config;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.database.function.GuildFunctions;
import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.utilities.MessageUtilities;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Arrays;

public class CommandLogSetting extends Command {

    public CommandLogSetting() {
        super("comlog", Config.MODULES.get("setting"), 0, -1L, Arrays.asList("-comlog", "-comlog setup", "-comlog <#channel>", "-comlog unset"), false, Arrays.asList(Permission.MANAGE_SERVER));
    }

    public void onCommand(MessageEvent e) {
        if(!e.hasParameters()) {
            String channel = GuildFunctions.getGuildSetting("comlog", e.getGuild().getId());
            String status = (channel == null) ? "There is currently no command log set." : "The command log is currently set to use " + e.getGuild().getTextChannelById(channel).getAsMention();

            EmbedBuilder embed = new EmbedBuilder().setTitle("Command Log").setDescription(status)
                    .addField("Help", "Use `" + e.getPrefix() + "help " + e.getCommand().getName() + "` to get information on how to use this command.", true);
            MessageHandler.sendMessage(e, embed.build());
            return;
        }

        if(e.getParameters().equalsIgnoreCase("setup")) {
            if(e.getGuild().getSelfMember().hasPermission(Permission.MANAGE_CHANNEL, Permission.MANAGE_PERMISSIONS)) {
                e.getGuild().createTextChannel("com-log").queue(channel -> {
                    channel.createPermissionOverride(e.getGuild().getSelfMember()).setAllow(Permission.MESSAGE_WRITE).queue();
                    if(GuildFunctions.setGuildSettings("comlog", channel.getId(), e.getGuild().getId())) {
                        EmbedBuilder embed = new EmbedBuilder().setTitle("Command Log").setDescription("The " + channel.getAsMention() + " channel has been setup correctly.");
                        MessageHandler.sendMessage(e, embed.build());
                    }
                });
            } else {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Missing Permission").setDescription("I require the `MANAGE_CHANNEL` and `MANAGE_PERMISSIONS` permissions to setup the command log automatically.");
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
            return;
        }

        if(GuildFunctions.setGuildSettings("comlog", null, e.getGuild().getId())) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Command Log").setDescription("The command log has been unset, deactivating the log.");
            MessageHandler.sendMessage(e, embed.build());
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
                    .setFooter(Config.STANDARD_STRINGS.get(0), Config.BOT.getAvatarUrl())
                    .setTimestamp(Instant.now());
            MessageHandler.sendMessage(e, log, embed.build());
        }
    }
}
