package com.yuuko.core.modules.developer.commands;

import com.yuuko.core.database.DatabaseFunctions;
import com.yuuko.core.modules.Command;
import com.yuuko.core.utils.MessageHandler;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandAddServers extends Command {

    public CommandAddServers() {
        super("addservers", "com.yuuko.core.modules.developer.ModuleDeveloper", 0, new String[]{"-addservers"}, null);
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
