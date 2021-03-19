package com.yuuko.core.commands;

import org.reflections8.Reflections;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Module {
    private final String name;
    private final boolean nsfw;
    private final Map<String, Class<? extends Command>> commands = new HashMap<>();

    public Module(String name, boolean isNSFW) {
        this.name = name;
        this.nsfw = isNSFW;
        new Reflections("com.yuuko.core.commands." + name)
                .getSubTypesOf(Command.class)
                .forEach(aClass -> commands.putIfAbsent(aClass.getName(), aClass));
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
