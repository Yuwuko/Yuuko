package com.yuuko.core.modules;

import com.yuuko.core.Configuration;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public abstract class Command {

    private final String commandName;
    private final String commandModule;
    private final String commandDescription = "";
    private final int expectedParameters;
    private final String[] commandUsage;
    private final Permission commandPermission;

    public Command(String commandName, String commandModule, int expectedParameters, String[] commandUsage, Permission commandPermission) {
        this.commandName = commandName;
        this.commandModule = commandModule;
        this.expectedParameters = expectedParameters;
        this.commandUsage = commandUsage;
        this.commandPermission = commandPermission;
    }

    public String getCommandName() {
        return commandName;
    }

    public String getGlobalName() {
        return Configuration.GLOBAL_PREFIX + commandName;
    }

    public String getCommandModule() {
        return commandModule;
    }

    public int getExpectedParameters() {
        return expectedParameters;
    }

    public String[] getCommandUsage() {
        return commandUsage;
    }

    public Permission getCommandPermission() {
        return commandPermission;
    }

    // Abstract method signature to ensure method is implemented.
    public abstract void executeCommand(MessageReceivedEvent e, String[] command);
}
