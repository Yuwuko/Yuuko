package com.yuuko.core.commands.developer.commands;

import com.yuuko.core.Configuration;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.developer.DeveloperModule;
import com.yuuko.core.events.extensions.MessageEvent;
import net.dv8tion.jda.core.EmbedBuilder;

public class ReloadApiCommand extends Command {

    public ReloadApiCommand() {
        super("reapi", DeveloperModule.class, 0, new String[]{"-reapi"}, false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        try {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Successfully reloaded " + Configuration.loadApi() + " API Keys.");
            MessageHandler.sendMessage(e, embed.build());
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
        }
    }
}
