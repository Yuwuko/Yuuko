package com.yuuko.core.events;

import com.yuuko.core.events.controllers.*;
import net.dv8tion.jda.core.events.channel.text.GenericTextChannelEvent;
import net.dv8tion.jda.core.events.guild.GenericGuildEvent;
import net.dv8tion.jda.core.events.guild.voice.GenericGuildVoiceEvent;
import net.dv8tion.jda.core.events.message.guild.GenericGuildMessageEvent;
import net.dv8tion.jda.core.events.message.react.GenericMessageReactionEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericEventManager extends ListenerAdapter {
    private static final Logger log = LoggerFactory.getLogger(GenericEventManager.class);

    /**
     * Captures and deals with generic guild events.
     *
     * @param e GenericGuildEvent
     */
    @Override
    public void onGenericGuild(GenericGuildEvent e) {
        try {
            new GenericGuildController(e);
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
        }
    }

    /**
     * Captures and deals with generic message events.
     *
     * @param e -> GenericMessageEvent.
     */
    @Override
    public void onGenericGuildMessage(GenericGuildMessageEvent e) {
        try {
            new GenericGuildMessageController(e);
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
        }
    }

    /**
     * Captures and deals generic reaction events.
     *
     * @param e -> GenericMessageReactionEvent.
     */
    @Override
    public void onGenericMessageReaction(GenericMessageReactionEvent e) {
        try {
            new GenericMessageReactionController(e);
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
        }
    }

    /**
     * Captures and deals with generic voice events.
     *
     * @param e -> GenericGuildVoiceEvent.
     */
    @Override
    public void onGenericGuildVoice(GenericGuildVoiceEvent e) {
        try {
            new GenericGuildVoiceController(e);
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
        }
    }

    /**
     * Captures and deals with generic text-channel events.
     *
     * @param e -> GenericTextChannelEvent.
     */
    @Override
    public void onGenericTextChannel(GenericTextChannelEvent e) {
        try {
            new GenericTextChannelController(e);
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
        }
    }

}
