package com.yuuko.core.modules;

import com.yuuko.core.database.DatabaseFunctions;
import com.yuuko.core.utils.MessageHandler;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public abstract class Module {

    private final String moduleName;
    private final String dbModuleName;
    private final Command[] moduleCommands;

    public Module(String moduleName, String dbModuleName, Command[] moduleCommands) {
        this.moduleName = moduleName;
        this.dbModuleName = dbModuleName;
        this.moduleCommands = moduleCommands;
    }

    public String getModuleName() {
        return moduleName;
    }

    public Command[] getModuleCommands() {
        return moduleCommands;
    }

    protected boolean checkModuleSettings(MessageReceivedEvent e) {
        if(new DatabaseFunctions().checkModuleSettings(dbModuleName, e.getGuild().getId())) {
            return false;
        } else {
            MessageHandler.sendMessage(e, "Sorry " + e.getAuthor().getAsMention() + ", '" + moduleName.substring(6).toLowerCase() + "' is disabled.");
            return true;
        }
    }

    protected boolean isNSFW(MessageReceivedEvent e) {
        return e.getTextChannel().isNSFW();
    }
}
