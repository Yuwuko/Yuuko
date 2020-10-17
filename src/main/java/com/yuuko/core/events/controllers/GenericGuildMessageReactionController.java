package com.yuuko.core.events.controllers;

import com.yuuko.core.commands.setting.commands.StarboardSetting;
import com.yuuko.core.commands.utility.commands.ReactionRoleCommand;
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
            if(e.getUser() == null || e.getUser().isBot()) {
                return;
            }

            // Starboard
            if(e instanceof GuildMessageReactionAddEvent && e.getReactionEmote().getName().equals("⭐")) {
                StarboardSetting.execute((GuildMessageReactionAddEvent) e);
            }

            // Reaction Role
            ReactionRoleCommand.processReaction(e);

        } catch(IllegalStateException il) {
            //TODO: Figure out why custom emoji's can't be found.
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
        }
    }

}
