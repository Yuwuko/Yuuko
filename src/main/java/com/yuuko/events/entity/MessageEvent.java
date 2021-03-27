package com.yuuko.events.entity;

import com.yuuko.modules.Command;
import com.yuuko.modules.Module;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class MessageEvent extends GuildMessageReceivedEvent {
    private Command command;
    private String prefix;
    private String parameters;

    public MessageEvent(GuildMessageReceivedEvent event) {
        super(event.getJDA(), event.getResponseNumber(), event.getMessage());
    }

    public String getPrefix() {
        return prefix;
    }

    public Module getModule() {
        return command.getModule();
    }

    public Command getCommand() {
        return command;
    }

    public String getParameters() {
        return parameters;
    }

    public int getShardId() {
        return getJDA().getShardInfo().getShardId();
    }

    /**
     * Sets prefix associated with the message event.
     * Returns MessageEvent object so method can be used as a parameter.
     *
     * @param prefix value to assign to prefix field.
     * @return MessageEvent
     */
    public MessageEvent setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    /**
     * Sets command associated with the message event.
     * Returns MessageEvent object so method can be used as a parameter.
     *
     * @param command value to assign to command field.
     * @return MessageEvent
     */
    public MessageEvent setCommand(Command command) {
        this.command = command;
        return this;
    }

    /**
     * Sets parameters associated with the message event.
     * Returns MessageEvent object so method can be used as a parameter.
     *
     * @param parameters value to assign to parameters field.
     * @return MessageEvent
     */
    public MessageEvent setParameters(String parameters) {
        this.parameters = parameters;
        return this;
    }

    /**
     * Checks if the event command has parameters by checking if the
     * parameters field contains a value other than null.
     *
     * @return boolean
     */
    public boolean hasParameters() {
        return parameters != null;
    }
}
