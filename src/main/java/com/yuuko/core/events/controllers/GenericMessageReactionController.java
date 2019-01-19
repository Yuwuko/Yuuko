package com.yuuko.core.events.controllers;

import com.yuuko.core.commands.utility.UtilityModule;
import com.yuuko.core.database.DatabaseFunctions;
import com.yuuko.core.metrics.handlers.MetricsManager;
import com.yuuko.core.utilities.MessageHandler;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.react.GenericMessageReactionEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionRemoveEvent;

import java.awt.*;
import java.time.format.DateTimeFormatter;

public class GenericMessageReactionController {

    public GenericMessageReactionController(GenericMessageReactionEvent e) {
        if(e instanceof MessageReactionAddEvent || e instanceof MessageReactionRemoveEvent) {
            processGenericMessageReactionEvent(e);
        }
    }

    private void processGenericMessageReactionEvent(GenericMessageReactionEvent e) {
        try {
            // Increment react counter, regardless of it's author.
            MetricsManager.getEventMetrics().REACTS_PROCESSED.getAndIncrement();

            if(e.getUser().isBot()) {
                return;
            }

            // Message Pin
            if(e.getReaction().getReactionEmote().getName().equals("📌")) {
                if(DatabaseFunctions.checkModuleSettings("moduleUtility", e.getGuild().getId())) {
                    if(e instanceof MessageReactionAddEvent) {
                        new UtilityModule((MessageReactionAddEvent) e);
                    } else if(e instanceof MessageReactionRemoveEvent) {
                        new UtilityModule((MessageReactionRemoveEvent) e);
                    }
                }
            }

            // Starboard
            if(e instanceof MessageReactionAddEvent && e.getReactionEmote().getName().equals("⭐")) {
                if(DatabaseFunctions.getGuildSetting("starboard", e.getGuild().getId()) != null) {
                    TextChannel starboard = e.getGuild().getTextChannelById(DatabaseFunctions.getGuildSetting("starboard", e.getGuild().getId()));
                    if(starboard != null && !e.getTextChannel().getId().equals(starboard.getId())) {
                        Message starred = e.getTextChannel().getMessageById(e.getMessageId()).complete();

                        EmbedBuilder starredEmbed = new EmbedBuilder()
                                .setColor(Color.ORANGE)
                                .setAuthor(starred.getMember().getEffectiveName(), null, starred.getAuthor().getEffectiveAvatarUrl())
                                .setDescription(starred.getContentDisplay())
                                .setImage(starred.getAttachments().size() > 0 ? starred.getAttachments().get(0).getUrl() : null)
                                .setFooter(starred.getCreationTime().format(DateTimeFormatter.ofPattern("d MMM yyyy  hh:mma")), null);
                        starboard.sendMessage("⭐ - " + e.getTextChannel().getAsMention()).queue((message) -> starboard.sendMessage(starredEmbed.build()).queue());
                    }
                }
            }

        } catch(Exception ex) {
            MessageHandler.sendException(ex, "processGenericMessageReactionEvent");
        }
    }

}
