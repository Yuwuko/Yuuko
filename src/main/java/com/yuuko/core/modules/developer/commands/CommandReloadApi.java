package com.yuuko.core.modules.developer.commands;

import com.yuuko.core.Configuration;
import com.yuuko.core.modules.Command;
import com.yuuko.core.utils.MessageHandler;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandReloadApi extends Command {

    public CommandReloadApi() {
        super("reloadapi", "com.yuuko.core.modules.developer.ModuleDeveloper", 1, new String[]{"-reloadapi [type] [status]"}, null);
    }

    @Override
    public void executeCommand(MessageReceivedEvent e, String[] command) {
        try {
            Configuration.loadApi();
        } catch(Exception ex) {
            MessageHandler.sendException(ex, e.getMessage().getContentRaw());
        }
    }
}
