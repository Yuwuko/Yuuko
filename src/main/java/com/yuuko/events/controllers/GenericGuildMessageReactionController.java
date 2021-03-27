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

    public GenericGuildMessageReactionController(GenericGuildMessageReactionEvent e) {
        if(e instanceof GuildMessageReactionAddEvent) {
            guildMessageReactionEvent(e);
            return;
        }

        if(e instanceof GuildMessageReactionRemoveEvent) {
            guildMessageReactionEvent(e);
        }
    }

    private void guildMessageReactionEvent(GenericGuildMessageReactionEvent e) {
        try {
            // Prevent null users or bots from triggering reaction events.
            if(e.getUser() == null || e.getUser().isBot()) {
                return;
            }

            // Starboard
            if(e instanceof GuildMessageReactionAddEvent) {
                if(e.getReactionEmote().getName().equals("⭐")) {
                    StarboardSetting.execute((GuildMessageReactionAddEvent) e);
                }
            }

            // Events
            EventCommand.processReaction(e);

            // Reaction Role
            ReactionRoleCommand.processReaction(e);
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
        }
    }

}
