package com.yuuko.core.modules;

import com.yuuko.core.database.DatabaseFunctions;
import com.yuuko.core.utils.MessageHandler;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public abstract class Module {
    private final String moduleName;
    private final String dbModuleName;
    private final boolean isNSFW;
    private final Command[] moduleCommands;

    public Module(String moduleName, String dbModuleName, boolean isNSFW, Command[] moduleCommands) {
        this.moduleName = moduleName;
        this.dbModuleName = dbModuleName;
        this.moduleCommands = moduleCommands;
        this.isNSFW = isNSFW;
    }

    public String getModuleName() {
        return moduleName;
    }

    public Command[] getModuleCommandsArray() {
        return moduleCommands;
    }

    public List<Command> getModuleCommandsList() {
        return Arrays.asList(moduleCommands);
    }

    public boolean checkModuleSettings(MessageReceivedEvent e) {
        // Executor still checks core, in this case simply return true.
        if(moduleName.equals("Core") || moduleName.equals("Developer")) {
            return true;
        }

        if(new DatabaseFunctions().checkModuleSettings(dbModuleName, e.getGuild().getId())) {
            return true;
        } else {
            EmbedBuilder member = new EmbedBuilder().setTitle("Module Disabled").setDescription("The **_" + moduleName + "_** module is disabled.");
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
