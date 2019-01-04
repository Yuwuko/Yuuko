package com.yuuko.core.modules.developer.commands;

import com.yuuko.core.database.DatabaseFunctions;
import com.yuuko.core.modules.Command;
import com.yuuko.core.modules.developer.DeveloperModule;
import com.yuuko.core.utilities.MessageHandler;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class SyncServersCommand extends Command {

    public SyncServersCommand() {
        super("syncservers", DeveloperModule.class, 0, new String[]{"-syncservers"}, null);
    }

    @Override
    public void executeCommand(MessageReceivedEvent e, String[] command) {
        try {
            if(new DatabaseFunctions().addServers(e)) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Servers added successfully.");
                MessageHandler.sendMessage(e, embed.build());
            }
        } catch(Exception ex) {
            MessageHandler.sendException(ex, command[0]);
        }
    }

}
