package com.yuuko.core.commands.developer.commands;

import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.developer.DeveloperModule;
import com.yuuko.core.database.DatabaseFunctions;
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
            if(DatabaseFunctions.addGuilds(e)) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Guilds added successfully.");
                MessageHandler.sendMessage(e, embed.build());
            }
        } catch(Exception ex) {
            MessageHandler.sendException(ex, command[0]);
        }
    }

}
