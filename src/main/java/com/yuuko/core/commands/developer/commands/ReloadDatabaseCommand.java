package com.yuuko.core.commands.developer.commands;

import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.developer.DeveloperModule;
import com.yuuko.core.database.connection.MetricsDatabaseConnection;
import com.yuuko.core.database.connection.SettingsDatabaseConnection;
import com.yuuko.core.events.extensions.MessageEvent;
import net.dv8tion.jda.core.EmbedBuilder;

public class ReloadDatabaseCommand extends Command {

    public ReloadDatabaseCommand() {
        super("redb", DeveloperModule.class, 0, new String[]{"-redb"}, false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        try {
            new MetricsDatabaseConnection();
            new SettingsDatabaseConnection();

            EmbedBuilder embed = new EmbedBuilder().setTitle("Successfully reloaded Metrics/Settings databases.");
            MessageHandler.sendMessage(e, embed.build());
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
        }
    }
}
