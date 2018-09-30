package com.basketbandit.core.controllers;

import com.basketbandit.core.database.DatabaseFunctions;
import com.basketbandit.core.modules.utility.ModuleUtility;
import com.basketbandit.core.utils.Utils;
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
        // Figure out if the user is a bot or not, so we don't waste any time.
        if(e.getUser().isBot()) {
            return;
        }

        if(e.getReaction().getReactionEmote().getName().equals("📌")) {
            if(new DatabaseFunctions().checkModuleSettings("moduleUtility", e.getGuild().getId())) {
                if(e instanceof MessageReactionAddEvent) {
                    new ModuleUtility((MessageReactionAddEvent)e);
                } else if(e instanceof MessageReactionRemoveEvent) {
                    new ModuleUtility((MessageReactionRemoveEvent)e);
                }
            }
        }

        Utils.incrementEvent(1);
    }

}
