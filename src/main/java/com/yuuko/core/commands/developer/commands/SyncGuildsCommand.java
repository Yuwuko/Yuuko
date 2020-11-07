package com.yuuko.core.commands.developer.commands;

import com.yuuko.core.Configuration;
import com.yuuko.core.MessageHandler;
import com.yuuko.core.commands.Command;
import com.yuuko.core.database.function.GuildFunctions;
import com.yuuko.core.events.entity.MessageEvent;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;

public class SyncGuildsCommand extends Command {

    public SyncGuildsCommand() {
        super("syncguilds", Configuration.MODULES.get("developer"), 0, -1L, Arrays.asList("-syncguilds"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
        try {
            e.getJDA().getGuildCache().forEach(GuildFunctions::verifyIntegrity);
            EmbedBuilder embed = new EmbedBuilder().setTitle("Guilds updated.");
            MessageHandler.sendMessage(e, embed.build());
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this, ex.getMessage(), ex);
        }
    }

}
