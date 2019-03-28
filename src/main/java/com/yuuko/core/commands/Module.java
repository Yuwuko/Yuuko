package com.yuuko.core.commands;

import com.yuuko.core.MessageHandler;
import com.yuuko.core.database.ModuleFunctions;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public abstract class Module {
    private final String name;
    private final boolean nsfw;
    private final Command[] commands;

    public Module(String name, boolean isNSFW, Command[] commands) {
        this.name = name;
        this.commands = commands;
        this.nsfw = isNSFW;
    }

    public String getName() {
        return name;
    }

    public Command[] getCommandsAsArray() {
        return commands;
    }

    public List<Command> getCommandsAsList() {
        return Arrays.asList(commands);
    }

    public String getCommandsAsString() {
        StringBuilder string = new StringBuilder();
        for(Command command: commands) {
            string.append("`").append(command.getName()).append("` ");
        }
        return string.toString();
    }

    public boolean isEnabled(MessageReceivedEvent e) {
        // Executor still checks core/developer, in this case simply return true.
        if(name.equals("Core") || name.equals("Developer")) {
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
