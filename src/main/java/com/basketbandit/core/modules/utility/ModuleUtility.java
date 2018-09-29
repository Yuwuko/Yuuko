package com.basketbandit.core.modules.utility;

import com.basketbandit.core.modules.C;
import com.basketbandit.core.modules.Module;
import com.basketbandit.core.modules.utility.commands.*;
import com.basketbandit.core.utils.Utils;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageReaction;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionRemoveEvent;

public class ModuleUtility extends Module {

    public ModuleUtility() {
        super("ModuleUtility", "moduleUtility");
    }

    /**
     * Module constructor for MessageReceivedEvents
     * @param e MessageReceivedEvent
     */
    public ModuleUtility(MessageReceivedEvent e, String[] command) {
        super("ModuleUtility", "moduleUtility");

        if(checkModuleSettings(e)) {
            return;
        }

        executeCommand(e, command);
    }

    /**
     * Module constructor for MessageReactionAddEvents
     * @param e MessageReactionAddEvent
     */
    public ModuleUtility(MessageReactionAddEvent e) {
        super("ModuleUtility", "moduleUtility");

        if(e.getReaction().getReactionEmote().getName().equals("📌")) {
            Message message = e.getTextChannel().getMessageById(e.getMessageId()).complete();
            message.pin().queue();
        }
    }

    /**
     * Module constructor for MessageReactionRemoveEvents
     * @param e MessageReactionRemoveEvent
     */
    public ModuleUtility(MessageReactionRemoveEvent e) {
        super("ModuleUtility", "moduleUtility");

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

    @Override
    protected void executeCommand(MessageReceivedEvent e, String[] command) {
        if(command[0].equals(C.USER.getCommandName())) {
            new CommandUser(e, command);
            return;
        }

        if(command[0].equals(C.SERVER.getCommandName())) {
            new CommandServer(e, command);
            return;
        }

        if(command[0].equals(C.BIND.getCommandName()) && (e.getMember().hasPermission(C.BIND.getCommandPermission()) || e.getMember().hasPermission(e.getTextChannel(), C.BIND.getCommandPermission()))) {
            new CommandBind(e, command);
            return;
        }

        if(command[0].equals(C.EXCLUDE.getCommandName()) && (e.getMember().hasPermission(C.EXCLUDE.getCommandPermission()) || e.getMember().hasPermission(e.getTextChannel(), C.EXCLUDE.getCommandPermission()))) {
            new CommandExclude(e, command);
            return;
        }

        if(command[0].equals(C.CHANNEL.getCommandName()) && (e.getMember().hasPermission(C.CHANNEL.getCommandPermission()) || e.getMember().hasPermission(e.getTextChannel(), C.CHANNEL.getCommandPermission())) && (e.getMember().hasPermission(C.CHANNEL.getCommandPermission()) || e.getMember().hasPermission(e.getTextChannel(), C.CHANNEL.getCommandPermission()))) {
            new CommandChannel(e, command);
            return;
        }

        if(command[0].equals(C.WEATHER.getCommandName())) {
            new CommandWeather(e, command);
            return;
        }

        Utils.sendMessage(e, "Sorry " + e.getAuthor().getAsMention() + ", you lack the required permissions to use that command.");
    }
}