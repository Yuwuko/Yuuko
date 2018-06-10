package com.basketbandit.core.modules;

import com.basketbandit.core.database.DatabaseFunctions;
import com.basketbandit.core.utils.Utils;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public abstract class Module {

    private final String moduleName;
    private final String dbModuleName;

    public Module(String moduleName, String dbModuleName) {
        this.moduleName = moduleName;
        this.dbModuleName = dbModuleName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public String getDbModuleName() {
        return dbModuleName;
    }

    protected boolean checkModuleSettings(MessageReceivedEvent e) {
        if(new DatabaseFunctions().checkModuleSettings(dbModuleName, e.getGuild().getId())) {
            return false;
        } else {
            Utils.sendMessage(e, "Sorry " + e.getAuthor().getAsMention() + ", " + moduleName + " is disabled.");
            return true;
        }
    }

    // Abstract method signature to ensure method is implemented.
    protected abstract void executeCommand(MessageReceivedEvent e, String[] command);
}
