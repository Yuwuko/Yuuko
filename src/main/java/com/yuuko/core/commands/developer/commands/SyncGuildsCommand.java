package com.yuuko.core.commands.developer.commands;

import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.developer.DeveloperModule;
import com.yuuko.core.database.DatabaseFunctions;
import com.yuuko.core.utilities.MessageHandler;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class SyncGuildsCommand extends Command {

    public SyncGuildsCommand() {
        super("syncguilds", DeveloperModule.class, 0, new String[]{"-syncguilds"}, false, null);
    }

    @Override
    public void onCommand(MessageReceivedEvent e, String[] command) {
        try {
            if(DatabaseFunctions.addGuilds(e)) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Guilds added/updated successfully.");
                MessageHandler.sendMessage(e, embed.build());
            }
        } catch(Exception ex) {
            MessageHandler.sendException(ex, command[0]);
        }
    }

}
