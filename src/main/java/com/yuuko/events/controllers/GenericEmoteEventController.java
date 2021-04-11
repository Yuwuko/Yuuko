package com.yuuko.events.controllers;

import com.yuuko.modules.utility.commands.ReactionRoleCommand;
import net.dv8tion.jda.api.events.emote.EmoteRemovedEvent;
import net.dv8tion.jda.api.events.emote.GenericEmoteEvent;
import net.dv8tion.jda.api.events.emote.update.EmoteUpdateNameEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericEmoteEventController {
    private static final Logger log = LoggerFactory.getLogger(GenericEmoteEventController.class);

    public GenericEmoteEventController(GenericEmoteEvent event) {
        if(event instanceof EmoteUpdateNameEvent) {
            emoteUpdateNameEvent((EmoteUpdateNameEvent) event);
            return;
        }

        if(event instanceof EmoteRemovedEvent) {
            emoteRemovedEvent((EmoteRemovedEvent) event);
        }
    }

    public void emoteUpdateNameEvent(EmoteUpdateNameEvent event) {
        final String oldEmote = event.getOldName() + ":" + event.getEmote().getId();
        final String newEmote = event.getNewName() + ":" + event.getEmote().getId();
        ReactionRoleCommand.DatabaseInterface.updateReactionRole(event.getGuild(), oldEmote, newEmote);
    }

    public void emoteRemovedEvent(EmoteRemovedEvent event) {
        ReactionRoleCommand.DatabaseInterface.removeReactionRole(event.getEmote());
    }
}
