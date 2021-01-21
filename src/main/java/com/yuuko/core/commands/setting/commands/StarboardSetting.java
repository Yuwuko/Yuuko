package com.yuuko.core.commands.setting.commands;

import com.yuuko.core.MessageDispatcher;
import com.yuuko.core.Yuuko;
import com.yuuko.core.commands.Command;
import com.yuuko.core.database.function.GuildFunctions;
import com.yuuko.core.events.entity.MessageEvent;
import com.yuuko.core.utilities.MessageUtilities;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

import java.awt.*;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class StarboardSetting extends Command {

    public StarboardSetting() {
        super("starboard", Yuuko.MODULES.get("setting"), 0, -1L, Arrays.asList("-starboard", "-starboard setup", "-starboard <#channel>", "-starboard unset"), false, Arrays.asList(Permission.MANAGE_SERVER, Permission.MANAGE_CHANNEL, Permission.MANAGE_PERMISSIONS));
    }

    public void onCommand(MessageEvent e) throws Exception {
        if(!e.hasParameters()) {
            String channel = GuildFunctions.getGuildSetting("starboard", e.getGuild().getId());
            EmbedBuilder embed = new EmbedBuilder().setTitle("Starboard")
                    .setDescription((channel == null) ? "There is currently no starboard set." : "The starboard is currently set to use " + e.getGuild().getTextChannelById(channel).getAsMention())
                    .addField("Help", "Use `" + e.getPrefix() + "help " + e.getCommand().getName() + "` to get information on how to use this command.", true);
            MessageDispatcher.reply(e, embed.build());
            return;
        }

        if(e.hasParameters() && e.getParameters().equalsIgnoreCase("setup")) {
            e.getGuild().createTextChannel("starboard").queue(channel -> {
                channel.createPermissionOverride(e.getGuild().getSelfMember()).setAllow(Permission.MESSAGE_WRITE, Permission.MESSAGE_EMBED_LINKS).queue();
                if(GuildFunctions.setGuildSettings("starboard", channel.getId(), e.getGuild().getId())) {
                    EmbedBuilder embed = new EmbedBuilder().setTitle("Starboard").setDescription("The " + channel.getAsMention() + " channel has been setup correctly.");
                    MessageDispatcher.reply(e, embed.build());
                }
            });
            return;
        }

        TextChannel channel = MessageUtilities.getFirstMentionedChannel(e);
        if(channel != null) {
            if(GuildFunctions.setGuildSettings("starboard", channel.getId(), e.getGuild().getId())) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Starboard").setDescription("The starboard channel has been set to **" + channel.getAsMention() + "**.");
                MessageDispatcher.reply(e, embed.build());
            }
            return;
        }

        if(GuildFunctions.setGuildSettings("starboard", null, e.getGuild().getId())) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Starboard").setDescription("The starboard channel has been unset, thus deactivating the starboard.");
            MessageDispatcher.reply(e, embed.build());
        }
    }

    /**
     * Executes starboard setting.
     * @param e GuildMessageReactionAddEvent
     */
    public static void execute(GuildMessageReactionAddEvent e) {
        String channelId = GuildFunctions.getGuildSetting("starboard", e.getGuild().getId());
        if(channelId == null) {
            return;
        }

        // If the channel doesn't exist, remove the database record of it existing.
        TextChannel starboard = e.getGuild().getTextChannelById(channelId);
        if(starboard == null) {
            GuildFunctions.setGuildSettings("starboard", null, e.getGuild().getId());
            return;
        }

        // Update star-count and return if found, else create new entry.
        Message starred = e.retrieveMessage().complete();
        AtomicBoolean found = new AtomicBoolean(false);
        starboard.getIterableHistory().limit(25).complete().stream()
                .filter(message -> message.getContentStripped().contains(starred.getId()) || message.getId().equals(starred.getId()))
                .findFirst()
                .ifPresent(message -> message.getReactions().stream()
                        .filter(messageReaction -> messageReaction.getReactionEmote().getName().equals("⭐"))
                        .findFirst()
                        .ifPresent(messageReaction -> {
                            int emoteCount = Integer.parseInt(message.getContentRaw().substring(1, message.getContentRaw().indexOf("`", 1)));
                            if(emoteCount != messageReaction.getCount()) {
                                MessageBuilder messagebuilder = new MessageBuilder()
                                        .setContent("`"+ messageReaction.getCount() +"`⭐ - " + e.getChannel().getAsMention() + " `<" + starred.getId() + ">`" )
                                        .setEmbed((message.getEmbeds().size() != 0) ? message.getEmbeds().get(0) : null);
                                message.editMessage(messagebuilder.build()).queue();
                            }
                            found.set(true);
                        }));
        if(found.get()) {
            return;
        }

        // Create and send new starboard entry. (embed)
        if(starred.getEmbeds().size() != 0) {
            MessageBuilder messageBuilder = new MessageBuilder()
                    .setContent("`1`⭐ - " + e.getChannel().getAsMention() + " `" + e.getMessageId() + "`\n")
                    .setEmbed(starred.getEmbeds().get(0));
            starboard.sendMessage(messageBuilder.build()).queue(message -> message.addReaction("⭐").queue());
            return;
        }

        // Create and send new starboard entry. (non-embed)
        List<Message.Attachment> attachments = starred.getAttachments();
        MessageBuilder messageBuilder = new MessageBuilder()
                .setContent("`1`⭐ - " + e.getChannel().getAsMention() + " `" + e.getMessageId() + "`")
                .setEmbed(new EmbedBuilder()
                        .setColor(Color.ORANGE)
                        .setAuthor(starred.getAuthor().getAsTag(), null, starred.getAuthor().getEffectiveAvatarUrl())
                        .setDescription(starred.getContentDisplay() + ((attachments.size() != 0) ? "\n" + attachments.get(0).getProxyUrl() : ""))
                        .setImage(attachments.size() != 0 && attachments.get(0).isImage() ? attachments.get(0).getProxyUrl() : null)
                        .setTimestamp(Instant.now())
                        .build()
                );
        starboard.sendMessage(messageBuilder.build()).queue(message -> message.addReaction("⭐").queue());
    }

}
