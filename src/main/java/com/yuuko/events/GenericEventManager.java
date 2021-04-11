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
     * Captures generic guild events.
     * @param event {@link GenericGuildEvent}
     */
    @Override
    public void onGenericGuild(@NotNull GenericGuildEvent event) {
        try {
            new GenericGuildController(event);
        } catch(Exception e) {
            log.error("An error occurred while running the {} class, message: {}", this, e.getMessage(), e);
        }
    }

    /**
     * Captures generic message events.
     * @param event {@link GenericGuildMessageEvent}
     */
    @Override
    public void onGenericGuildMessage(@NotNull GenericGuildMessageEvent event) {
        try {
            new GenericGuildMessageController(event);
        } catch(Exception e) {
            log.error("An error occurred while running the {} class, message: {}", this, e.getMessage(), e);
        }
    }

    /**
     * Captures generic reaction events.
     * @param event {@link GenericGuildMessageReactionEvent}
     */
    @Override
    public void onGenericGuildMessageReaction(@NotNull GenericGuildMessageReactionEvent event) {
        try {
            new GenericGuildMessageReactionController(event);
        } catch(Exception e) {
            log.error("An error occurred while running the {} class, message: {}", this, e.getMessage(), e);
        }
    }

    /**
     * Captures generic voice events.
     * @param event {@link GenericGuildVoiceEvent}
     */
    @Override
    public void onGenericGuildVoice(@NotNull GenericGuildVoiceEvent event) {
        try {
            new GenericGuildVoiceController(event);
        } catch(Exception e) {
            log.error("An error occurred while running the {} class, message: {}", this, e.getMessage(), e);
        }
    }

    /**
     * Captures generic text-channel events.
     * @param event {@link GenericTextChannelEvent}
     */
    @Override
    public void onGenericTextChannel(@NotNull GenericTextChannelEvent event) {
        try {
            new GenericTextChannelController(event);
        } catch(Exception e) {
            log.error("An error occurred while running the {} class, message: {}", this, e.getMessage(), e);
        }
    }

    /**
     * Captures generic emote events.
     * @param event {@link GenericEmoteEvent}
     */
    @Override
    public void onGenericEmote(@NotNull GenericEmoteEvent event) {
        try {
            new GenericEmoteEventController(event);
        } catch(Exception e) {
            log.error("An error occurred while running the {} class, message: {}", this, e.getMessage(), e);
        }
    }

    /**
     * Captures generic role events.
     * @param event {@link GenericRoleEvent}
     */
    @Override
    public void onGenericRole(@NotNull GenericRoleEvent event) {
        try {
            new GenericRoleEventController(event);
        } catch(Exception e) {
            log.error("An error occurred while running the {} class, message: {}", this, e.getMessage(), e);
        }
    }
}
