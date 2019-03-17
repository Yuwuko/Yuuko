package com.yuuko.core.commands.developer.commands;

import com.yuuko.core.Configuration;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.developer.DeveloperModule;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ReloadApiCommand extends Command {

    public ReloadApiCommand() {
        super("reapi", DeveloperModule.class, 1, new String[]{"-reapi"}, false, null);
    }

    @Override
    public void onCommand(MessageReceivedEvent e, String[] command) {
        try {
            Configuration.loadApi();
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
        }
    }
}
