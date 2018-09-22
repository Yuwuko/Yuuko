package com.basketbandit.core.controllers;

import com.basketbandit.core.database.DatabaseFunctions;
import com.basketbandit.core.modules.utility.ModuleUtility;
import net.dv8tion.jda.core.entities.MessageReaction;
import net.dv8tion.jda.core.events.message.react.GenericMessageReactionEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionRemoveEvent;

public class GenericMessageReactionController {

    public GenericMessageReactionController(GenericMessageReactionEvent e) {
        if(e instanceof MessageReactionAddEvent) {
            messageReactionAddEvent((MessageReactionAddEvent)e);
        } else if(e instanceof MessageReactionRemoveEvent) {
            messageReactionRemoveEvent((MessageReactionRemoveEvent)e);
        }
    }

    private void messageReactionAddEvent(MessageReactionAddEvent e) {
        MessageReaction react = e.getReaction();
        String serverLong = e.getGuild().getId();

        if(react.getReactionEmote().getName().equals("📌")) {
            if(new DatabaseFunctions().checkModuleSettings("moduleUtility", serverLong)) {
                new ModuleUtility(e);
            }
        }
    }

    private void messageReactionRemoveEvent(MessageReactionRemoveEvent e) {
        MessageReaction react = e.getReaction();
        String serverLong = e.getGuild().getId();

        if(react.getReactionEmote().getName().equals("📌")) {
            if(new DatabaseFunctions().checkModuleSettings("moduleUtility", serverLong)) {
                new ModuleUtility(e);
            }
        }
    }

}
