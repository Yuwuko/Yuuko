package basketbandit.core.commands;

import basketbandit.core.Configuration;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public abstract class Command {

    private final String commandName;
    private final String commandModule;
    private final Permission commandPermission;

    Command(String name, String module, Permission permission) {
        this.commandName = name;
        this.commandModule = module;
        this.commandPermission = permission;
    }

    public String getCommandName() {
        return commandName;
    }

    public String getEffectiveName() {
        return Configuration.PREFIX + commandName;
    }

    public String getCommandModule() {
        return commandModule;
    }

    public Permission getCommandPermission() {
        return commandPermission;
    }

    // Abstract method signature to ensure method is implemented.
    protected abstract boolean executeCommand(MessageReceivedEvent e);
}
