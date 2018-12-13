package com.yuuko.core.controllers;

import com.yuuko.core.Statistics;
import com.yuuko.core.database.DatabaseFunctions;
import com.yuuko.core.modules.utility.ModuleUtility;
import com.yuuko.core.utils.MessageHandler;
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
            Statistics.REACTS_PROCESSED.getAndIncrement();

            if(e.getUser().isBot()) {
                return;
            }

            if(e.getReaction().getReactionEmote().getName().equals("📌")) {
                if(new DatabaseFunctions().checkModuleSettings("moduleUtility", e.getGuild().getId())) {
                    if(e instanceof MessageReactionAddEvent) {
                        new ModuleUtility((MessageReactionAddEvent) e);
                    } else if(e instanceof MessageReactionRemoveEvent) {
                        new ModuleUtility((MessageReactionRemoveEvent) e);
                    }
                }
            }
        } catch(Exception ex) {
            MessageHandler.sendException(ex, "processGenericMessageReactionEvent");
        }
    }

}
