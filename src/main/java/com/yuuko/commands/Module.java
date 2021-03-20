package com.yuuko.commands;

import org.reflections8.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public abstract class Module {
    private final String name;
    private final boolean nsfw;
    private final Map<String, Command> commands = new HashMap<>();

    public Module(String name, boolean isNSFW) {
        this.name = name;
        this.nsfw = isNSFW;
        new Reflections("com.yuuko.commands." + name)
                .getSubTypesOf(Command.class)
                .forEach(clazz -> {
                    try {
                        Command command = clazz.getConstructor().newInstance();
                        command.setModule(this);
                        commands.putIfAbsent(command.getName(), command);
                    } catch(InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                });
    }

    public String getName() {
        return name;
    }

    public Map<String, Command> getCommands() {
        return commands;
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
