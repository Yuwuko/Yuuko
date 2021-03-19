package com.yuuko.events.controllers;

import com.yuuko.commands.utility.commands.ReactionRoleCommand;
import net.dv8tion.jda.api.events.emote.EmoteRemovedEvent;
import net.dv8tion.jda.api.events.emote.GenericEmoteEvent;
import net.dv8tion.jda.api.events.emote.update.EmoteUpdateNameEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericEmoteEventController {
    private static final Logger log = LoggerFactory.getLogger(GenericEmoteEventController.class);

    public GenericEmoteEventController(GenericEmoteEvent e) {
        if(e instanceof EmoteUpdateNameEvent) {
            emoteUpdateNameEvent((EmoteUpdateNameEvent) e);
            return;
        }

        if(e instanceof EmoteRemovedEvent) {
            emoteRemovedEvent((EmoteRemovedEvent) e);
        }
    }

    public void emoteUpdateNameEvent(EmoteUpdateNameEvent e) {
        final String oldEmote = e.getOldName() + ":" + e.getEmote().getId();
        final String newEmote = e.getNewName() + ":" + e.getEmote().getId();
        ReactionRoleCommand.DatabaseInterface.updateReactionRole(e.getGuild(), oldEmote, newEmote);
    }

    public void emoteRemovedEvent(EmoteRemovedEvent e) {
        ReactionRoleCommand.DatabaseInterface.removeReactionRole(e.getEmote());
    }
}
