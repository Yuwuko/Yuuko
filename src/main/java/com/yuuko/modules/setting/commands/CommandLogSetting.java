package com.yuuko.modules.setting.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.Yuuko;
import com.yuuko.database.function.GuildFunctions;
import com.yuuko.events.entity.MessageEvent;
import com.yuuko.modules.Command;
import com.yuuko.utilities.MessageUtilities;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Arrays;

public class CommandLogSetting extends Command {

    public CommandLogSetting() {
        super("commandlog", 0, -1L, Arrays.asList("-commandlog", "-commandlog setup", "-commandlog <#channel>", "-commandlog unset"), false, Arrays.asList(Permission.MANAGE_SERVER));
    }

    public void onCommand(MessageEvent context) throws Exception {
        if(!context.hasParameters()) {
            String channel = GuildFunctions.getGuildSetting("commandlog", context.getGuild().getId());
            EmbedBuilder embed = new EmbedBuilder().setTitle("Command Log")
                    .setDescription((channel == null) ? "There is currently no command log set." : "The command log is currently set to use " + context.getGuild().getTextChannelById(channel).getAsMention())
                    .addField("Help", "Use `" + context.getPrefix() + "help " + context.getCommand().getName() + "` to get information on how to use this command.", true);
            MessageDispatcher.reply(context, embed.build());
            return;
        }

        if(context.getParameters().equalsIgnoreCase("setup")) {
            context.getGuild().createTextChannel("command-log").queue(channel -> {
                channel.createPermissionOverride(context.getGuild().getSelfMember()).setAllow(Permission.MESSAGE_WRITE, Permission.MESSAGE_EMBED_LINKS).queue();
                if(GuildFunctions.setGuildSettings("commandlog", channel.getId(), context.getGuild().getId())) {
                    EmbedBuilder embed = new EmbedBuilder().setTitle("Command Log").setDescription("The " + channel.getAsMention() + " channel has been setup correctly.");
                    MessageDispatcher.reply(context, embed.build());
                }
            });
            return;
        }

        TextChannel channel = MessageUtilities.getFirstMentionedChannel(context);
        if(channel != null) {
            if(GuildFunctions.setGuildSettings("commandlog", channel.getId(), context.getGuild().getId())) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Command Log").setDescription("The command log has been set to " + channel.getAsMention() + ".");
                MessageDispatcher.reply(context, embed.build());
            }
            return;
        }

        if(GuildFunctions.setGuildSettings("commandlog", null, context.getGuild().getId())) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Command Log").setDescription("The command log has been unset, deactivating the log.");
            MessageDispatcher.reply(context, embed.build());
        }
    }

    /**
     * Executes the command logging function if it is enabled.
     *
     * @param e MessageEvent
     * @param executionTimeMs long
     */
    public static void execute(MessageEvent e, double executionTimeMs) {
        String channelId = GuildFunctions.getGuildSetting("commandlog", e.getGuild().getId());
        if(channelId != null) {
            TextChannel log = e.getGuild().getTextChannelById(channelId);
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("Command")
                    .setThumbnail(e.getAuthor().getAvatarUrl())
                    .addField("User", e.getAuthor().getName() + "#" + e.getAuthor().getDiscriminator(), true)
                    .addField("Command", e.getMessage().getContentDisplay(), true)
                    .addField("Channel", e.getMessage().getTextChannel().getAsMention(), true)
                    .addField("Execution Time", new BigDecimal(executionTimeMs).setScale(2, RoundingMode.HALF_UP) + "ms", true)
                    .setFooter(Yuuko.STANDARD_STRINGS.get(0), Yuuko.BOT.getAvatarUrl())
                    .setTimestamp(Instant.now());
            MessageDispatcher.sendMessage(e, log, embed.build());
        }
    }
}
