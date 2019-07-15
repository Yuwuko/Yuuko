package com.yuuko.core.commands;

import com.yuuko.core.events.entity.MessageEvent;
import net.dv8tion.jda.api.Permission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public abstract class Command {
    private final String name;
    private final Module module;
    private final int expectedParameters;
    private final List<String> usage;
    private final List<Permission> permissions;
    private final boolean nsfw;

    protected static final Logger log = LoggerFactory.getLogger(Command.class);

    public Command(String name, Module module, int expectedParameters, List<String> usage, boolean nsfw, List<Permission> permissions) {
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

    public Module getModule() {
        return module;
    }

    public int getExpectedParameters() {
        return expectedParameters;
    }

    public List<String> getUsage() {
        return usage;
    }

    public boolean isNSFW() {
        return nsfw;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    // Abstract method signature to ensure method is implemented.
    public abstract void onCommand(MessageEvent e);
}
