package com.yuuko.core.commands.developer.commands;

import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.developer.DeveloperModule;
import com.yuuko.core.database.function.GuildFunctions;
import com.yuuko.core.events.extensions.MessageEvent;
import net.dv8tion.jda.core.EmbedBuilder;

import java.util.Arrays;

public class SyncGuildsCommand extends Command {

    public SyncGuildsCommand() {
        super("syncguilds", DeveloperModule.class, 0, Arrays.asList("-syncguilds"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        try {
            if(GuildFunctions.addGuilds(e.getJDA()) && GuildFunctions.purgeGuilds()) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Guilds added/updated/deleted successfully.");
                MessageHandler.sendMessage(e, embed.build());
            }
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
        }
    }

}
