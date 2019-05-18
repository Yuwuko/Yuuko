package com.yuuko.core.commands;

import com.yuuko.core.MessageHandler;
import com.yuuko.core.database.function.CommandFunctions;
import com.yuuko.core.events.extensions.MessageEvent;
import com.yuuko.core.utilities.Utilities;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public abstract class Command {
    private final String name;
    private final Class<?> module;
    private final int expectedParameters;
    private final List<String> usage;
    private final List<Permission> permissions;
    private final boolean nsfw;

    protected static final Logger log = LoggerFactory.getLogger(Command.class);

    private static final List<String> staticModules = Arrays.asList("core", "developer");

    public Command(String name, Class<?> module, int expectedParameters, List<String> usage, boolean nsfw, List<Permission> permissions) {
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

    public Class<?> getModule() {
        return module;
    }

    public String getModuleName() {
        return Utilities.getModuleName(module);
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

    public boolean isEnabled(MessageReceivedEvent e) {
        // Executor still checks core/developer, in this case simply return true.
        if(staticModules.contains(getModuleName())) {
            return true;
        }

        // Checks if the command is globally disabled either global:global, global:local, local:global or local:local.
        String guild = e.getGuild().getId();
        String channel = e.getTextChannel().getId();
        if(!CommandFunctions.isDisabled(guild, "*", "*") &&
           !CommandFunctions.isDisabled(guild, channel, "*") &&
           !CommandFunctions.isDisabled(guild, "*", name) &&
           !CommandFunctions.isDisabled(guild, channel, name)) {
            return true;
        } else {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Command Disabled").setDescription("The `" + name + "` command is disabled.");
            MessageHandler.sendMessage(e, embed.build());
            return false;
        }
    }

    // Abstract method signature to ensure method is implemented.
    public abstract void onCommand(MessageEvent e);
}
