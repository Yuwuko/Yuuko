package com.yuuko.core.commands;

import com.yuuko.core.Configuration;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Command {
    private final String name;
    private final Class<?> module;
    private final int expectedParameters;
    private final String[] usage;
    private final Permission[] permissions;
    private final boolean nsfw;

    protected static final Logger log = LoggerFactory.getLogger(Command.class);

    public Command(String name, Class<?> module, int expectedParameters, String[] usage, boolean nsfw, Permission[] permissions) {
        this.name = name;
        this.module = module;
        this.expectedParameters = expectedParameters;
        this.usage = usage;
        this.nsfw = nsfw;
        this.permissions = permissions;
    }

    public String getName() {
        return name;
    }

    public String getGlobalName() {
        return Configuration.GLOBAL_PREFIX + name;
    }

    public Class<?> getModule() {
        return module;
    }

    public int getExpectedParameters() {
        return expectedParameters;
    }

    public String[] getUsage() {
        return usage;
    }

    public boolean isNSFW() {
        return nsfw;
    }

    public Permission[] getPermissions() {
        return permissions;
    }

    // Abstract method signature to ensure method is implemented.
    public abstract void onCommand(MessageReceivedEvent e, String[] command);
}
