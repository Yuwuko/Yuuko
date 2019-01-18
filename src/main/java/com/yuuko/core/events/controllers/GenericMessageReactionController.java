package com.yuuko.core.events.controllers;

import com.yuuko.core.commands.utility.UtilityModule;
import com.yuuko.core.database.DatabaseFunctions;
import com.yuuko.core.metrics.handlers.MetricsManager;
import com.yuuko.core.utilities.MessageHandler;
import net.dv8tion.jda.core.events.message.react.GenericMessageReactionEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionRemoveEvent;

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

            if(e.getReaction().getReactionEmote().getName().equals("📌")) {
                if(DatabaseFunctions.checkModuleSettings("moduleUtility", e.getGuild().getId())) {
                    if(e instanceof MessageReactionAddEvent) {
                        new UtilityModule((MessageReactionAddEvent) e);
                    } else if(e instanceof MessageReactionRemoveEvent) {
                        new UtilityModule((MessageReactionRemoveEvent) e);
                    }
                }
            }
        } catch(Exception ex) {
            MessageHandler.sendException(ex, "processGenericMessageReactionEvent");
        }
    }

}
