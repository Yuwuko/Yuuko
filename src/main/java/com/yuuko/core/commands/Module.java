package com.yuuko.core.commands;

import java.util.List;
import java.util.Map;

public abstract class Module {
    private final String name;
    private final boolean nsfw;
    private final Map<String, Class<? extends Command>> commands;

    public Module(String name, boolean isNSFW, Map<String, Class<? extends Command>> commands) {
        this.name = name;
        this.nsfw = isNSFW;
        this.commands = commands;
    }

    public String getName() {
        return name;
    }

    public List<Class<? extends Command>> getCommands() {
        return List.copyOf(commands.values());
    }

    public String getCommandsAsString() {
        StringBuilder string = new StringBuilder();
        for(String name : commands.keySet()) {
            string.append("`").append(name).append("` ");
        }
        return string.toString();
    }

    public boolean isNSFW() {
        return nsfw;
    }
}
