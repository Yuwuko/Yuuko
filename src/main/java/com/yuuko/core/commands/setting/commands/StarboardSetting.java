package com.yuuko.core.commands.setting.commands;

import com.yuuko.core.Configuration;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.database.function.GuildFunctions;
import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.utilities.MessageUtilities;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

import java.awt.*;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

public class StarboardSetting extends Command {
    private static final List<String> fileTypes = Arrays.asList("mp4", "mov", "avi");

    public StarboardSetting() {
        super("starboard", Configuration.MODULES.get("setting"), 0, -1L, Arrays.asList("-starboard", "-starboard setup", "-starboard <#channel>", "-starboard unset"), false, Arrays.asList(Permission.MANAGE_SERVER, Permission.MANAGE_CHANNEL, Permission.MANAGE_PERMISSIONS));
    }

    public void onCommand(MessageEvent e) {
        if(!e.hasParameters()) {
            String channel = GuildFunctions.getGuildSetting("starboard", e.getGuild().getId());
            String status = (channel == null) ? "There is currently no starboard set." : "The starboard is currently set to use " + e.getGuild().getTextChannelById(channel).getAsMention();

            EmbedBuilder embed = new EmbedBuilder().setTitle("Starboard").setDescription(status)
                    .addField("Help", "Use `" + e.getPrefix() + "help " + e.getCommand().getName() + "` to get information on how to use this command.", true);
            MessageHandler.sendMessage(e, embed.build());
            return;
        }

        if(e.hasParameters() && e.getParameters().equalsIgnoreCase("setup")) {
            e.getGuild().createTextChannel("starboard").queue(channel -> {
                channel.createPermissionOverride(e.getGuild().getSelfMember()).setAllow(Permission.MESSAGE_WRITE, Permission.MESSAGE_EMBED_LINKS).queue();
                if(GuildFunctions.setGuildSettings("starboard", channel.getId(), e.getGuild().getId())) {
                    EmbedBuilder embed = new EmbedBuilder().setTitle("Starboard").setDescription("The " + channel.getAsMention() + " channel has been setup correctly.");
                    MessageHandler.sendMessage(e, embed.build());
                }
            });
            return;
        }

        TextChannel channel = MessageUtilities.getFirstMentionedChannel(e);
        if(channel != null) {
            if(GuildFunctions.setGuildSettings("starboard", channel.getId(), e.getGuild().getId())) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Starboard").setDescription("The starboard channel has been set to **" + channel.getAsMention() + "**.");
                MessageHandler.sendMessage(e, embed.build());
            }
            return;
        }

        if(GuildFunctions.setGuildSettings("starboard", null, e.getGuild().getId())) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Starboard").setDescription("The starboard channel has been unset, thus deactivating the starboard.");
            MessageHandler.sendMessage(e, embed.build());
        }
    }

    /**
     * Executes starboard setting.
     *
     * @param e GuildMessageReactionAddEvent
     */
    public static void execute(GuildMessageReactionAddEvent e) {
        String channelId = GuildFunctions.getGuildSetting("starboard", e.getGuild().getId());

        if(channelId != null) {
            TextChannel starboard = e.getGuild().getTextChannelById(channelId);
            Message starred = e.getChannel().retrieveMessageById(e.getMessageId()).complete();

            // If the channel doesn't exist, remove the database record of it existing.
            if(starboard == null) {
                GuildFunctions.setGuildSettings("starboard", null, e.getGuild().getId());
                return;
            }

            // Cannot starboard embedded messages.
            if(starred.getEmbeds().size() > 0) {
                return;
            }

            // Cannot starboard not image/video attachments.
            if(starred.getAttachments().size() > 0 && !starred.getAttachments().get(0).isImage()) {
                return;
            }

            List<Message> history = starboard.getIterableHistory().complete();
            for(Message message: history) {
                if(message.getContentRaw().contains(starred.getId())) {
                    for(MessageReaction reaction: starred.getReactions()) {
                        if(reaction.getReactionEmote().getName().equals("⭐")) {
                            String content = message.getContentRaw();
                            int emoteCount = Integer.parseInt(content.substring(1, content.indexOf("`", 1)));

                            if(emoteCount != reaction.getCount()) {
                                message.editMessage("`"+ reaction.getCount() +"`⭐ - " + e.getChannel().getAsMention() + " `<" + starred.getId() + ">`" ).queue();
                                return;
                            }
                        }
                    }
                    return;
                }
            }

            // Check video formats to post without embed.
            if(starred.getAttachments().size() > 0) {
                String attachment = starred.getAttachments().get(0).getProxyUrl();
                if(fileTypes.contains(attachment.substring(attachment.length()-3))) {
                    starboard.sendMessage("`1`⭐ - " + e.getChannel().getAsMention() + " `<" + e.getMessageId() + ">`").queue(message -> starboard.sendMessage(attachment).queue());
                    return;
                }
            }

            EmbedBuilder starredEmbed = new EmbedBuilder()
                    .setColor(Color.ORANGE)
                    .setAuthor(starred.getMember().getEffectiveName(), null, starred.getAuthor().getEffectiveAvatarUrl())
                    .setDescription(starred.getContentDisplay())
                    .setImage(starred.getAttachments().size() > 0 ? starred.getAttachments().get(0).getProxyUrl() : null)
                    .setTimestamp(Instant.now());
            starboard.sendMessage("`1`⭐ - " + e.getChannel().getAsMention() + " `<" + e.getMessageId() + ">`").queue(message -> starboard.sendMessage(starredEmbed.build()).queue());
        }
    }
}
