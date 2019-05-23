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
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;

import java.awt.*;
import java.time.Instant;
import java.util.List;

public class StarboardSetting extends Setting {

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

    private void setup(MessageReceivedEvent e) {
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
     * @param e MessageReactionAddEvent
     */
    public static void execute(MessageReactionAddEvent e) {
        String channelId = GuildFunctions.getGuildSetting("starboard", e.getGuild().getId());

        if(channelId != null) {
            TextChannel starboard = e.getGuild().getTextChannelById(channelId);
            Message starred = e.getTextChannel().getMessageById(e.getMessageId()).complete();

            List<Message> history = starboard.getIterableHistory().complete();
            for(Message message: history) {
                if(message.getContentRaw().contains(starred.getId())) {
                    for(MessageReaction reaction: starred.getReactions()) {
                        if(reaction.getReactionEmote().getName().equals("⭐")) {
                            String content = message.getContentRaw();
                            int emoteCount = Integer.parseInt(content.substring(content.indexOf("`")+1, content.lastIndexOf("`")));

                            if(emoteCount != reaction.getCount()) {
                                message.editMessage("`"+ reaction.getCount() +"`⭐ - " + e.getTextChannel().getAsMention() + " <" + starred.getId() + ">" ).queue();
                                return;
                            }
                        }
                    }
                    return;
                }
            }

            EmbedBuilder starredEmbed = new EmbedBuilder()
                    .setColor(Color.ORANGE)
                    .setAuthor(starred.getMember().getEffectiveName(), null, starred.getAuthor().getEffectiveAvatarUrl())
                    .setDescription(starred.getContentDisplay())
                    .setImage(starred.getAttachments().size() > 0 ? starred.getAttachments().get(0).getUrl() : null)
                    .setTimestamp(Instant.now());
            starboard.sendMessage("`1`⭐ - " + e.getTextChannel().getAsMention() + " <" + e.getMessageId() + ">").queue((message) -> starboard.sendMessage(starredEmbed.build()).queue());
        }
    }
}
