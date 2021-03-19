package com.yuuko.commands.developer.commands;

import com.yuuko.MessageDispatcher;
import com.yuuko.Yuuko;
import com.yuuko.commands.Command;
import com.yuuko.commands.core.commands.BindCommand;
import com.yuuko.database.function.GuildFunctions;
import com.yuuko.events.entity.MessageEvent;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;

public class SyncGuildsCommand extends Command {

    public SyncGuildsCommand() {
        super("syncguilds", Yuuko.MODULES.get("developer"), 0, -1L, Arrays.asList("-syncguilds"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) throws Exception {
        e.getJDA().getGuildCache().forEach(guild -> {
            GuildFunctions.addOrUpdateGuild(guild);
            BindCommand.DatabaseInterface.verifyBinds(guild);
        });
        EmbedBuilder embed = new EmbedBuilder().setTitle("Guilds updated.");
        MessageDispatcher.reply(e, embed.build());
    }

}
