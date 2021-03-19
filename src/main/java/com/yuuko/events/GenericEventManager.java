package com.yuuko.events;

import com.yuuko.events.controllers.*;
import net.dv8tion.jda.api.events.channel.text.GenericTextChannelEvent;
import net.dv8tion.jda.api.events.emote.GenericEmoteEvent;
import net.dv8tion.jda.api.events.guild.GenericGuildEvent;
import net.dv8tion.jda.api.events.guild.voice.GenericGuildVoiceEvent;
import net.dv8tion.jda.api.events.message.guild.GenericGuildMessageEvent;
import net.dv8tion.jda.api.events.message.guild.react.GenericGuildMessageReactionEvent;
import net.dv8tion.jda.api.events.role.GenericRoleEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
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
    public void onGenericGuild(@NotNull GenericGuildEvent e) {
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
    public void onGenericGuildMessage(@NotNull GenericGuildMessageEvent e) {
        try {
            new GenericGuildMessageController(e);
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
        }
    }

    /**
     * Captures and deals generic reaction events.
     *
     * @param e -> GenericGuildMessageReactionEvent.
     */
    @Override
    public void onGenericGuildMessageReaction(@NotNull GenericGuildMessageReactionEvent e) {
        try {
            new GenericGuildMessageReactionController(e);
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
    public void onGenericGuildVoice(@NotNull GenericGuildVoiceEvent e) {
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
    public void onGenericTextChannel(@NotNull GenericTextChannelEvent e) {
        try {
            new GenericTextChannelController(e);
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
        }
    }

    @Override
    public void onGenericEmote(@NotNull GenericEmoteEvent e) {
        try {
            new GenericEmoteEventController(e);
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
        }
    }

    @Override
    public void onGenericRole(@NotNull GenericRoleEvent e) {
        try {
            new GenericRoleEventController(e);
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
        }
    }
}
