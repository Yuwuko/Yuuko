package com.yuuko.modules;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public abstract class Module {
    protected static final Logger log = LoggerFactory.getLogger(Module.class);
    private final String name;
    private final boolean nsfw;
    private final Map<String, Command> commands = new HashMap<>();

    public Module(String name, boolean isNSFW) {
        this.name = name;
        this.nsfw = isNSFW;
        new Reflections("com.yuuko.modules." + name)
                .getSubTypesOf(Command.class)
                .forEach(clazz -> {
                    try {
                        Command command = clazz.getConstructor().newInstance();
                        command.setModule(this);
                        commands.putIfAbsent(command.getName(), command);
                    } catch(InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        log.error("Something went wrong during the reflection process, message: {}", e.getMessage(), e);
                    }
                });
    }

    public String getName() {
        return name;
    }

    public Map<String, Command> getCommands() {
        return commands;
    }

    public boolean isNSFW() {
        return nsfw;
    }

    public String getCommandsAsString() {
        StringBuilder string = new StringBuilder();
        for(String name : commands.keySet()) {
            string.append("`").append(name).append("` ");
        }
        return string.toString();
    }

}
