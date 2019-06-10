package com.yuuko.core.events.entity;

import com.yuuko.core.Configuration;
import com.yuuko.core.utilities.Utilities;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class MessageEvent extends GuildMessageReceivedEvent {

    private String prefix;
    private List<String> command;

    public MessageEvent(GuildMessageReceivedEvent event) {
        super(event.getJDA(), event.getResponseNumber(), event.getMessage());

        String message = getMessage().getContentRaw();
        this.prefix = message.toLowerCase().startsWith(Utilities.getServerPrefix(getGuild())) ? Utilities.getServerPrefix(getGuild()) : (message.toLowerCase().startsWith(Configuration.GLOBAL_PREFIX) ? Configuration.GLOBAL_PREFIX : "");

        if(!prefix.equals("")) {
            this.command = Arrays.asList(message.substring(prefix.length()).split("\\s+", 2));
            this.command.set(0, command.get(0).toLowerCase());
        }
    }

    public String getPrefix() {
        return prefix;
    }

    public List<String> getCommand() {
        return command;
    }

    // Only used in very specific scenarios.
    public void setCommand(List<String> command) {
        this.command = command;
    }

    public boolean hasParameters() {
        return command.size() > 1;
    }

}
