package com.yuuko.core.events.controllers;

import com.yuuko.core.commands.core.settings.StarboardSetting;
import com.yuuko.core.commands.utility.commands.ReactionRoleCommand;
import com.yuuko.core.metrics.MetricsManager;
import net.dv8tion.jda.core.events.message.guild.react.GenericGuildMessageReactionEvent;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionRemoveEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericGuildMessageReactionController {
    private static final Logger log = LoggerFactory.getLogger(GenericGuildMessageReactionController.class);

    public GenericGuildMessageReactionController(GenericGuildMessageReactionEvent e) {
        if(e instanceof GuildMessageReactionAddEvent || e instanceof GuildMessageReactionRemoveEvent) {
            guildMessageReactionEvent(e);
        }
    }

    private void guildMessageReactionEvent(GenericGuildMessageReactionEvent e) {
        try {
            if(e.getUser().isBot()) {
                MetricsManager.getEventMetrics().BOT_REACTS_PROCESSED.getAndIncrement();
                return;
            }

            MetricsManager.getEventMetrics().HUMAN_REACTS_PROCESSED.getAndIncrement();

            // Starboard
            if(e instanceof GuildMessageReactionAddEvent && e.getReactionEmote().getName().equals("⭐")) {
                StarboardSetting.execute((GuildMessageReactionAddEvent) e);
            }

            // Reaction Role
            ReactionRoleCommand.processReaction(e);

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
        }
    }

}
