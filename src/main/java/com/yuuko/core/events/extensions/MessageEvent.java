package com.yuuko.core.events.extensions;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class MessageEvent extends MessageReceivedEvent {

    private final String[] command;

    public MessageEvent(MessageReceivedEvent event, String[] command) {
        super(event.getJDA(), event.getResponseNumber(), event.getMessage());
        this.command = command;
    }

    public String[] getCommand() {
        return command;
    }

    public String getCommandAction() {
        return command[0];
    }

    public String getCommandParameter() {
        return (command.length > 1) ? command[1] : "";
    }
}
