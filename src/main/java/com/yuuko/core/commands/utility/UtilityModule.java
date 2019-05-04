package com.yuuko.core.commands.utility;

import com.yuuko.core.CommandExecutor;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.Module;
import com.yuuko.core.commands.utility.commands.*;
import com.yuuko.core.events.extensions.MessageEvent;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageReaction;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionRemoveEvent;

public class UtilityModule extends Module {
    private static final Command[] commands = new Command[]{
            new UserCommand(),
            new GuildCommand(),
            new BindCommand(),
            new ChannelCommand(),
            new AvatarCommand(),
            new RolesCommand(),
            new PingCommand()
    };

    public UtilityModule(MessageEvent e) {
        super("utility", false, commands);
        new CommandExecutor(e, this);
    }

    /**
     * Module constructor for MessageReactionAddEvents
     * @param e MessageReactionAddEvent
     */
    public UtilityModule(MessageReactionAddEvent e) {
        super("utility", false, new Command[]{});

        if(e.getReaction().getReactionEmote().getName().equals("📌")) {
            Message message = e.getTextChannel().getMessageById(e.getMessageId()).complete();
            message.pin().queue();
        }
    }

    /**
     * Module constructor for MessageReactionRemoveEvents
     * @param e MessageReactionRemoveEvent
     */
    public UtilityModule(MessageReactionRemoveEvent e) {
        super("utility", false, new Command[]{});

        Message message = e.getTextChannel().getMessageById(e.getMessageId()).complete();

        if(e.getReactionEmote().getName().equals("📌")) {
            for(MessageReaction emote: message.getReactions()) {
                if(emote.getReactionEmote().getName().equals("📌")) {
                    return;
                }
            }
            message.unpin().queue();
        }
    }

}