package com.yuuko.core.modules;

import com.yuuko.core.database.DatabaseFunctions;
import com.yuuko.core.utilities.MessageHandler;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public abstract class Module {
    private final String name;
    private final String dbColumnName;
    private final boolean isNSFW;
    private final Command[] commands;

    public Module(String name, String dbColumnName, boolean isNSFW, Command[] commands) {
        this.name = name;
        this.dbColumnName = dbColumnName;
        this.commands = commands;
        this.isNSFW = isNSFW;
    }

    public String getName() {
        return name;
    }

    public String getDbColumnName() {
        return dbColumnName;
    }

    public Command[] getCommandsArray() {
        return commands;
    }

    public List<Command> getCommandsList() {
        return Arrays.asList(commands);
    }

    public boolean checkModuleSettings(MessageReceivedEvent e) {
        // Executor still checks core/developer, in this case simply return true.
        if(name.equals("Core") || name.equals("Developer")) {
            return true;
        }

        if(new DatabaseFunctions().checkModuleSettings(dbColumnName, e.getGuild().getId())) {
            return true;
        } else {
            EmbedBuilder member = new EmbedBuilder().setTitle("Module Disabled").setDescription("The **_" + name + "_** module is disabled.");
            MessageHandler.sendMessage(e, member.build());
            return false;
        }
    }

    public boolean isModuleNSFW() {
        return isNSFW;
    }

    public boolean isChannelNSFW(MessageReceivedEvent e) {
        return e.getTextChannel().isNSFW();
    }
}
