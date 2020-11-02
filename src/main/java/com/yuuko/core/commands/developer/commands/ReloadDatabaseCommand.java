package com.yuuko.core.commands.developer.commands;

import com.yuuko.core.Configuration;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.database.connection.DatabaseConnection;
import com.yuuko.core.events.entity.MessageEvent;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;

public class ReloadDatabaseCommand extends Command {

    public ReloadDatabaseCommand() {
        super("redb", Configuration.MODULES.get("developer"), 0, -1L, Arrays.asList("-redb"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        try {
            new DatabaseConnection();

            EmbedBuilder embed = new EmbedBuilder().setTitle("Successfully reloaded Metrics/Settings databases.");
            MessageHandler.sendMessage(e, embed.build());
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
        }
    }
}
