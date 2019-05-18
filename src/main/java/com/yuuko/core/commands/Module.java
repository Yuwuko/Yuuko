package com.yuuko.core.commands;

import com.yuuko.core.MessageHandler;
import com.yuuko.core.database.function.ModuleFunctions;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Module {
    private final String name;
    private final boolean nsfw;
    private final Map<String, Command> commands = new HashMap<>();

    public Module(String name, boolean isNSFW, List<Command> commands) {
        this.name = name;
        this.nsfw = isNSFW;
        if(commands != null) {
            for(Command command : commands) {
                this.commands.put(command.getName(), command);
            }
        }
    }

    public String getName() {
        return name;
    }

    public Map<String, Command> getCommands() {
        return commands;
    }

    public String getCommandsAsString() {
        StringBuilder string = new StringBuilder();
        for(Command command: commands.values()) {
            string.append("`").append(command.getName()).append("` ");
        }
        return string.toString();
    }

    public boolean isEnabled(MessageReceivedEvent e) {
        // Executor still checks core/developer, in this case simply return true.
        if(name.equals("core") || name.equals("developer")) {
            return true;
        }

        if(ModuleFunctions.isEnabled(e.getGuild().getId(), name)) {
            return true;
        } else {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Module Disabled").setDescription("The `" + name + "` module is disabled.");
            MessageHandler.sendMessage(e, embed.build());
            return false;
        }
    }

    public boolean isNSFW() {
        return nsfw;
    }
}
