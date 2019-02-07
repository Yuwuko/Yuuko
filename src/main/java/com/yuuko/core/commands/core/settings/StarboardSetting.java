package com.yuuko.core.commands.core.settings;

import com.yuuko.core.database.DatabaseFunctions;
import com.yuuko.core.utilities.MessageHandler;
import com.yuuko.core.utilities.MessageUtilities;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;

import java.awt.*;
import java.time.Instant;

public class StarboardSetting {

    public StarboardSetting(MessageReceivedEvent e, String value) {
        onCommand(e, value);
    }

    private void onCommand(MessageReceivedEvent e, String value) {
        if(value.equalsIgnoreCase("setup")) {
            if(e.getGuild().getSelfMember().hasPermission(Permission.MANAGE_CHANNEL, Permission.MANAGE_PERMISSIONS)) {
                setup(e);
            } else {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Missing Permission").setDescription("I require the **Manage Channel** and **Manage Permissions** permissions to setup the starboard automatically.");
                MessageHandler.sendMessage(e, embed.build());
            }
            return;
        }

        TextChannel channel = MessageUtilities.getFirstMentionedChannel(e);
        if(channel != null) {
            if(DatabaseFunctions.setGuildSettings("starboard", channel.getId(), e.getGuild().getId())) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Starboard").setDescription("The starboard channel has been set to **" + channel.getAsMention() + "**.");
                MessageHandler.sendMessage(e, embed.build());
            }
        } else {
            if(DatabaseFunctions.setGuildSettings("starboard", null, e.getGuild().getId())) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Starboard").setDescription("The starboard channel has been unset, thus deactivating the starboard.");
                MessageHandler.sendMessage(e, embed.build());
            }
        }
    }

    private void setup(MessageReceivedEvent e) {
        try {
            e.getGuild().getController().createTextChannel("starboard").queue(channel -> {
                TextChannel textChannel = (TextChannel)channel;
                channel.createPermissionOverride(e.getGuild().getSelfMember()).setAllow(Permission.MESSAGE_WRITE).queue();
                if(DatabaseFunctions.setGuildSettings("starboard", channel.getId(), e.getGuild().getId())) {
                    EmbedBuilder embed = new EmbedBuilder().setTitle("Starboard").setDescription("The " + textChannel.getAsMention() + " channel has been setup correctly.");
                    MessageHandler.sendMessage(e, embed.build());
                }
            });
        } catch(Exception ex) {
            //
        }
    }

    /**
     * Executes starboard setting.
     * @param e GuildBanEvent
     */
    public static void execute(MessageReactionAddEvent e) {
        String channelId = DatabaseFunctions.getGuildSetting("modLog", e.getGuild().getId());
        if(channelId != null) {
            TextChannel starboard = e.getGuild().getTextChannelById(channelId);
            Message starred = e.getTextChannel().getMessageById(e.getMessageId()).complete();

            EmbedBuilder starredEmbed = new EmbedBuilder()
                    .setColor(Color.ORANGE)
                    .setAuthor(starred.getMember().getEffectiveName(), null, starred.getAuthor().getEffectiveAvatarUrl())
                    .setDescription(starred.getContentDisplay())
                    .setImage(starred.getAttachments().size() > 0 ? starred.getAttachments().get(0).getUrl() : null)
                    .setTimestamp(Instant.now());
            starboard.sendMessage("⭐ - " + e.getTextChannel().getAsMention()).queue((message) -> starboard.sendMessage(starredEmbed.build()).queue());
        }
    }
}
