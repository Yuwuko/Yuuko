package com.yuuko.core.events;

import com.yuuko.core.events.controllers.GenericGuildController;
import com.yuuko.core.events.controllers.GenericGuildVoiceController;
import com.yuuko.core.events.controllers.GenericMessageController;
import com.yuuko.core.events.controllers.GenericMessageReactionController;
import com.yuuko.core.utilities.MessageHandler;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.guild.GenericGuildEvent;
import net.dv8tion.jda.core.events.guild.voice.GenericGuildVoiceEvent;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;
import net.dv8tion.jda.core.events.message.react.GenericMessageReactionEvent;
import net.dv8tion.jda.core.hooks.InterfacedEventManager;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GenericEventManager extends ListenerAdapter {

    /**
     * Captures and deals with generic guild events.
     * @param e GenericGuildEvent
     */
    @Override
    public void onGenericGuild(GenericGuildEvent e) {
        try {
            new GenericGuildController(e);
        } catch(Exception ex) {
            MessageHandler.sendException(ex, "public void onGenericGuild(GenericGuildEvent e)");
        }
    }

    /**
     * Captures and deals with generic message events.
     * @param e -> GenericMessageEvent.
     */
    @Override
    public void onGenericMessage(GenericMessageEvent e) {
        try {
            new GenericMessageController(e);
        } catch(Exception ex) {
            MessageHandler.sendException(ex, "public void onGenericMessage(GenericMessageEvent e)");
        }
    }

    /**
     * Captures and deals generic reaction events.
     * @param e -> GenericMessageReactionEvent.
     */
    @Override
    public void onGenericMessageReaction(GenericMessageReactionEvent e) {
        try {
            new GenericMessageReactionController(e);
        } catch(Exception ex) {
            MessageHandler.sendException(ex, "public void onGenericMessageReaction(GenericMessageReactionEvent e)");
        }
    }

    /**
     * Captures and deals with generic voice events.
     * @param e -> GenericGuildVoiceEvent.
     */
    @Override
    public void onGenericGuildVoice(GenericGuildVoiceEvent e) {
        try {
            new GenericGuildVoiceController(e);
        } catch(Exception ex) {
            MessageHandler.sendException(ex, "public void onGenericGuildVoice(GenericGuildVoiceEvent e)");
        }
    }

    /**
     * Threaded Event Manager Class
     */
    public static class ThreadedEventManager extends InterfacedEventManager {
        private final ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);

        @Override
        public void handle(Event e) {
            threadPool.submit(() -> super.handle(e));
        }
    }

}
