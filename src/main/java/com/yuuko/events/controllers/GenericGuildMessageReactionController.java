package com.yuuko.events.controllers;

import com.yuuko.modules.setting.commands.StarboardSetting;
import com.yuuko.modules.utility.commands.EventCommand;
import com.yuuko.modules.utility.commands.ReactionRoleCommand;
import net.dv8tion.jda.api.events.message.guild.react.GenericGuildMessageReactionEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericGuildMessageReactionController {
    private static final Logger log = LoggerFactory.getLogger(GenericGuildMessageReactionController.class);

    public GenericGuildMessageReactionController(GenericGuildMessageReactionEvent event) {
        if(event instanceof GuildMessageReactionAddEvent) {
            guildMessageReactionEvent(event);
            return;
        }

        if(event instanceof GuildMessageReactionRemoveEvent) {
            guildMessageReactionEvent(event);
        }
    }

    private void guildMessageReactionEvent(GenericGuildMessageReactionEvent event) {
        try {
            // Prevent null users or bots from triggering reaction events.
            if(event.getUser() == null || event.getUser().isBot()) {
                return;
            }

            // Starboard
            if(event instanceof GuildMessageReactionAddEvent) {
                if(event.getReactionEmote().getName().equals("⭐")) {
                    StarboardSetting.execute((GuildMessageReactionAddEvent) event);
                }
            }

            // Events
            EventCommand.processReaction(event);
            // Reaction Role
            ReactionRoleCommand.processReaction(event);

        } catch(Exception e) {
            log.error("An error occurred while running the {} class, message: {}", this, e.getMessage(), e);
        }
    }

}
