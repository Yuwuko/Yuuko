package com.yuuko.core.modules.utility;

import com.yuuko.core.CommandExecutor;
import com.yuuko.core.modules.Command;
import com.yuuko.core.modules.Module;
import com.yuuko.core.modules.utility.commands.BindCommand;
import com.yuuko.core.modules.utility.commands.ChannelCommand;
import com.yuuko.core.modules.utility.commands.ServerCommand;
import com.yuuko.core.modules.utility.commands.UserCommand;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageReaction;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionRemoveEvent;

public class UtilityModule extends Module {

    public UtilityModule(MessageReceivedEvent e, String[] command) {
        super("Utility", "moduleUtility", false, new Command[]{
                new UserCommand(),
                new ServerCommand(),
                new BindCommand(),
                new ChannelCommand()
        });

        new CommandExecutor(e, command, this);
    }

    /**
     * Module constructor for MessageReactionAddEvents
     * @param e MessageReactionAddEvent
     */
    public UtilityModule(MessageReactionAddEvent e) {
        super("Utility", "moduleUtility", false, null);

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
        super("Utility", "moduleUtility", false, null);

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