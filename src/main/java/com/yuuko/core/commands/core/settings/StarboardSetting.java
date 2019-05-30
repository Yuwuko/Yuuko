package com.yuuko.core.commands.core.settings;

import com.yuuko.core.MessageHandler;
import com.yuuko.core.database.function.GuildFunctions;
import com.yuuko.core.events.extensions.MessageEvent;
import com.yuuko.core.utilities.MessageUtilities;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageReaction;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionAddEvent;

import java.awt.*;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

public class StarboardSetting extends Setting {

    private static final List<String> fileTypes = Arrays.asList("mp4", "mov", "avi");

    public StarboardSetting(MessageEvent e) {
        onCommand(e);
    }

    protected void onCommand(MessageEvent e) {
        String[] parameters = e.getCommand().get(1).split("\\s+", 2);

        if(parameters[1].equalsIgnoreCase("setup")) {
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
            if(GuildFunctions.setGuildSettings("starboard", channel.getId(), e.getGuild().getId())) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Starboard").setDescription("The starboard channel has been set to **" + channel.getAsMention() + "**.");
                MessageHandler.sendMessage(e, embed.build());
            }
        } else {
            if(GuildFunctions.setGuildSettings("starboard", null, e.getGuild().getId())) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Starboard").setDescription("The starboard channel has been unset, thus deactivating the starboard.");
                MessageHandler.sendMessage(e, embed.build());
            }
        }
    }

    private void setup(MessageEvent e) {
        try {
            e.getGuild().getController().createTextChannel("starboard").queue(channel -> {
                TextChannel textChannel = (TextChannel)channel;
                channel.createPermissionOverride(e.getGuild().getSelfMember()).setAllow(Permission.MESSAGE_WRITE, Permission.MESSAGE_EMBED_LINKS).queue();
                if(GuildFunctions.setGuildSettings("starboard", channel.getId(), e.getGuild().getId())) {
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
     *
     * @param e GuildMessageReactionAddEvent
     */
    public static void execute(GuildMessageReactionAddEvent e) {
        String channelId = GuildFunctions.getGuildSetting("starboard", e.getGuild().getId());

        if(channelId != null) {
            TextChannel starboard = e.getGuild().getTextChannelById(channelId);
            Message starred = e.getChannel().getMessageById(e.getMessageId()).complete();

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
