package com.yuuko.events.entity;

import com.yuuko.i18n.I18n;
import com.yuuko.modules.Command;
import com.yuuko.modules.Module;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class MessageEvent extends GuildMessageReceivedEvent {
    private Command command;
    private String language;
    private String prefix;
    private String parameters;

    public MessageEvent(GuildMessageReceivedEvent event) {
        super(event.getJDA(), event.getResponseNumber(), event.getMessage());
    }

    public String getLanguage() {
        return language;
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
     * Gets a localised string using the given key.
     * @param key {@link String}
     * @return {@link String}
     */
    public String i18n(String key) {
        return I18n.get(language, command.getName(), key);
    }

    /**
     * Gets a localised string using the given key and class.
     * @param key {@link String}
     * @return {@link String}
     */
    public String i18n(String key, String auxiliary) {
        return I18n.get(language, auxiliary, key, true);
    }

    /**
     * Sets language associated with the message event.
     * @param language value to assign to lang field.
     * @return {@link MessageEvent}
     */
    public MessageEvent setLanguage(String language) {
        this.language = language;
        return this;
    }

    /**
     * Sets prefix associated with the message event.
     * @param prefix value to assign to prefix field.
     * @return {@link MessageEvent}
     */
    public MessageEvent setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    /**
     * Sets command associated with the message event.
     * @param command value to assign to command field.
     * @return {@link MessageEvent}
     */
    public MessageEvent setCommand(Command command) {
        this.command = command;
        return this;
    }

    /**
     * Sets parameters associated with the message event.
     * @param parameters value to assign to parameters field.
     * @return {@link MessageEvent}
     */
    public MessageEvent setParameters(String parameters) {
        this.parameters = parameters;
        return this;
    }

    /**
     * Checks if the event command has parameters by checking if the
     * parameters field contains a value other than null.
     * @return boolean
     */
    public boolean hasParameters() {
        return parameters != null;
    }
}
