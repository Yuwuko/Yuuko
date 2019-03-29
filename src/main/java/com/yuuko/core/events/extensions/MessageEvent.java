package com.yuuko.core.events.extensions;

import com.yuuko.core.Configuration;
import com.yuuko.core.utilities.Utilities;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class MessageEvent extends MessageReceivedEvent {

    private String prefix;
    private String[] command;

    public MessageEvent(MessageReceivedEvent event) {
        super(event.getJDA(), event.getResponseNumber(), event.getMessage());

        String message = getMessage().getContentRaw();
        this.prefix = message.toLowerCase().startsWith(Utilities.getServerPrefix(getGuild())) ? Utilities.getServerPrefix(getGuild()) : (message.toLowerCase().startsWith(Configuration.GLOBAL_PREFIX) ? Configuration.GLOBAL_PREFIX : "");

        if(!prefix.equals("")) {
            this.command = message.substring(prefix.length()).split("\\s+", 2);
        }
    }

    public String getPrefix() {
        return prefix;
    }

    public String[] getCommand() {
        return command;
    }

    // Only used in very specific scenarios.
    public void setCommand(String[] command) {
        this.command = command;
    }

    public boolean hasParameters() {
        return command.length > 1;
    }

}
